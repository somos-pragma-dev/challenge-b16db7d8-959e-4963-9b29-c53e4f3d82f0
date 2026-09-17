package com.pragma.reconciliation.application.events;

import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record MovementReconciledEvent(
    UUID id,
    String accountId,
    String movementId,
    String eventId,
    Integer version,
    BigDecimal amount,
    String currency,
    LocalDate transactionDate,
    ReconciliationStatus status,
    String source,
    Instant createdAt,
    Instant processedAt,
    String mismatchReason,
    boolean idempotencyProcessed
) {
    public static MovementReconciledEvent fromReconciliation(Reconciliation reconciliation) {
        return new MovementReconciledEvent(
            reconciliation.getId(),
            reconciliation.getAccountId(),
            reconciliation.getMovementId(),
            reconciliation.getEventId(),
            reconciliation.getVersion(),
            reconciliation.getAmount(),
            reconciliation.getCurrency(),
            reconciliation.getTransactionDate(),
            reconciliation.getStatus(),
            reconciliation.getSource(),
            reconciliation.getCreatedAt(),
            reconciliation.getProcessedAt(),
            reconciliation.getMismatchReason(),
            reconciliation.isIdempotencyProcessed()
        );
    }

    public boolean isSuccessful() {
        return status == ReconciliationStatus.MATCHED;
    }

    public boolean requiresManualIntervention() {
        return status == ReconciliationStatus.MANUAL;
    }

    public boolean hasMismatch() {
        return status == ReconciliationStatus.MISMATCHED;
    }

    public String getEventKey() {
        return eventId + "_v" + version;
    }

    public Instant getProcessingLatency() {
        if (createdAt != null && processedAt != null) {
            return processedAt.minusMillis(createdAt.toEpochMilli());
        }
        return Instant.EPOCH;
    }

    public long getLatencySeconds() {
        if (createdAt != null && processedAt != null) {
            return java.time.Duration.between(createdAt, processedAt).getSeconds();
        }
        return 0L;
    }
}