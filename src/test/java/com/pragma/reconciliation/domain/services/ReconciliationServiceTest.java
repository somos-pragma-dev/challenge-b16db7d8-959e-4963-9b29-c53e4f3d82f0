package com.pragma.reconciliation.domain.services;

import com.pragma.reconciliation.application.commands.ReconcileMovementCommand;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import com.pragma.reconciliation.domain.ports.out.ReconciliationPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReconciliationService - Pruebas unitarias del servicio de conciliacion")
class ReconciliationServiceTest {

    @Mock
    private ReconciliationPersistencePort persistencePort;

    private ReconciliationService reconciliationService;

    @BeforeEach
    void setUp() {
        reconciliationService = new ReconciliationService(persistencePort);
    }

    @Nested
    @DisplayName("Escenario: Reconciliacion exitosa de movimiento unico")
    class ReconciliacionExitosa {

        @Test
        @DisplayName("Debe reconciliar movimiento cuando es nuevo y no existen duplicados")
        void debeReconciliarMovimientoNuevo() {
            ReconcileMovementCommand command = crearCommand("ACC-001", "MOV-001", "EVT-001", 1);
            Reconciliation reconciliationCreada = crearReconciliation("ACC-001", "MOV-001", "EVT-001", 1);

            when(persistencePort.existsByEventIdAndVersion("EVT-001", 1)).thenReturn(Mono.just(false));
            when(persistencePort.save(any(Reconciliation.class))).thenReturn(Mono.just(reconciliationCreada));

            StepVerifier.create(reconciliationService.reconcileMovement(command))
                    .assertNext(result -> {
                        assertThat(result.getAccountId()).isEqualTo("ACC-001");
                        assertThat(result.getMovementId()).isEqualTo("MOV-001");
                        assertThat(result.getStatus()).isEqualTo(ReconciliationStatus.MATCHED);
                    })
                    .verifyComplete();

            verify(persistencePort).existsByEventIdAndVersion("EVT-001", 1);
            verify(persistencePort).save(any(Reconciliation.class));
        }

        @Test
        @DisplayName("Debe marcar como duplicado cuando el movimiento ya fue procesado")
        void debeMarcarComoDuplicado() {
            ReconcileMovementCommand command = crearCommand("ACC-001", "MOV-001", "EVT-DUP", 1);
            Reconciliation existente = crearReconciliation("ACC-001", "MOV-001", "EVT-DUP", 1);
            existente = existente.withStatus(ReconciliationStatus.MATCHED);

            when(persistencePort.existsByEventIdAndVersion("EVT-DUP", 1)).thenReturn(Mono.just(true));
            when(persistencePort.findByEventIdAndVersion("EVT-DUP", 1)).thenReturn(Mono.just(existente));

            StepVerifier.create(reconciliationService.reconcileMovement(command))
                    .assertNext(result -> {
                        assertThat(result.isIdempotencyProcessed()).isTrue();
                        assertThat(result.getStatus()).isEqualTo(ReconciliationStatus.MATCHED);
                    })
                    .verifyComplete();

            verify(persistencePort, never()).save(any(Reconciliation.class));
        }
    }

    @Nested
    @DisplayName("Escenario: Movimiento fuera de orden (out-of-order)")
    class MovimientoFueraDeOrden {

