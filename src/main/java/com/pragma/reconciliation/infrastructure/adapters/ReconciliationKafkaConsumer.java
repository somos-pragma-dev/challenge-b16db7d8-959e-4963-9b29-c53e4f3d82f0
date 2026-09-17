package com.pragma.reconciliation.infrastructure.adapters;

import com.pragma.reconciliation.application.commands.ReconcileMovementCommand;
import com.pragma.reconciliation.application.events.MovementReconciledEvent;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.ports.in.ReconciliationServicePort;
import com.pragma.reconciliation.domain.services.Reprocesamiento;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ReconciliationKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationKafkaConsumer.class);
    private static final String CONSUMER_GROUP = "reconciliation-processor";
    private static final int BATCH_SIZE = 100;
    private static final Duration PROCESSING_TIMEOUT = Duration.ofSeconds(30);
    private static final int MAX_RETRY_ATTEMPTS = 3;

    private final ReconciliationServicePort reconciliationService;
    private final Reprocesamiento reprocesamiento;
    private final MeterRegistry meterRegistry;
    private final Counter messagesReceivedCounter;
    private final Counter messagesProcessedCounter;
    private final Counter messagesFailedCounter;
    private final Timer processingTimer;

    public ReconciliationKafkaConsumer(
            ReconciliationServicePort reconciliationService,
            Reprocesamiento reprocesamiento,
            MeterRegistry meterRegistry
    ) {
        this.reconciliationService = reconciliationService;
        this.reprocesamiento = reprocesamiento;
        this.meterRegistry = meterRegistry;
        this.messagesReceivedCounter = Counter.builder("kafka.messages.received")
                .description("Total de mensajes recibidos desde Kafka")
                .register(meterRegistry);
        this.messagesProcessedCounter = Counter.builder("kafka.messages.processed")
                .description("Total de mensajes procesados exitosamente")
                .register(meterRegistry);
        this.messagesFailedCounter = Counter.builder("kafka.messages.failed")
                .description("Total de mensajes que fallaron en procesamiento")
                .register(meterRegistry);
        this.processingTimer = Timer.builder("kafka.messages.processing.time")
                .description("Tiempo de procesamiento de mensajes")
                .register(meterRegistry);
    }

    @KafkaListener(
            topics = "${reconciliation.kafka.topics.movements:banking.movements}",
            groupId = "${reconciliation.kafka.consumer.group:reconciliation-processor}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeMovements(List<ReconcileMovementCommand> commands, Acknowledgment ack) {
        log.info("Recibidos {} movimientos para conciliación", commands.size());
        messagesReceivedCounter.increment(commands.size());

        AtomicInteger processedCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);

        Flux.fromIterable(commands)
                .flatMap(this::processMovement, BATCH_SIZE)
                .doOnNext(result -> {
                    if (result.isPresent()) {
                        processedCount.incrementAndGet();
                        messagesProcessedCounter.increment();
                        log.debug("Movimiento procesado exitosamente: {}", result.get().getMovementId());
                    } else {
                        failedCount.incrementAndGet();
                        messagesFailedCounter.increment();
                        log.warn("Movimiento no procesado correctamente");
                    }
                })
                .doOnError(error -> {
                    log.error("Error en procesamiento por lotes: {}", error.getMessage(), error);
                    messagesFailedCounter.increment(commands.size());
                })
                .timeout(PROCESSING_TIMEOUT)
                .doFinally(signal -> {
                    log.info("Lote procesado - Exitosos: {}, Fallidos: {}", 
                            processedCount.get(), failedCount.get());
                    ack.acknowledge();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    private Mono<Reconciliation> processMovement(ReconcileMovementCommand command) {
        return Timer.resource(processingTimer, () -> 
            reconciliationService.reconcileMovement(command)
                .doOnSuccess(reconciliation -> {
                    if (reconciliation.isIdempotencyProcessed()) {
                        log.info("Movimiento ya procesado (idempotente): eventId={}, version={}",
                                command.eventId(), command.version());
                    }
                })
                .doOnError(error -> {
                    log.error("Error procesando movimiento: eventId={}, error={}",
                            command.eventId(), error.getMessage(), error);
                })
        );
    }

    @KafkaListener(
            topics = "${reconciliation.kafka.topics.reprocess:banking.movements.reprocess}",
            groupId = "${reconciliation.kafka.consumer.group:reconciliation-processor}-reprocess",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeReprocessRequests(ReconcileMovementCommand command, Acknowledgment ack) {
        log.info("Recibido request de reprocesamiento para eventId={}, version={}",
                command.eventId(), command.version());

        reconciliationService.reconcileMovement(command)
                .doOnSuccess(reconciliation -> {
                    log.info("Reprocesamiento completado: id={}, status={}",
                            reconciliation.getId(), reconciliation.getStatus());
                    ack.acknowledge();
                })
                .doOnError(error -> {
                    log.error("Error en reprocesamiento: eventId={}, error={}",
                            command.eventId(), error.getMessage(), error);
                    ack.acknowledge();
                })
                .subscribe();
    }

    public Flux<MovementReconciledEvent> consumeMovementsStream() {
        return Flux.empty();
    }

    public Mono<Reconciliation> consume(ReconcileMovementCommand command) {
        return reconciliationService.reconcileMovement(command)
                .doOnSuccess(reconciliation -> {
                    log.info("Mensaje procesado: id={}, status={}", 
                            reconciliation.getId(), reconciliation.getStatus());
                })
                .doOnError(error -> {
                    log.error("Error al consumir mensaje: {}", error.getMessage(), error);
                });
    }

    public Flux<Reconciliation> consumeBatch(List<ReconcileMovementCommand> commands) {
        return reconciliationService.processReconciliationBatch(commands)
                .doOnNext(reconciliation -> {
                    log.debug("Procesado en batch: {}", reconciliation.getMovementId());
                })
                .doOnComplete(() -> {
                    log.info("Batch procesado completamente: {} mensajes", commands.size());
                });
    }

    public Mono<Reconciliation> consumeWithRetry(ReconcileMovementCommand command, int maxAttempts) {
        return consume(command)
                .retryWhen(reactor.util.retry.Retry.backoff(maxAttempts, Duration.ofSeconds(2))
                        .doBeforeRetry(signal -> {
                            log.warn("Reintentando mensaje, intento {} para eventId={}",
                                    signal.totalRetries() + 1, command.eventId());
                        })
                        .onRetryExhaustedThrow((spec, signal) -> {
                            log.error("Exhaustos los reintentos para eventId={}", command.eventId());
                            return signal.failure();
                        }));
    }

    public Mono<Reconciliation> reprocessFromOffset(long offset) {
        Instant threshold = Instant.now().minusSeconds(offset * 60);
        return reprocesamiento.reprocessOlderThan(threshold)
                .take(1)
                .singleOrEmpty()
                .doOnNext(r -> log.info("Reprocesado desde offset {}: id={}", offset, r.getId()));
    }
}