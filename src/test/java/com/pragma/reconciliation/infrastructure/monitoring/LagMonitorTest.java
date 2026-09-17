package com.pragma.reconciliation.infrastructure.monitoring;

import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import com.pragma.reconciliation.domain.ports.in.ReconciliationServicePort;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
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
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LagMonitor - Pruebas del monitoreo de lag y alertas")
class LagMonitorTest {

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private ReconciliationServicePort reconciliationService;

    @Mock
    private Counter lagAlertCounter;

    @Mock
    private Timer processingTimer;

    private LagMonitor lagMonitor;

    @BeforeEach
    void setUp() {
        when(meterRegistry.counter(anyString())).thenReturn(lagAlertCounter);
        when(meterRegistry.timer(anyString())).thenReturn(processingTimer);

        lagMonitor = new LagMonitor(reconciliationService, meterRegistry);
    }

    @Nested
    @DisplayName("Escenario: Medicion de lag dentro del SLA")
    class LagDentroSLA {

        @Test
        @DisplayName("Debe reportar lag bajo cuando la diferencia es menor al umbral")
        void debeReportarLagBajo() {
            Instant eventoTime = Instant.now().minusSeconds(60);

            StepVerifier.create(lagMonitor.measureLag(eventoTime))
                    .assertNext(lag -> {
                        assertThat(lag.seconds()).isLessThan(300);
                        assertThat(lag.alertTriggered()).isFalse();
                    })
                    .verifyComplete();
        }

        @Test
        @DisplayName("No debe generar alerta cuando lag esta dentro del SLA")
        void noDebeGenerarAlerta() {
            Instant eventoTime = Instant.now().minusSeconds(120);

            lagMonitor.recordEvent(eventoTime);

            verify(lagAlertCounter, never()).increment();
        }
    }

    @Nested
    @DisplayName("Escenario: Medicion de lag fuera del SLA")
    class LagFueraSLA {

        @Test
        @DisplayName("Debe reportar lag alto cuando supera el umbral de 5 minutos")
        void debeReportarLagAlto() {
            Instant eventoTime = Instant.now().minusSeconds(400);

            StepVerifier.create(lagMonitor.measureLag(eventoTime))
                    .assertNext(lag -> {
                        assertThat(lag.seconds()).isGreaterThan(300);
                        assertThat(lag.alertTriggered()).isTrue();
                    })
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe generar alerta cuando lag supera el SLA")
        void debeGenerarAlerta() {
            Instant eventoTime = Instant.now().minusSeconds(400);

            when(lagAlertCounter.increment()).thenReturn(1.0);

            lagMonitor.recordEvent(eventoTime);
            lagMonitor.checkAndAlert(eventoTime);

            verify(lagAlertCounter).increment();
        }

        @Test
        @DisplayName("Debe registrar metricas de lag para Prometheus")
        void debeRegistrarMetricasPrometheus() {
            Instant eventoTime = Instant.now().minusSeconds(600);

            lagMonitor.recordEvent(eventoTime);

            verify(meterRegistry).counter("reconciliation.lag.alerts.triggered");
            verify(meterRegistry).timer("reconciliation.lag.measurement");
        }
    }

    @Nested
    @DisplayName("Escenario: Monitoreo continuo del lag")
    class MonitoreoContinuo {

        @Test
        @DisplayName("Debe iniciar flujo de monitoreo con intervalo configurado")
        void debeIniciarMonitoreo() {
            LagMonitor monitor = new LagMonitor(reconciliationService, meterRegistry);

            StepVerifier.create(monitor.startMonitoring().take(3))
                    .expectNextCount(3)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe detectar tendencia de lag creciente")
        void debeDetectarTendenciaCreciente() {
            Instant tiempo1 = Instant.now().minusSeconds(100);
            Instant tiempo2 = Instant.now().minusSeconds(200);
            Instant tiempo3 = Instant.now().minusSeconds(300);

            lagMonitor.recordEvent(tiempo1);
            lagMonitor.recordEvent(tiempo2);
            lagMonitor.recordEvent(tiempo3);

            StepVerifier.create(lagMonitor.getLagTrend())
                    .assertNext(trend -> {
                        assertThat(trend).isGreaterThan(0);
                    })
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Escenario: Alertas de SLA")
    class AlertasSLA {

        @Test
        @DisplayName("Debe notificar cuando lag supera threshold por primera vez")
        void debeNotificarPrimeraVez() {
            Instant eventoTime = Instant.now().minusSeconds(360);

            when(lagAlertCounter.increment()).thenReturn(1.0);

            StepVerifier.create(lagMonitor.checkAndAlert(eventoTime))
                    .assertNext(alert -> {
                        assertThat(alert.lagSeconds()).isGreaterThan(300);
                        assertThat(alert.message()).contains("SUPERO");
                    })
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe reducir ruido de alertas consecutivas")
        void debeReducirRuidoAlertas() {
            Instant eventoTime = Instant.now().minusSeconds(400);

            when(lagAlertCounter.increment()).thenReturn(1.0);

            lagMonitor.checkAndAlert(eventoTime);
            lagMonitor.checkAndAlert(eventoTime.minusSeconds(10));
            lagMonitor.checkAndAlert(eventoTime.minusSeconds(20));

            verify(lagAlertCounter, times(1)).increment();
        }
    }

    @Nested
    @DisplayName("Escenario: Metricas de rendimiento")
    class MetricasRendimiento {

        @Test
        @DisplayName("Debe medir tiempo de procesamiento de eventos")
        void debeMedirTiempoProcesamiento() {
            lagMonitor.recordProcessingTime(Duration.ofMillis(150));

            verify(processingTimer).record(any(Duration.class));
        }

        @Test
        @DisplayName("Debe exportar metricas para Prometheus")
        void debeExportarPrometheus() {
            when(meterRegistry.get(anyString())).thenReturn(mock(io.micrometer.core.instrument.Meter.class));

            String metrics = lagMonitor.exportMetrics();

            assertThat(metrics).contains("reconciliation_lag");
        }

        @Test
        @DisplayName("Debe contar eventos procesados")
        void debeContarEventosProcesados() {
            lagMonitor.recordEvent(Instant.now().minusSeconds(50));
            lagMonitor.recordEvent(Instant.now().minusSeconds(50));
            lagMonitor.recordEvent(Instant.now().minusSeconds(50));

            verify(meterRegistry, times(3)).counter(anyString());
        }
    }
}