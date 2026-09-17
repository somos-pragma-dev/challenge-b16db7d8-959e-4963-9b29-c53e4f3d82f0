package com.pragma.reconciliation.application.commands;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReconcileMovementCommand(
    UUID id,
    String accountId,
    String movementId,
    String eventId,
    Integer version,
    BigDecimal amount,
    String currency,
    Instant transactionDate,
    String source,
    String referenceId,
    String description,
    Instant receivedAt
) {
    public ReconcileMovementCommand {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("accountId no puede ser nulo o vacío");
        }
        if (movementId == null || movementId.isBlank()) {
            throw new IllegalArgumentException("movementId no puede ser nulo o vacío");
        }
        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("eventId no puede ser nulo o vacío");
        }
        if (version == null || version < 0) {
            throw new IllegalArgumentException("version debe ser mayor o igual a 0");
        }
        if (amount == null) {
            throw new IllegalArgumentException("amount no puede ser nulo");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("currency no puede ser nulo o vacío");
        }
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("source no puede ser nulo o vacío");
        }
    }

    public boolean isFromCoreBanking() {
        return "CORE_BANKING".equalsIgnoreCase(source);
    }

    public boolean isFromPaymentGateway() {
        return "PAYMENT_GATEWAY".equalsIgnoreCase(source);
    }

    public boolean isFromSettlementSystem() {
        return "SETTLEMENT_SYSTEM".equalsIgnoreCase(source);
    }

    public boolean isFromValidSource() {
        return isFromCoreBanking() || isFromPaymentGateway() || isFromSettlementSystem();
    }

    public String getUniqueEventKey() {
        return eventId + "|" + version;
    }
}