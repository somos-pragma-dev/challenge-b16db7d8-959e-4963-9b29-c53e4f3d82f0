package com.pragma.reconciliation.domain.services;

import com.pragma.reconciliation.application.commands.ReconcileMovementCommand;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import com.pragma.reconciliation.domain.ports.in.ReconciliationServicePort;
import com.pragma.reconciliation.domain.ports.out.ReconciliationPersistencePort;
import com.pragma.reconciliation.application.events.MovementReconciledEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ReconciliationService implements ReconciliationServicePort {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationService.class);
    private static final Duration DEFAULT_MATCHING_WINDOW = Duration.ofMinutes(5);
    private static final Duration OUT_OF_ORDER_THRESHOLD = Duration.ofMinutes(10);

    private final ReconciliationPersistencePort persistencePort;

    public ReconciliationService(ReconciliationPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Mono<Reconciliation> reconcileMovement(ReconcileMovementCommand command) {
        log.info("Iniciando reconciliación para eventId={}, version={}, source={}",
                command.eventId(), command.version(), command.source());

        if (!command.isFromValidSource()) {
            log.warn("Fuente de movimiento inválida: {}", command.source());
            return Mono.error(new IllegalArgumentException("Fuente de movimiento inválida: " + command.source()));
        }

        return persistencePort.existsByEventIdAndVersion(command.eventId(), command.version())
                .flatMap(exists -> {
                    if (exists) {
                        log.info("Movimiento duplicado detectado, eventId={}, version={}",
                                command.eventId(), command.version());
                        return findExistingAndMarkAsProcessed(command.eventId(), command.version());
                    }
                    return processNewMovement(command);
                })
                .doOnSuccess(reconciliation -> log.info("Reconciliación completada: id={}, status={}",
                        reconciliation.getId(), reconciliation.getStatus()))
                .doOnError(error -> log.error("Error en reconciliación: {}", error.getMessage()));
    }

    private Mono<Reconciliation> findExistingAndMarkAsProcessed(String eventId, Integer version) {
        return persistencePort.findByEventIdAndVersion(eventId, version)
                .flatMap(existing -> {
                    if (existing.isIdempotencyProcessed()) {
                        log.debug("Movimiento ya procesado completamente, retornando existente: {}", existing.getId());
                        return Mono.just(existing);
                    }
                    return Mono.just(existing.markAsProcessed());
                });
    }

    private Mono<Reconciliation> processNewMovement(ReconcileMovementCommand command) {
        Reconciliation reconciliation = createReconciliationFromCommand(command);

        return persistencePort.save(reconciliation)
                .flatMap(saved -> performMatching(saved)
                        .flatMap(matched -> {
                            if (matched.getStatus() == ReconciliationStatus.MISMATCHED) {
                                return persistencePort.update(matched);
                            }
                            return Mono.just(matched);
                        })
                        .flatMap(updated -> publishReconciliationEvent(updated)
                                .thenReturn(updated)));
    }

    private Reconciliation createReconciliationFromCommand(ReconcileMovementCommand command) {
        return new Reconciliation(
                command.id() != null ? command.id() : UUID.randomUUID(),
                command.accountId(),
                command.movementId(),
                command.eventId(),
                command.version(),
                command.amount(),
                command.currency(),
                command.transactionDate(),
                ReconciliationStatus.PENDING,
                command.source(),
                Instant.now(),
                null,
                null,
                false
        );
    }

    private Mono<Reconciliation> performMatching(Reconciliation reconciliation) {
        Instant transactionTime = reconciliation.getTransactionDate() != null
                ? reconciliation.getTransactionDate().atStartOfDay().toInstant(ZoneOffset.UTC)
                : Instant.now();

        Instant windowStart = transactionTime.minus(DEFAULT_MATCHING_WINDOW);
        Instant windowEnd = transactionTime.plus(DEFAULT_MATCHING_WINDOW);

        log.debug("Ventana de matching para {}: {} a {}",
                reconciliation.getMovementId(), windowStart, windowEnd);

        return persistencePort.findByDateRange(windowStart, windowEnd)
                .filter(r -> r.getAccountId().equals(reconciliation.getAccountId()))
                .filter(r -> !r.getId().equals(reconciliation.getId()))
                .collectList()
                .flatMap(candidates -> {
                    if (candidates.isEmpty()) {
                        log.info("No se encontraron candidatos para matching, movimiento queda pendiente: {}",
                                reconciliation.getMovementId());
                        return Mono.just(reconciliation);
                    }
                    return findMatchingCandidate(reconciliation, candidates);
                });
    }

    private Mono<Reconciliation> findMatchingCandidate(Reconciliation reconciliation, List<Reconciliation> candidates) {
        BigDecimal tolerance = reconciliation.getAmount().multiply(BigDecimal.valueOf(0.01));

        return Flux.fromIterable(candidates)
                .filter(candidate -> hasMatchingSource(candidate, reconciliation))
                .filter(candidate -> isAmountWithinTolerance(reconciliation.getAmount(), candidate.getAmount(), tolerance))
                .next()
                .flatMap(matched -> {
                    log.info("Match encontrado para {} con {}", reconciliation.getMovementId(), matched.getMovementId());
                    return Mono.just(reconciliation.withStatus(ReconciliationStatus.MATCHED));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No se encontró match para {}, marcando como discrepancia", reconciliation.getMovementId());
                    String reason = String.format("No se encontró movimiento coincidente en ventana de %d minutos. Candidatos encontrados: %d",
                            DEFAULT_MATCHING_WINDOW.toMinutes(), candidates.size());
                    return Mono.just(reconciliation.withStatus(ReconciliationStatus.MISMATCHED)
                            .withMismatchReason(reason));
                }));
    }

    private boolean hasMatchingSource(Reconciliation existing, Reconciliation incoming) {
        return !existing.getSource().equals(incoming.getSource());
    }

    private boolean isAmountWithinTolerance(BigDecimal amount1, BigDecimal amount2, BigDecimal tolerance) {
        BigDecimal difference = amount1.subtract(amount2).abs();
        return difference.compareTo(tolerance) <= 0;
    }

    private Mono<Void> publishReconciliationEvent(Reconciliation reconciliation) {
        MovementReconciledEvent event = new MovementReconciledEvent(
                reconciliation.getId(),
                reconciliation.getAccountId(),
                reconciliation.getMovementId(),
                reconciliation.getEventId(),
                reconciliation.getVersion(),
                reconciliation.getAmount(),
                reconciliation.getCurrency(),
                reconciliation.getStatus(),
                reconciliation.getProcessedAt(),
                reconciliation.getMismatchReason()
        );
        log.info("Evento de reconciliación publicado: {}", event);
        return Mono.empty();
    }

    @Override
    public Mono<Reconciliation> getReconciliationById(UUID id) {
        return persistencePort.findById(id);
    }

    @Override
    public Flux<Reconciliation> getReconciliationsByAccountId(String accountId) {
        return persistencePort.findByAccountId(accountId);
    }

    @Override
    public Flux<Reconciliation> getReconciliationsByStatus(ReconciliationStatus status) {
        return persistencePort.findByStatus(status);
    }

    @Override
    public Flux<Reconciliation> getReconciliationsByDateRange(Instant startDate, Instant endDate) {
        return persistencePort.findByDateRange(startDate, endDate);
    }

    @Override
    public Mono<Reconciliation> updateReconciliationStatus(UUID id, ReconciliationStatus newStatus) {
        return persistencePort.findById(id)
                .flatMap(reconciliation -> {
                    if (!reconciliation.getStatus().canTransitionTo(newStatus)) {
                        return Mono.error(new IllegalStateException(
                                "Transición de estado inválida: " + reconciliation.getStatus() + " -> " + newStatus));
                    }
                    Reconciliation updated = reconciliation.withStatus(newStatus).markAsProcessed();
                    return persistencePort.update(updated);
                });
    }

    @Override
    public Mono<Reconciliation> markAsManualReview(UUID id, String reason) {
        return persistencePort.findById(id)
                .flatMap(reconciliation -> {
                    Reconciliation updated = reconciliation.withStatus(ReconciliationStatus.MANUAL)
                            .withMismatchReason(reason)
                            .markAsProcessed();
                    return persistencePort.update(updated);
                });
    }

    @Override
    public Flux<MovementReconciledEvent> processReconciliationBatch(List<ReconcileMovementCommand> commands) {
        log.info("Procesando lote de {} movimientos", commands.size());

        return Flux.fromIterable(commands)
                .flatMap(this::reconcileMovement)
                .map(this::toMovementReconciledEvent);
    }

    private MovementReconciledEvent toMovementReconciledEvent(Reconciliation reconciliation) {
        return new MovementReconciledEvent(
                reconciliation.getId(),
                reconciliation.getAccountId(),
                reconciliation.getMovementId(),
                reconciliation.getEventId(),
                reconciliation.getVersion(),
                reconciliation.getAmount(),
                reconciliation.getCurrency(),
                reconciliation.getStatus(),
                reconciliation.getProcessedAt(),
                reconciliation.getMismatchReason()
        );
    }

    @Override
    public Mono<Boolean> isMovementProcessed(String eventId, Integer version) {
        return persistencePort.existsByEventIdAndVersion(eventId, version);
    }

    @Override
    public Mono<Reconciliation> reprocessReconciliation(UUID id) {
        return persistencePort.findById(id)
                .flatMap(reconciliation -> {
                    if (reconciliation.getStatus() == ReconciliationStatus.MATCHED) {
                        log.info("Reconciliación {} ya está conciliada, no se reprocesa", id);
                        return Mono.just(reconciliation);
                    }
                    Reconciliation reset = reconciliation.withStatus(ReconciliationStatus.PENDING);
                    return persistencePort.update(reset)
                            .flatMap(this::performMatching)
                            .flatMap(matched -> persistencePort.update(matched));
                });
    }

    @Override
    public Flux<Reconciliation> getPendingReconciliationsOlderThan(Instant threshold) {
        return persistencePort.findPendingOlderThan(threshold);
    }

    private static class ZoneOffset {
        static java.time.ZoneOffset UTC = java.time.ZoneOffset.UTC;
    }
}