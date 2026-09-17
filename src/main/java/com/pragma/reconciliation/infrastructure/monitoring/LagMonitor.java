package com.pragma.reconciliation.infrastructure.monitoring;

import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import com.pragma.reconciliation.domain.ports.in.ReconciliationServicePort;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class LagMonitor {

    private static final Logger log = LoggerFactory.getLogger(LagMonitor.class);
    private static final Duration DEFAULT_SLA_THRESHOLD = Duration.ofMinutes(5);
    private static final Duration DEFAULT_CHECK_INTERVAL = Duration.ofSeconds(30);

    private final ReconciliationServicePort reconciliationService;
    private final MeterRegistry meterRegistry;
    private final AtomicLong currentLagSeconds;
    private final AtomicReference<Instant> lastAlertTime;
    private final AtomicLong pendingCount;
    private final Timer lagCheckTimer;
    private final AtomicReference<Duration> slaThreshold;
    private final AtomicReference<Duration> checkInterval;
    private final java.util.List<Long> lagHistory = java.util.Collections.synchronizedList(new java.util.ArrayList<>());

    public LagMonitor(ReconciliationServicePort reconciliationService, MeterRegistry meterRegistry) {
        this.reconciliationService = reconciliationService;
        this.meterRegistry = meterRegistry;
        this.currentLagSeconds = new AtomicLong(0);
        this.lastAlertTime = new AtomicReference<>(Instant.EPOCH);
        this.pendingCount = new AtomicLong(0);
        this.slaThreshold = new AtomicReference<>(DEFAULT_SLA_THRESHOLD);
        this.checkInterval = new AtomicReference<>(DEFAULT_CHECK_INTERVAL);

        registerMetrics();
        this.lagCheckTimer = Timer.builder("reconciliation.lag.check")
                .description("Tiempo de verificación del lag de conciliación")
                .register(meterRegistry);
    }

    private void registerMetrics() {
        Gauge.builder("reconciliation.lag.seconds", currentLagSeconds, AtomicLong::get)
                .description("Lag actual de conciliación en segundos")
                .tag("sla_threshold", String.valueOf(slaThreshold.get().toSeconds()))
                .register(meterRegistry);

        Gauge.builder("reconciliation.pending.count", pendingCount, AtomicLong::get)
                .description("Cantidad de conciliaciones pendientes")
                .register(meterRegistry);

        Gauge.builder("reconciliation.lag.alert.active", this, LagMonitor::isAlertActive)
                .description("Indica si hay una alerta de lag activa")
                .register(meterRegistry);
    }

    public int isAlertActive() {
        return (currentLagSeconds.get() > slaThreshold.get().toSeconds()) ? 1 : 0;
    }

    public Mono<LagMeasurement> measureLag(Instant eventTime) {
        Duration sla = slaThreshold.get();
        long lagSeconds = Duration.between(eventTime, Instant.now()).getSeconds();
        boolean alertTriggered = lagSeconds > sla.toSeconds();
        
        return Mono.just(new LagMeasurement(lagSeconds, alertTriggered, eventTime));
    }

    public record LagMeasurement(long seconds, boolean alertTriggered, Instant eventTime) {}

    public void recordEvent(Instant eventTime) {
        long lagSeconds = Duration.between(eventTime, Instant.now()).getSeconds();
        lagHistory.add(lagSeconds);
        if (lagHistory.size() > 100) {
            lagHistory.remove(0);
        }
    }

    public Mono<Void> checkAndAlert() {
        return Timer.resource(lagCheckTimer)
                .flatMap(timer -> {
                    log.debug("Iniciando verificación de lag de conciliación");
                    return calculateCurrentLag()
                            .flatMap(this::updateMetricsAndCheckAlert)
                            .doOnSuccess(v -> log.debug("Verificación de lag completada, lag actual: {} segundos", 
                                    currentLagSeconds.get()))
                            .doOnError(e -> log.error("Error al verificar lag de conciliación", e));
                });
    }

    public Mono<Void> checkAndAlert(Instant eventTime) {
        return measureLag(eventTime)
                .flatMap(measurement -> {
                    if (measurement.alertTriggered()) {
                        return triggerAlert(measurement.seconds(), pendingCount.get());
                    }
                    return Mono.empty();
                });
    }

    private Mono<Long> calculateCurrentLag() {
        Duration interval = checkInterval.get();
        Instant threshold = Instant.now().minus(interval.multipliedBy(2));
        
        return reconciliationService.getReconciliationsByStatus(ReconciliationStatus.PENDING)
                .take(1)
                .switchIfEmpty(Flux.empty().then(Mono.just(Instant.now())))
                .flatMap(reconciliation -> {
                    Instant createdAt = reconciliation.getCreatedAt();
                    long lagSeconds = Duration.between(createdAt, Instant.now()).getSeconds();
                    return Mono.just(lagSeconds);
                })
                .defaultIfEmpty(0L)
                .single();
    }

    private Mono<Void> updateMetricsAndCheckAlert(Long lagSeconds) {
        currentLagSeconds.set(lagSeconds);
        
        return reconciliationService.getReconciliationsByStatus(ReconciliationStatus.PENDING)
                .count()
                .doOnNext(count -> pendingCount.set(count))
                .flatMap(count -> {
                    if (lagSeconds > slaThreshold.get().toSeconds()) {
                        return handleLagAlert(lagSeconds, count);
                    }
                    return Mono.empty();
                });
    }

    private Mono<Void> handleLagAlert(Long lagSeconds, Long pendingCount) {
        Instant now = Instant.now();
        Instant lastAlert = lastAlertTime.get();
        Duration interval = checkInterval.get();
        
        if (Duration.between(lastAlert, now).compareTo(interval) > 0) {
            if (lastAlertTime.compareAndSet(lastAlert, now)) {
                log.warn("ALERTA: Lag de conciliación excede el SLA de {} minutos. " +
                        "Lag actual: {} segundos, Movimientos pendientes: {}", 
                        slaThreshold.get().toMinutes(), lagSeconds, pendingCount);
                
                emitAlertMetric(lagSeconds, pendingCount);
                return Mono.empty();
            }
        }
        return Mono.empty();
    }

    private Mono<Void> triggerAlert(Long lagSeconds, Long pendingCount) {
        meterRegistry.counter("reconciliation.lag.alerts.triggered",
                "reason", "sla_exceeded")
                .increment();
        
        log.error("ALERTA - Lag: {}s, Pendientes: {}, Umbral SLA: {}s",
                lagSeconds, pendingCount, slaThreshold.get().toSeconds());
        
        return Mono.just(new AlertEvent(lagSeconds, "Lag SUPERO el umbral de " + slaThreshold.get().toMinutes() + " minutos"))
                .then();
    }

    public record AlertEvent(long lagSeconds, String message) {}

    private void emitAlertMetric(Long lagSeconds, Long pendingCount) {
        meterRegistry.counter("reconciliation.lag.alerts.triggered",
                "reason", "sla_exceeded")
                .increment();
        
        log.error("MÉTRICAS DE ALERTA - Lag: {}s, Pendientes: {}, Umbral SLA: {}s",
                lagSeconds, pendingCount, slaThreshold.get().toSeconds());
    }

    public Mono<Long> getCurrentLagSeconds() {
        return calculateCurrentLag();
    }

    public long getSlaThresholdSeconds() {
        return slaThreshold.get().toSeconds();
    }

    public boolean isLagExceedingSla() {
        return currentLagSeconds.get() > slaThreshold.get().toSeconds();
    }

    public Mono<Long> getPendingCount() {
        return reconciliationService.getReconciliationsByStatus(ReconciliationStatus.PENDING)
                .count();
    }

    public Flux<Long> startMonitoring() {
        return Flux.interval(checkInterval.get())
                .flatMap(tick -> calculateCurrentLag()
                        .doOnNext(lag -> {
                            currentLagSeconds.set(lag);
                            if (lag > slaThreshold.get().toSeconds()) {
                                handleLagAlert(lag, pendingCount.get()).block();
                            }
                        })
                );
    }

    public Mono<Double> getLagTrend() {
        return Mono.fromCallable(() -> {
            if (lagHistory.size() < 2) {
                return 0.0;
            }
            synchronized (lagHistory) {
                long first = lagHistory.get(0);
                long last = lagHistory.get(lagHistory.size() - 1);
                return (double) (last - first);
            }
        });
    }

    public void recordProcessingTime(Duration duration) {
        meterRegistry.timer("reconciliation.lag.measurement")
                .record(duration);
    }

    public String exportMetrics() {
        StringBuilder sb = new StringBuilder();
        sb.append("# TYPE reconciliation_lag gauge\n");
        sb.append("reconciliation_lag ").append(currentLagSeconds.get()).append("\n");
        sb.append("# TYPE reconciliation_pending gauge\n");
        sb.append("reconciliation_pending ").append(pendingCount.get()).append("\n");
        sb.append("# TYPE reconciliation_lag_alert gauge\n");
        sb.append("reconciliation_lag_alert ").append(isAlertActive()).append("\n");
        return sb.toString();
    }
}