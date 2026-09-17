# Estados de Reconciliación

## Descripción General

El sistema de conciliación bancaria utiliza una máquina de estados finitos para gestionar el ciclo de vida de cada movimiento reconciliado. Cada movimiento atraviesa estados específicos que determinan su tratamiento y las acciones disponibles.

## Estados Definidos

### PENDING
Estado inicial de todo movimiento recibido. Indica que el movimiento ha sido ingestado pero aún no ha sido procesado contra las fuentes de datos disponibles. Un movimiento en este estado puede ser procesado nuevamente si arrive duplicados o fuera de orden.

### MATCHED
Estado alcanzado cuando el movimiento coincide exactamente en las tres fuentes de datos: core bancario, gateway de pagos y sistema de liquidación. La coincidencia se realiza comparando el eventId, version, accountId, amount y transactionDate. Este estado es terminal, no requiere intervención adicional.

### MISMATCHED
Estado alcanzado cuando se detectan discrepancias entre las fuentes de datos. Las causas comunes incluyen diferencias en el monto, moneda incorrecta, fechas de transacción divergentes o accountId inconsistentes. Este estado puede requerir revisión manual dependiendo de la naturaleza de la discrepancia.

### MANUAL
Estado terminal para movimientos que requieren revisión por parte del equipo de operaciones. Se alcanza automáticamente cuando el sistema detecta un patrón de discrepancia no resoluble automáticamente, o cuando un analista marca el movimiento para revisión.

## Transiciones de Estados

```
PENDING → MATCHED    (coincidencia exacta en todas las fuentes)
PENDING → MISMATCHED (discrepancia detectada, resoluble)
PENDING → MANUAL    (discrepancia no resoluble automáticamente)
MISMATCHED → MANUAL (analista decide revisión)
MATCHED → PENDING   (reprocesamiento activado)
MANUAL → PENDING    (reprocesamiento después de corrección)
```

## Reglas de Transición

Cada estado define qué transiciones son válidas mediante el método canTransitionTo. Las transiciones inválidas generan una MovementStateTransitionException. El sistema impide que un movimiento en estado terminal (MATCHED o MANUAL) vuelva a procesarse sin haber sido explícitamente marcado para reprocesamiento.

## Estados que Requieren Intervención

Los estados MISMATCHED y MANUAL indican que el flujo automático no pudo completar la reconciliación. El sistema expone métricas por cada estado mediante el endpoint de Actuador, permitiendo al equipo de operaciones monitorear la carga de trabajo pendiente de revisión.

## Consideraciones de Implementación

El enum ReconciliationStatus encapsula la lógica de transiciones. Cada estado incluye una descripción legible para auditoría. El método isTerminal() identifica estados que no requieren procesamiento adicional, mientras que requiresProcessing() indica estados activos para el motor de reconciliación.