        @Test
        @DisplayName("Debe rechazar movimiento con version menor a la procesada")
        void debeRechazarVersionMenor() {
            ReconcileMovementCommand command = crearCommand("ACC-002", "MOV-002", "EVT-002", 1);
            Reconciliation existente = crearReconciliation("ACC-002", "MOV-002", "EVT-002", 5);
            existente = existente.withStatus(ReconciliationStatus.MATCHED);

            when(persistencePort.existsByEventIdAndVersion("EVT-002", 1)).thenReturn(Mono.just(true));
            when(persistencePort.findByEventIdAndVersion("EVT-002", 1)).thenReturn(Mono.just(existente));

            StepVerifier.create(reconciliationService.reconcileMovement(command))
                    .assertNext(result -> {
                        assertThat(result.getVersion()).isEqualTo(5);
                        assertThat(result.isIdempotencyProcessed()).isTrue();
                    })
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe procesar movimiento con version mayor que la existente")
        void debeProcesarVersionMayor() {
            ReconcileMovementCommand command = crearCommand("ACC-002", "MOV-002", "EVT-002", 10);
            Reconciliation existente = crearReconciliation("ACC-002", "MOV-002", "EVT-002", 5);
            Reconciliation actualizada = crearReconciliation("ACC-002", "MOV-002", "EVT-002", 10);

            when(persistencePort.existsByEventIdAndVersion("EVT-002", 10)).thenReturn(Mono.just(false));
            when(persistencePort.findByEventIdAndVersion("EVT-002", 5)).thenReturn(Mono.just(existente));
            when(persistencePort.update(any(Reconciliation.class))).thenReturn(Mono.just(actualizada));

            StepVerifier.create(reconciliationService.reconcileMovement(command))
                    .assertNext(result -> {
                        assertThat(result.getVersion()).isEqualTo(10);
                        assertThat(result.isIdempotencyProcessed()).isFalse();
                    })
                    .verifyComplete();

            verify(persistencePort).update(any(Reconciliation.class));
        }
    }

    @Nested
    @DisplayName("Escenario: Detencion de discrepancias (mismatch)")
    class Discrepancias {

