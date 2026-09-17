package com.pragma.reconciliation.domain.services;

import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import com.pragma.reconciliation.domain.ports.in.ReconciliationServicePort;
import com.pragma.reconciliation.domain.ports.out.ReconciliationPersistencePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Reprocesamiento {
    private static final Logger log = LoggerFactory.getLogger(Reprocesamiento.class);
    private static final int MAX_REPROCESS_ATTEMPTS = 3;
    private static final long RETRY_DELAY_SECONDS = 60;

    private final ReconciliationServicePort reconciliationService;
    private final ReconciliationPersistencePort persistencePort;

    public Reprocesamiento(
            ReconciliationServicePort reconciliationService,
            ReconciliationPersistencePort persistencePort
    ) {
        this.reconciliationService = reconciliationService;
        this.persistencePort = persistencePort;
    }

    public Mono<Reconciliation> reprocess(UUID reconciliationId) {
        log.info("Iniciando reprocesamiento para id={}", reconciliationId);
        
        return persistencePort.findById(reconciliationId)
                .flatMap(this::executeReprocess)
                .switchIfEmpty(Mono.error(
                    new IllegalArgumentException("Reconciliation no encontrada: " + reconciliationId)
                ));
    }

    private Mono<Reconciliation> executeReprocess(Reconciliation reconciliation) {
        if (reconciliation.getStatus() == ReconciliationStatus.MATCHED) {
            log.info("Reconciliation ya conciliada, omitiendo reprocesamiento: {}", reconciliation.getId());
            return Mono.just(reconciliation);
        }

        String eventId = reconciliation.getEventId();
        Integer version = reconciliation.getVersion();

        return persistencePort.existsByEventIdAndVersion(eventId, version)
                .flatMap(exists -> {
                    if (exists) {
                        return persistencePort.findByEventIdAndVersion(eventId, version)
                                .flatMap(existing -> {
                                    if (existing.isIdempotencyProcessed()) {
                                        log.info("Movimiento ya procesado (idempotente): eventId={}, version={}",
                                                eventId, version);
                                        return Mono.just(existing);
                                    }
                                    return performReprocess(existing);
                                });
                    }
                    return performReprocess(reconciliation);
                });
    }

    private Mono<Reconciliation> performReprocess(Reconciliation reconciliation) {
        log.info("Ejecutando reprocesamiento para eventId={}, version={}",
                reconciliation.getEventId(), reconciliation.getVersion());

        return reconciliationService.reconcileMovement(
                new com.pragma.reconciliation.application.commands.ReconcileMovementCommand(
                        reconciliation.getAccountId(),
                        reconciliation.getMovementId(),
                        reconciliation.getSource(),
                        reconciliation.getEventId(),
                        reconciliation.getVersion(),
                        reconciliation.getAmount(),
                        reconciliation.getCurrency(),
                        reconciliation.getTransactionDate(),
                        java.util.List.of()
                )
        );
    }

    public Flux<Reconciliation> reprocessOlderThan(Instant threshold) {
        log.info("Buscando reconciliations pendientes mayores a: {}", threshold);
        
        return persistencePort.findPendingOlderThan(threshold)
                .flatMap(reconciliation -> 
                    executeReprocess(reconciliation)
                        .onErrorResume(error -> {
                            log.error("Error en reprocesamiento de {}: {}", 
                                    reconciliation.getId(), error.getMessage());
                            return Flux.empty();
                        }),
                    10
                );
    }

    public Mono<Reconciliation> reprocessFromOffset(long offset) {
        Instant threshold = Instant.now().minus(RETRY_DELAY_SECONDS * offset, ChronoUnit.SECONDS);
        return reprocessOlderThan(threshold)
                .take(1)
                .singleOrEmpty();
    }
}