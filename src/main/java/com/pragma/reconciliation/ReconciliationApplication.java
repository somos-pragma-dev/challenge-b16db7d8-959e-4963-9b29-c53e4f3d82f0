package com.pragma.reconciliation;

import com.pragma.reconciliation.application.events.MovementReconciledEvent;
import com.pragma.reconciliation.domain.services.ReconciliationService;
import com.pragma.reconciliation.infrastructure.adapters.ReconciliationKafkaConsumer;
import com.pragma.reconciliation.infrastructure.config.ReconciliationConfig;
import com.pragma.reconciliation.infrastructure.messaging.KafkaConfig;
import com.pragma.reconciliation.infrastructure.monitoring.LagMonitor;
import com.pragma.reconciliation.infrastructure.persistence.ReconciliationJpaRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@EnableConfigurationProperties(ReconciliationConfig.class)
@Import({
    ReconciliationService.class,
    ReconciliationJpaRepository.class,
    ReconciliationKafkaConsumer.class,
    KafkaConfig.class,
    LagMonitor.class
})
public class ReconciliationApplication {
    
    private static final Logger log = LoggerFactory.getLogger(ReconciliationApplication.class);
    
    private final ReconciliationService reconciliationService;
    private final ReconciliationJpaRepository repository;
    private final LagMonitor lagMonitor;
    private final MeterRegistry meterRegistry;
    private final ReconciliationConfig config;
    
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicLong processedCount = new AtomicLong(0);
    private final AtomicLong failedCount = new AtomicLong(0);
    
    public ReconciliationApplication(
            ReconciliationService reconciliationService,
            ReconciliationJpaRepository repository,
            LagMonitor lagMonitor,
            MeterRegistry meterRegistry,
            ReconciliationConfig config) {
        this.reconciliationService = reconciliationService;
        this.repository = repository;
        this.lagMonitor = lagMonitor;
        this.meterRegistry = meterRegistry;
        this.config = config;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(ReconciliationApplication.class, args);
    }
    
    @Bean
    public Timer reconciliationTimer(MeterRegistry registry) {
        return Timer.builder("reconciliation.processing.time")
                .description("Tiempo total de procesamiento de conciliacion")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);
    }
    
    @Bean
    public Flux<MovementReconciledEvent> reconciliationStream() {
        return Flux.defer(() -> {
            if (!running.compareAndSet(false, true)) {
                log.warn("El stream de conciliacion ya esta en ejecucion");
                return Flux.empty();
            }
            
            int matchingWindowMinutes = config.getMatchingWindow() != null 
                ? config.getMatchingWindow().getMinutes() 
                : 60;
            int pollIntervalSeconds = config.getProcessing() != null 
                ? config.getProcessing().getPollIntervalSeconds() 
                : 30;
            
            log.info("Iniciando stream de conciliacion con ventana de {} minutos", 
                    matchingWindowMinutes);
            
            Instant threshold = Instant.now().minus(Duration.ofMinutes(matchingWindowMinutes));
            
            return repository.findPendingOlderThan(threshold)
            .flatMap(reconciliation -> {
                var command = new com.pragma.reconciliation.application.commands.ReconcileMovementCommand(
                    reconciliation.getAccountId(),
                    reconciliation.getMovementId(),
                    reconciliation.getSource(),
                    reconciliation.getEventId(),
                    reconciliation.getVersion(),
                    reconciliation.getAmount(),
                    reconciliation.getCurrency(),
                    reconciliation.getTransactionDate(),
                    java.util.List.of()
                );
                return reconciliationService.reconcileMovement(command)
                    .doOnSuccess(event -> {
                        processedCount.incrementAndGet();
                        log.debug("Movimiento conciliado: eventId={}, status={}", 
                                event.eventId(), event.status());
                    })
                    .doOnError(error -> {
                        failedCount.incrementAndGet();
                        log.error("Error al conciliar movimiento: eventId={}, error={}", 
                                command.eventId(), error.getMessage());
                    })
                    .onErrorResume(error -> {
                        log.error("Reconciliacion fallida para eventId: {}", 
                                command.eventId(), error);
                        return Mono.empty();
                    });
            })
            .subscribeOn(Schedulers.boundedElastic())
            .doFinally(signal -> {
                running.set(false);
                log.info("Stream de conciliacion detenido. Procesados: {}, Fallidos: {}", 
                        processedCount.get(), failedCount.get());
            });
        }).repeat().delayUntil(x -> Mono.delay(Duration.ofSeconds(pollIntervalSeconds)));
    }
    
    @Bean
    public ReconciliationKafkaConsumer kafkaConsumer(
            ReconciliationService service,
            LagMonitor monitor) {
        return new ReconciliationKafkaConsumer(service, monitor);
    }
    
    public record HealthStatus(
            boolean running,
            long processedCount,
            long failedCount,
            Instant lastProcessedAt,
            long currentLagSeconds,
            boolean lagAlertActive
    ) {}
    
    public HealthStatus getHealthStatus() {
        return new HealthStatus(
                running.get(),
                processedCount.get(),
                failedCount.get(),
                null,
                lagMonitor.getCurrentLagSeconds().block() != null ? lagMonitor.getCurrentLagSeconds().block() : 0,
                lagMonitor.isAlertActive() > 0
        );
    }
}