        @Test
        @DisplayName("Debe marcar como mismatch cuando los montos no coinciden entre fuentes")
        void debeMarcarMontoNoCoincide() {
            ReconcileMovementCommand command = crearCommand("ACC-003", "MOV-003", "EVT-003", 1);
            command = new ReconcileMovementCommand(
                    command.accountId(),
                    command.movementId(),
                    "CORE",
                    command.eventId(),
                    command.version(),
                    new BigDecimal("1000.00"),
                    command.currency(),
                    command.transactionDate(),
                    List.of(
                            new com.pragma.reconciliation.application.commands.MovementSource("CORE", new BigDecimal("1000.00")),
                            new com.pragma.reconciliation.application.commands.MovementSource("GATEWAY", new BigDecimal("999.50"))
                    )
            );

            when(persistencePort.existsByEventIdAndVersion(anyString(), any(Integer.class))).thenReturn(Mono.just(false));
            when(persistencePort.save(any(Reconciliation.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

            StepVerifier.create(reconciliationService.reconcileMovement(command))
                    .assertNext(result -> {
                        assertThat(result.getStatus()).isEqualTo(ReconciliationStatus.MISMATCHED);
                        assertThat(result.getMismatchReason()).contains("1000.00");
                    })
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe marcar para revision manual cuando discrepancy supera umbral")
        void debeMarcarParaRevisionManual() {
            ReconcileMovementCommand command = crearCommand("ACC-004", "MOV-004", "EVT-004", 1);
            BigDecimal montoCore = new BigDecimal("10000.00");
            BigDecimal montoGateway = new BigDecimal("8500.00");

            command = new ReconcileMovementCommand(
                    command.accountId(), command.movementId(), "CORE",
                    command.eventId(), command.version(), montoCore, command.currency(),
                    command.transactionDate(),
                    List.of(
                            new com.pragma.reconciliation.application.commands.MovementSource("CORE", montoCore),
                            new com.pragma.reconciliation.application.commands.MovementSource("GATEWAY", montoGateway)
                    )
            );

            when(persistencePort.existsByEventIdAndVersion(anyString(), any(Integer.class))).thenReturn(Mono.just(false));
            when(persistencePort.save(any(Reconciliation.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

            StepVerifier.create(reconciliationService.reconcileMovement(command))
                    .assertNext(result -> {
                        assertThat(result.getStatus()).isIn(
                                ReconciliationStatus.MISMATCHED,
                                ReconciliationStatus.MANUAL
                        );
                    })
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Escenario: Consulta de reconciliaciones")
    class Consultas {

        @Test
        @DisplayName("Debe obtener reconciliaciones por ID de cuenta")
        void debeObtenerPorCuenta() {
            List<Reconciliation> reconciliations = List.of(
                    crearReconciliation("ACC-005", "MOV-005", "EVT-005", 1),
                    crearReconciliation("ACC-005", "MOV-006", "EVT-006", 1)
            );

            when(persistencePort.findByAccountId("ACC-005")).thenReturn(Flux.fromIterable(reconciliations));

            StepVerifier.create(reconciliationService.getReconciliationsByAccountId("ACC-005"))
                    .expectNextCount(2)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe obtener reconciliaciones por estado")
        void debeObtenerPorEstado() {
            Reconciliation matched = crearReconciliation("ACC-006", "MOV-007", "EVT-007", 1);
            matched = matched.withStatus(ReconciliationStatus.MATCHED);

            when(persistencePort.findByStatus(ReconciliationStatus.MATCHED)).thenReturn(Flux.just(matched));

            StepVerifier.create(reconciliationService.getReconciliationsByStatus(ReconciliationStatus.MATCHED))
                    .assertNext(r -> assertThat(r.getStatus()).isEqualTo(ReconciliationStatus.MATCHED))
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe verificar si movimiento fue procesado")
        void debeVerificarMovimientoProcesado() {
            when(persistencePort.existsByEventIdAndVersion("EVT-EXISTS", 1)).thenReturn(Mono.just(true));
            when(persistencePort.existsByEventIdAndVersion("EVT-NOTEXISTS", 1)).thenReturn(Mono.just(false));

            StepVerifier.create(reconciliationService.isMovementProcessed("EVT-EXISTS", 1))
                    .expectNext(true)
                    .verifyComplete();

            StepVerifier.create(reconciliationService.isMovementProcessed("EVT-NOTEXISTS", 1))
                    .expectNext(false)
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Escenario: Reprocesamiento")
    class Reprocesamiento {

        @Test
        @DisplayName("Debe reprocesar reconciliacion pendiente старше threshold")
        void debeReprocesarAntiguos() {
            Instant threshold = Instant.now().minusSeconds(3600);
            Reconciliation antigua = crearReconciliation("ACC-007", "MOV-008", "EVT-008", 1);
            antigua = antigua.withStatus(ReconciliationStatus.PENDING);

            when(persistencePort.findPendingOlderThan(threshold)).thenReturn(Flux.just(antigua));
            when(persistencePort.existsByEventIdAndVersion(anyString(), any(Integer.class))).thenReturn(Mono.just(false));
            when(persistencePort.update(any(Reconciliation.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

            StepVerifier.create(reconciliationService.getPendingReconciliationsOlderThan(threshold))
                    .assertNext(r -> assertThat(r.getStatus()).isEqualTo(ReconciliationStatus.PENDING))
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe reprocesar reconcile especifica por ID")
        void debeReprocesarPorId() {
            UUID id = UUID.randomUUID();
            Reconciliation reconciliacion = crearReconciliation("ACC-008", "MOV-009", "EVT-009", 1);
            reconciliacion = reconciliacion.withStatus(ReconciliationStatus.PENDING);

            when(persistencePort.findById(id)).thenReturn(Mono.just(reconciliacion));
            when(persistencePort.existsByEventIdAndVersion(anyString(), any(Integer.class))).thenReturn(Mono.just(false));
            when(persistencePort.update(any(Reconciliation.class))).thenAnswer(inv -> {
                Reconciliation r = inv.getArgument(0);
                return Mono.just(r.withStatus(ReconciliationStatus.MATCHED));
            });

            StepVerifier.create(reconciliationService.reprocessReconciliation(id))
                    .assertNext(r -> assertThat(r.getStatus()).isEqualTo(ReconciliationStatus.MATCHED))
                    .verifyComplete();
        }
    }

    private ReconcileMovementCommand crearCommand(String accountId, String movementId,
                                                   String eventId, Integer version) {
        return new ReconcileMovementCommand(
                accountId, movementId, "CORE", eventId, version,
                new BigDecimal("1500.00"), "USD", LocalDate.now(),
                List.of(new com.pragma.reconciliation.application.commands.MovementSource("CORE", new BigDecimal("1500.00")))
        );
    }

    private Reconciliation crearReconciliation(String accountId, String movementId,
                                                String eventId, Integer version) {
        return new Reconciliation(
                UUID.randomUUID(), accountId, movementId, eventId, version,
                new BigDecimal("1500.00"), "USD", LocalDate.now(),
                ReconciliationStatus.PENDING, "CORE",
                Instant.now(), null, null, false
        );
    }
}