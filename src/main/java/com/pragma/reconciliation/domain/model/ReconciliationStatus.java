package com.pragma.reconciliation.domain.model;

public enum ReconciliationStatus {
    PENDING("Pendiente de procesamiento"),
    MATCHED("Movimiento conciliado exitosamente"),
    MISMATCHED("Discrepancia detectada entre fuentes"),
    MANUAL("Requiere revisión manual del equipo de operaciones");

    private final String description;

    ReconciliationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canTransitionTo(ReconciliationStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == MATCHED || newStatus == MISMATCHED || newStatus == MANUAL;
            case MATCHED -> false;
            case MISMATCHED -> newStatus == MANUAL || newStatus == MATCHED;
            case MANUAL -> false;
        };
    }

    public boolean requiresManualIntervention() {
        return this == MANUAL || this == MISMATCHED;
    }

    public boolean isTerminal() {
        return this == MATCHED || this == MANUAL;
    }

    public boolean requiresProcessing() {
        return this == PENDING;
    }

    public static ReconciliationStatus fromString(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("El estado no puede ser nulo o vacío");
        }
        try {
            return valueOf(status.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Estado de reconciliación inválido: " + status + ". Estados válidos: " + 
                java.util.Arrays.toString(values())
            );
        }
    }
}