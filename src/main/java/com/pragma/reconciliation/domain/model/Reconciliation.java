package com.pragma.reconciliation.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Reconciliation {
    private final UUID id;
    private final String accountId;
    private final String movementId;
    private final String eventId;
    private final Integer version;
    private final BigDecimal amount;
    private final String currency;
    private final LocalDate transactionDate;
    private final ReconciliationStatus status;
    private final String source;
    private final Instant createdAt;
    private final Instant processedAt;
    private final String mismatchReason;
    private final boolean idempotencyProcessed;

    public Reconciliation(
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
            boolean idempotencyProcessed) {
        
        this.id = Objects.requireNonNull(id, "El ID de reconciliación no puede ser nulo");
        this.accountId = validateAccountId(accountId);
        this.movementId = validateMovementId(movementId);
        this.eventId = validateEventId(eventId);
        this.version = validateVersion(version);
        this.amount = validateAmount(amount);
        this.currency = validateCurrency(currency);
        this.transactionDate = Objects.requireNonNull(transactionDate, "La fecha de transacción no puede ser nula");
        this.status = Objects.requireNonNull(status, "El estado de reconciliación no puede ser nulo");
        this.source = validateSource(source);
        this.createdAt = Objects.requireNonNull(createdAt, "La fecha de creación no puede ser nula");
        this.processedAt = processedAt;
        this.mismatchReason = mismatchReason;
        this.idempotencyProcessed = idempotencyProcessed;
    }

    private String validateAccountId(String accountId) {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("El ID de cuenta no puede ser nulo o vacío");
        }
        if (accountId.length() > 50) {
            throw new IllegalArgumentException("El ID de cuenta no puede exceder 50 caracteres");
        }
        return accountId;
    }

    private String validateMovementId(String movementId) {
        if (movementId == null || movementId.isBlank()) {
            throw new IllegalArgumentException("El ID de movimiento no puede ser nulo o vacío");
        }
        return movementId;
    }

    private String validateEventId(String eventId) {
        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("El eventId no puede ser nulo o vacío");
        }
        return eventId;
    }

    private Integer validateVersion(Integer version) {
        if (version == null || version < 0) {
            throw new IllegalArgumentException("La versión debe ser mayor o igual a 0");
        }
        return version;
    }

    private BigDecimal validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
        return amount.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private String validateCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("La moneda no puede ser nula o vacía");
        }
        if (!currency.matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException("La moneda debe ser un código ISO de 3 letras mayúsculas");
        }
        return currency;
    }

    private String validateSource(String source) {
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("La fuente del movimiento no puede ser nula o vacía");
        }
        return source;
    }

    public UUID getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getMovementId() {
        return movementId;
    }

    public String getEventId() {
        return eventId;
    }

    public Integer getVersion() {
        return version;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public ReconciliationStatus getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public String getMismatchReason() {
        return mismatchReason;
    }

    public boolean isIdempotencyProcessed() {
        return idempotencyProcessed;
    }

    public boolean matches(Reconciliation other) {
        if (other == null) return false;
        return this.accountId.equals(other.accountId)
            && this.amount.compareTo(other.amount) == 0
            && this.currency.equals(other.currency)
            && this.transactionDate.equals(other.transactionDate);
    }

    public boolean isDuplicateOf(Reconciliation other) {
        if (other == null) return false;
        return this.eventId.equals(other.eventId) 
            && this.version.equals(other.version)
            && this.movementId.equals(other.movementId);
    }

    public boolean isOutOfOrder(Instant currentProcessingTime) {
        if (createdAt == null || currentProcessingTime == null) return false;
        long minutesDifference = java.time.Duration.between(createdAt, currentProcessingTime).toMinutes();
        return minutesDifference > 5;
    }

    public Reconciliation withStatus(ReconciliationStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                "Transición de estado inválida: " + this.status + " -> " + newStatus
            );
        }
        return new Reconciliation(
            this.id, this.accountId, this.movementId, this.eventId, this.version,
            this.amount, this.currency, this.transactionDate, newStatus, this.source,
            this.createdAt, Instant.now(), this.mismatchReason, this.idempotencyProcessed
        );
    }

    public Reconciliation withMismatchReason(String reason) {
        return new Reconciliation(
            this.id, this.accountId, this.movementId, this.eventId, this.version,
            this.amount, this.currency, this.transactionDate, 
            this.status == PENDING ? MISMATCHED : this.status, 
            this.source, this.createdAt, Instant.now(), reason, this.idempotencyProcessed
        );
    }

    public Reconciliation markAsProcessed() {
        return new Reconciliation(
            this.id, this.accountId, this.movementId, this.eventId, this.version,
            this.amount, this.currency, this.transactionDate, this.status,
            this.source, this.createdAt, Instant.now(), this.mismatchReason, true
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reconciliation that = (Reconciliation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reconciliation{" +
                "id=" + id +
                ", accountId='" + accountId + '\'' +
                ", movementId='" + movementId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", version=" + version +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", transactionDate=" + transactionDate +
                ", status=" + status +
                ", source='" + source + '\'' +
                ", createdAt=" + createdAt +
                ", processedAt=" + processedAt +
                ", mismatchReason='" + mismatchReason + '\'' +
                ", idempotencyProcessed=" + idempotencyProcessed +
                '}';
    }
}