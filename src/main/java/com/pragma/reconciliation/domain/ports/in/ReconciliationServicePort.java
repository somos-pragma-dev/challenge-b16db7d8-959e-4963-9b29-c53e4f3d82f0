package com.pragma.reconciliation.domain.ports.in;

import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import com.pragma.reconciliation.application.commands.ReconcileMovementCommand;
import com.pragma.reconciliation.application.events.MovementReconciledEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReconciliationServicePort {

    Mono<Reconciliation> reconcileMovement(ReconcileMovementCommand command);

    Mono<Reconciliation> getReconciliationById(UUID id);

    Flux<Reconciliation> getReconciliationsByAccountId(String accountId);

    Flux<Reconciliation> getReconciliationsByStatus(ReconciliationStatus status);

    Flux<Reconciliation> getReconciliationsByDateRange(Instant startDate, Instant endDate);

    Mono<Reconciliation> updateReconciliationStatus(UUID id, ReconciliationStatus newStatus);

    Mono<Reconciliation> markAsManualReview(UUID id, String reason);

    Flux<MovementReconciledEvent> processReconciliationBatch(List<ReconcileMovementCommand> commands);

    Mono<Boolean> isMovementProcessed(String eventId, Integer version);

    Mono<Reconciliation> reprocessReconciliation(UUID id);

    Flux<Reconciliation> getPendingReconciliationsOlderThan(Instant threshold);
}