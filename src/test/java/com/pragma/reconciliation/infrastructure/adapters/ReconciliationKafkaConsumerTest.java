package com.pragma.reconciliation.infrastructure.adapters;

import com.pragma.reconciliation.application.commands.ReconcileMovementCommand;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import com.pragma.reconciliation.domain.ports.in.ReconciliationServicePort;
import com.pragma.reconciliation.domain.services.Reprocesamiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReconciliationKafkaConsumer - Pruebas del consumidor de eventos Kafka")
class ReconciliationKafkaConsumerTest {

    @Mock
    private ReconciliationServicePort reconciliationServicePort;

    @Mock
    private Reprocesamiento reprocesamiento;

    @Mock
    private Acknowledgment acknowledgment;

    private ReconciliationKafkaConsumer kafkaConsumer;

    @BeforeEach
    void setUp() {
        kafkaConsumer = new ReconciliationKafkaConsumer(reconciliationServicePort, reprocesamiento);
    }

    @Nested
    @DisplayName("Escenario: Consumo exitoso de mensajes")
    class ConsumoExitoso {

        @Test
        @DisplayName("Debe procesar mensaje y confirmar automaticamente")
        void debeProcesarYConfirmar() {
            ReconcileMovementCommand command = crearCommand("ACC-KAFKA-001", "MOV-K-001", "EVT-K-001", 1);
            Reconciliation resultado = crearReconciliation("ACC-KAFKA-001", "MOV-K-001", "EVT-K-001", 1);
            resultado = resultado.withStatus(ReconciliationStatus.MATCHED);

            when(reconciliationServicePort.reconcileMovement(any(ReconcileMovementCommand.class)))
                    .thenReturn(Mono.just(resultado));

            StepVerifier.create(kafkaConsumer.consume(command))
                    .assertNext(r -> assertThat(r.getStatus()).isEqualTo(ReconciliationStatus.MATCHED))
                    .verifyComplete();

            verify(reconciliationServicePort).reconcileMovement(command);
        }

