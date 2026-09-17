package com.pragma.reconciliation.infrastructure.persistence;

import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import com.pragma.reconciliation.domain.ports.out.ReconciliationPersistencePort;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Repository
public class ReconciliationJpaRepository implements ReconciliationPersistencePort {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationJpaRepository.class);

    private final EntityManager entityManager;
    private final ReconciliationSpringDataRepository springDataRepository;

    public ReconciliationJpaRepository(EntityManager entityManager,
                                        ReconciliationSpringDataRepository springDataRepository) {
        this.entityManager = entityManager;
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Mono<Reconciliation> save(Reconciliation reconciliation) {
        return Mono.fromCallable(() -> {
            ReconciliationEntity entity = toEntity(reconciliation);
            entityManager.persist(entity);
            entityManager.flush();
            log.debug("Entidad persistida: {}", entity.getId());
            return toDomain(entity);
        });
    }

    @Override
    public Mono<Reconciliation> findById(UUID id) {
        return Mono.fromCallable(() -> {
            ReconciliationEntity entity = entityManager.find(ReconciliationEntity.class, id);
            return entity != null ? toDomain(entity) : null;
        });
    }

    @Override
    public Flux<Reconciliation> findByAccountId(String accountId) {
        return Mono.fromCallable(() -> {
            TypedQuery<ReconciliationEntity> query = entityManager.createQuery(
                    "SELECT r FROM ReconciliationEntity r WHERE r.accountId = :accountId ORDER BY r.createdAt DESC",
                    ReconciliationEntity.class);
            query.setParameter("accountId", accountId);
            return query.getResultList();
        }).flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<Reconciliation> findByStatus(ReconciliationStatus status) {
        return Mono.fromCallable(() -> {
            TypedQuery<ReconciliationEntity> query = entityManager.createQuery(
                    "SELECT r FROM ReconciliationEntity r WHERE r.status = :status ORDER BY r.createdAt DESC",
                    ReconciliationEntity.class);
            query.setParameter("status", status.name());
            return query.getResultList();
        }).flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<Reconciliation> findByDateRange(Instant startDate, Instant endDate) {
        return Mono.fromCallable(() -> {
            TypedQuery<ReconciliationEntity> query = entityManager.createQuery(
                    "SELECT r FROM ReconciliationEntity r WHERE r.createdAt >= :startDate AND r.createdAt <= :endDate ORDER BY r.createdAt ASC",
                    ReconciliationEntity.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        }).flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Reconciliation> findByEventIdAndVersion(String eventId, Integer version) {
        return Mono.fromCallable(() -> {
            TypedQuery<ReconciliationEntity> query = entityManager.createQuery(
                    "SELECT r FROM ReconciliationEntity r WHERE r.eventId = :eventId AND r.version = :version",
                    ReconciliationEntity.class);
            query.setParameter("eventId", eventId);
            query.setParameter("version", version);
            query.setMaxResults(1);
            var results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        }).map(this::toDomain);
    }

    @Override
    public Mono<Boolean> existsByEventIdAndVersion(String eventId, Integer version) {
        return Mono.fromCallable(() -> {
            TypedQuery<ReconciliationEntity> query = entityManager.createQuery(
                    "SELECT r FROM ReconciliationEntity r WHERE r.eventId = :eventId AND r.version = :version",
                    ReconciliationEntity.class);
            query.setParameter("eventId", eventId);
            query.setParameter("version", version);
            query.setMaxResults(1);
            return !query.getResultList().isEmpty();
        });
    }

    @Override
    public Mono<Reconciliation> update(Reconciliation reconciliation) {
        return Mono.fromCallable(() -> {
            ReconciliationEntity entity = toEntity(reconciliation);
            ReconciliationEntity merged = entityManager.merge(entity);
            entityManager.flush();
            log.debug("Entidad actualizada: {}", merged.getId());
            return toDomain(merged);
        });
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return Mono.fromCallable(() -> {
            ReconciliationEntity entity = entityManager.find(ReconciliationEntity.class, id);
            if (entity != null) {
                entityManager.remove(entity);
                entityManager.flush();
                log.debug("Entidad eliminada: {}", id);
            }
            return null;
        }).then();
    }

    @Override
    public Flux<Reconciliation> findPendingOlderThan(Instant threshold) {
        return Mono.fromCallable(() -> {
            TypedQuery<ReconciliationEntity> query = entityManager.createQuery(
                    "SELECT r FROM ReconciliationEntity r WHERE r.status = :status AND r.createdAt < :threshold",
                    ReconciliationEntity.class);
            query.setParameter("status", ReconciliationStatus.PENDING.name());
            query.setParameter("threshold", threshold);
            return query.getResultList();
        }).flatMapMany(Flux::fromIterable).map(this::toDomain);
    }

    @Override
    public Flux<Reconciliation> findAll(int page, int size) {
        return Mono.fromCallable(() -> {
            TypedQuery<ReconciliationEntity> query = entityManager.createQuery(
                    "SELECT r FROM ReconciliationEntity r ORDER BY r.createdAt DESC",
                    ReconciliationEntity.class);
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            return query.getResultList();
        }).flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Long> countByStatus(ReconciliationStatus status) {
        return Mono.fromCallable(() -> {
            Query query = entityManager.createQuery(
                    "SELECT COUNT(r) FROM ReconciliationEntity r WHERE r.status = :status");
            query.setParameter("status", status.name());
            return (Long) query.getSingleResult();
        });
    }

    @Override
    public Mono<Long> count() {
        return Mono.fromCallable(() -> {
            Query query = entityManager.createQuery("SELECT COUNT(r) FROM ReconciliationEntity r");
            return (Long) query.getSingleResult();
        });
    }

    private ReconciliationEntity toEntity(Reconciliation domain) {
        ReconciliationEntity entity = new ReconciliationEntity();
        entity.setId(domain.getId());
        entity.setAccountId(domain.getAccountId());
        entity.setMovementId(domain.getMovementId());
        entity.setEventId(domain.getEventId());
        entity.setVersion(domain.getVersion());
        entity.setAmount(domain.getAmount());
        entity.setCurrency(domain.getCurrency());
        entity.setTransactionDate(domain.getTransactionDate());
        entity.setStatus(domain.getStatus().name());
        entity.setSource(domain.getSource());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setProcessedAt(domain.getProcessedAt());
        entity.setMismatchReason(domain.getMismatchReason());
        entity.setIdempotencyProcessed(domain.isIdempotencyProcessed());
        return entity;
    }

    private Reconciliation toDomain(ReconciliationEntity entity) {
        return new Reconciliation(
                entity.getId(),
                entity.getAccountId(),
                entity.getMovementId(),
                entity.getEventId(),
                entity.getVersion(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getTransactionDate(),
                ReconciliationStatus.valueOf(entity.getStatus()),
                entity.getSource(),
                entity.getCreatedAt(),
                entity.getProcessedAt(),
                entity.getMismatchReason(),
                entity.isIdempotencyProcessed()
        );
    }

    @Entity
    @Table(name = "reconciliations")
    private static class ReconciliationEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(name = "account_id", nullable = false)
        private String accountId;

        @Column(name = "movement_id", nullable = false)
        private String movementId;

        @Column(name = "event_id", nullable = false)
        private String eventId;

        @Column(nullable = false)
        private Integer version;

        @Column(nullable = false, precision = 19, scale = 4)
        private java.math.BigDecimal amount;

        @Column(nullable = false, length = 3)
        private String currency;

        @Column(name = "transaction_date")
        private java.time.LocalDate transactionDate;

        @Column(nullable = false)
        private String status;

        @Column(nullable = false)
        private String source;

        @Column(name = "created_at", nullable = false)
        private Instant createdAt;

        @Column(name = "processed_at")
        private Instant processedAt;

        @Column(name = "mismatch_reason", length = 1000)
        private String mismatchReason;

        @Column(name = "idempotency_processed")
        private boolean idempotencyProcessed;

        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getAccountId() { return accountId; }
        public void setAccountId(String accountId) { this.accountId = accountId; }
        public String getMovementId() { return movementId; }
        public void setMovementId(String movementId) { this.movementId = movementId; }
        public String getEventId() { return eventId; }
        public void setEventId(String eventId) { this.eventId = eventId; }
        public Integer getVersion() { return version; }
        public void setVersion(Integer version) { this.version = version; }
        public java.math.BigDecimal getAmount() { return amount; }
        public void setAmount(java.math.BigDecimal amount) { this.amount = amount; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public java.time.LocalDate getTransactionDate() { return transactionDate; }
        public void setTransactionDate(java.time.LocalDate transactionDate) { this.transactionDate = transactionDate; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        public Instant getCreatedAt() { return createdAt; }
        public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
        public Instant getProcessedAt() { return processedAt; }
        public void setProcessedAt(Instant processedAt) { this.processedAt = processedAt; }
        public String getMismatchReason() { return mismatchReason; }
        public void setMismatchReason(String mismatchReason) { this.mismatchReason = mismatchReason; }
        public boolean isIdempotencyProcessed() { return idempotencyProcessed; }
        public void setIdempotencyProcessed(boolean idempotencyProcessed) { this.idempotencyProcessed = idempotencyProcessed; }
    }

    private interface ReconciliationSpringDataRepository {
        // Interfaz placeholder para Spring Data JPA si se usa en el futuro
        // Por ahora usamos EntityManager directamente
    }
}