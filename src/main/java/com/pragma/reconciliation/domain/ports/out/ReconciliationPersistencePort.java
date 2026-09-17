package com.pragma.reconciliation.domain.ports.out;

import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

public interface ReconciliationPersistencePort {

    Mono<Reconciliation> save(Reconciliation reconciliation);

    Mono<Reconciliation> findById(UUID id);

    Flux<Reconciliation> findByAccountId(String accountId);

    Flux<Reconciliation> findByStatus(ReconciliationStatus status);

    Flux<Reconciliation> findByDateRange(Instant startDate, Instant endDate);

    Mono<Reconciliation> findByEventIdAndVersion(String eventId, Integer version);

    Mono<Boolean> existsByEventIdAndVersion(String eventId, Integer version);

    Mono<Reconciliation> update(Reconciliation reconciliation);

    Mono<Void> deleteById(UUID id);

    Flux<Reconciliation> findPendingOlderThan(Instant threshold);

    Flux<Reconciliation> findAll(int page, int size);

    Mono<Long> countByStatus(ReconciliationStatus status);

    Mono<Long> count();
}