        @Test
        @DisplayName("Debe procesar batch de mensajes correctamente")
        void debeProcesarBatch() {
            List<ReconcileMovementCommand> commands = List.of(
                    crearCommand("ACC-KAFKA-002", "MOV-K-002", "EVT-K-002", 1),
                    crearCommand("ACC-KAFKA-003", "MOV-K-003", "EVT-K-003", 1),
                    crearCommand("ACC-KAFKA-004", "MOV-K-004", "EVT-K-004", 1)
            );

            Reconciliation resultado1 = crearReconciliation("ACC-KAFKA-002", "MOV-K-002", "EVT-K-002", 1);
            Reconciliation resultado2 = crearReconciliation("ACC-KAFKA-003", "MOV-K-003", "EVT-K-003", 1);
            Reconciliation resultado3 = crearReconciliation("ACC-KAFKA-004", "MOV-K-004", "EVT-K-004", 1);

            when(reconciliationServicePort.processReconciliationBatch(anyList()))
                    .thenReturn(Flux.just(resultado1, resultado2, resultado3));

            StepVerifier.create(kafkaConsumer.consumeBatch(commands))
                    .expectNextCount(3)
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Escenario: Manejo de errores en consumo")
    class ManejoErrores {

        @Test
        @DisplayName("Debe manejar error de reconciliacion y continuar")
        void debeManejarErrorYContinuar() {
            ReconcileMovementCommand command = crearCommand("ACC-KAFKA-005", "MOV-K-005", "EVT-K-005", 1);

            when(reconciliationServicePort.reconcileMovement(any(ReconcileMovementCommand.class)))
                    .thenReturn(Mono.error(new RuntimeException("Error de base de datos")));

            StepVerifier.create(kafkaConsumer.consume(command))
                    .expectError(RuntimeException.class)
                    .verify();
        }

        @Test
        @DisplayName("Debe reintentar mensaje fallido hasta maximo de intentos")
        void debeReintentarMensaje() {
            ReconcileMovementCommand command = crearCommand("ACC-KAFKA-006", "MOV-K-006", "EVT-K-006", 1);
            Reconciliation resultado = crearReconciliation("ACC-KAFKA-006", "MOV-K-006", "EVT-K-006", 1);

            when(reconciliationServicePort.reconcileMovement(any(ReconcileMovementCommand.class)))
                    .thenReturn(
                            Mono.error(new RuntimeException("Transient error")),
                            Mono.error(new RuntimeException("Transient error")),
                            Mono.just(resultado)
                    );

            StepVerifier.create(kafkaConsumer.consumeWithRetry(command, 3))
                    .assertNext(r -> assertThat(r).isNotNull())
                    .verifyComplete();

            verify(reconciliationServicePort, times(3)).reconcileMovement(any(ReconcileMovementCommand.class));
        }
    }

    @Nested
    @DisplayName("Escenario: Idempotencia en consumo")
    class Idempotencia {

        @Test
        @DisplayName("Debe detectar mensaje duplicado por eventId y version")
        void debeDetectarDuplicado() {
            ReconcileMovementCommand command = crearCommand("ACC-KAFKA-007", "MOV-K-007", "EVT-K-DUP", 1);

            when(reconciliationServicePort.isMovementProcessed("EVT-K-DUP", 1))
                    .thenReturn(Mono.just(true));

            Reconciliation existente = crearReconciliation("ACC-KAFKA-007", "MOV-K-007", "EVT-K-DUP", 1);
            existente = existente.withStatus(ReconciliationStatus.MATCHED);

            when(reconciliationServicePort.getReconciliationById(any(UUID.class)))
                    .thenReturn(Mono.just(existente));

            StepVerifier.create(kafkaConsumer.consume(command))
                    .assertNext(r -> assertThat(r.isIdempotencyProcessed()).isTrue())
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe procesar mensaje nuevo cuando no existe duplicado")
        void debeProcesarNuevoCuandoNoHayDuplicado() {
            ReconcileMovementCommand command = crearCommand("ACC-KAFKA-008", "MOV-K-008", "EVT-K-NEW", 1);
            Reconciliation resultado = crearReconciliation("ACC-KAFKA-008", "MOV-K-008", "EVT-K-NEW", 1);

            when(reconciliationServicePort.isMovementProcessed("EVT-K-NEW", 1))
                    .thenReturn(Mono.just(false));
            when(reconciliationServicePort.reconcileMovement(any(ReconcileMovementCommand.class)))
                    .thenReturn(Mono.just(resultado));

            StepVerifier.create(kafkaConsumer.consume(command))
                    .assertNext(r -> assertThat(r.isIdempotencyProcessed()).isFalse())
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Escenario: Reprocesamiento via Kafka replay")
    class ReprocesamientoKafka {

        @Test
        @DisplayName("Debe reprocesar mensajes desde offset especifico")
        void debeReprocesarDesdeOffset() {
            when(reconciliationServicePort.getPendingReconciliationsOlderThan(any()))
                    .thenReturn(Flux.empty());

            StepVerifier.create(kafkaConsumer.reprocessFromOffset(0L))
                    .expectNextCount(0)
                    .verifyComplete();
        }
    }

    private ReconcileMovementCommand crearCommand(String accountId, String movementId,
                                                   String eventId, Integer version) {
        return new ReconcileMovementCommand(
                accountId, movementId, "CORE", eventId, version,
                new BigDecimal("2000.00"), "USD", LocalDate.now(),
                List.of(new com.pragma.reconciliation.application.commands.MovementSource("CORE", new BigDecimal("2000.00")))
        );
    }

    private Reconciliation crearReconciliation(String accountId, String movementId,
                                                String eventId, Integer version) {
        return new Reconciliation(
                UUID.randomUUID(), accountId, movementId, eventId, version,
                new BigDecimal("2000.00"), "USD", LocalDate.now(),
                ReconciliationStatus.PENDING, "CORE",
                java.time.Instant.now(), null, null, false
        );
    }
}