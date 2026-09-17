# Mecanismo de Reprocesamiento: Kafka Replay vs Snapshot PostgreSQL

## Introducción al Problema de Reprocesamiento

En un sistema de conciliación bancaria en tiempo real que consume eventos de tres fuentes distintas (core bancario, gateway de pagos, sistema de liquidación), existen escenarios donde se requiere reprocesar movimientos ya conciliados. Estos escenarios incluyen: recuperación ante fallos del consumidor, corrección de reconciliaciones incorrectas, o reprocesamiento histórico para reportes. La decisión arquitectónica entre utilizar replay de Kafka o restaurar desde snapshot de PostgreSQL tiene implicaciones directas en latencia, consistencia y complejidad operativa.

## Análisis de Replay de Kafka

### Funcionamiento

El reprocesamiento mediante replay de Kafka consiste en repositionar el consumidor a un offset anterior en el topic, permitiendo reprocesar todos los mensajes desde ese punto. Kafka almacena los mensajes durante un período configurable (por defecto 7 días) y mantiene el offset de cada partición. El consumidor puede usar los métodos `seek()` o `seekToBeginning()` para repositionarse.

### Ventajas del Replay

La principal ventaja del replay de Kafka es la capacidad de reprocesar eventos originales exactamente como llegaron, preservando el orden exacto de llegada y los timestamps originales. Esto resulta crítico cuando la reconciliación depende de la secuencia temporal de los movimientos. Además, no requiere almacenamiento adicional en la base de datos más allá de lo ya persistido, ya que los eventos siguen disponibles en el broker de Kafka.

Otra ventaja significativa es la simplicidad de implementación: basta con repositionar el offset del consumidor sin necesidad de crear mecanismos complejos de restauración de estado. El sistema puede reprocesar eventos incluso si la base de datos fue restaurada desde un backup antiguo.

### Desventajas del Replay

La desventaja principal es la limitación temporal: si el período de retención de Kafka es menor que la ventana de reprocesamiento requerida, los eventos ya no estarán disponibles. Adicionalmente, el replay requiere reprocesar TODOS los eventos desde el punto de inicio, lo cual puede ser costoso en términos de tiempo y recursos cuando se necesita reprocesar un período extenso.

El replay también puede generar presión sobre el sistema de conciliación al procesar eventos que ya fueron conciliados, requiriendo que el mecanismo de idempotencia filtre correctamente los mensajes duplicados.

## Análisis de Snapshot desde PostgreSQL

### Funcionamiento

El reprocesamiento mediante snapshot consiste en restaurar el estado de las reconciliaciones directamente desde PostgreSQL, utilizando los datos persistidos en la tabla de reconciliaciones. Este enfoque trata la base de datos como la fuente autoritativa del estado, reconstructura el contexto de procesamiento a partir de los registros almacenados, y permite iniciar el procesamiento desde cualquier punto temporal presente en la base.

### Ventajas del Snapshot

La ventaja fundamental del snapshot es la disponibilidad: los datos en PostgreSQL tienen un período de retención mucho mayor que Kafka (meses o años con políticas de archivado). El reprocesamiento es más rápido porque opera directamente sobre datos estructurados en lugar de procesar streams de eventos. Además, el snapshot permite reprocesamiento selectivo: se pueden elegir registros específicos por cuenta, estado o rango de fechas sin afectar otros movimientos.

Desde la perspectiva de consistencia, PostgreSQL garantiza transacciones ACID, asegurando que el estado restaurado es consistente y completo. No depende de la configuración de retención del broker de mensajes.

### Desventajas del Snapshot

La desventaja principal es que el snapshot restaura el ESTADO de la reconciliación, no el EVENTO original completo. Si el evento original contenía metadatos que no se persistieron en la tabla de reconciliaciones, esa información se pierde. Adicionalmente, restaurar un snapshot masivo puede generar carga significativa sobre la base de datos.

## Mecanismo de Idempotencia y su Relación con el Reprocesamiento

El sistema implementa idempotencia mediante la combinación de `eventId` (identificador único del movimiento) y `version` (número de versión secuencial). Antes de procesar cualquier movimiento, el servicio verifica en PostgreSQL si existe un registro con el mismo `eventId` y `version` mediante el método `existsByEventIdAndVersion()`. Si existe, el movimiento se marca como duplicado y se omite el procesamiento.

Esta implementación de idempotencia es fundamental para el reprocesamiento porque permite aplicar indistintamente replay de Kafka o restauración de snapshot sin generar inconsistencias. Cuando se reprocesa un evento que ya fue conciliado, el sistema lo detecta y lo descarta automáticamente.

## Decisión Arquitectónica: Enfoque Híbrido

### Estrategia Elegida

Se implementa un enfoque híbrido que utiliza ambos mecanismos según el escenario específico:

**Para recuperación ante fallos inmediatos:** Se utiliza replay de Kafka con un período de retención de 7 días. Cuando el consumidor falla y se reinicia, automaticamente reprocesa los mensajes no reconocidos. El offset se commitea solo después de persistir exitosamente en PostgreSQL, garantizando exactly-once semantics.

**Para reprocesamiento histórico mayor a 7 días:** Se utiliza restauración de snapshot desde PostgreSQL. El servicio `ReconciliationService` proporciona el método `reprocessReconciliation(UUID id)` que permite reprocesar una reconciliación específica, y `getPendingReconciliationsOlderThan(Instant threshold)` para identificar registros antiguos que requieren reprocesamiento.

**Para reprocesamiento masivo programado:** Se combina ambos enfoques: primero se identifican los registros a reprocesar mediante consulta a PostgreSQL, luego se滤an los eventos correspondientes en Kafka para verificar su disponibilidad, y finalmente se ejecuta el reprocesamiento.

### Criterios de Selección

| Escenario | Mecanismo Preferido | Justificación |
|-----------|---------------------|----------------|
| Fallo de consumidor | Kafka Replay | Recuperación automática, menor latencia |
| Corrección manual (< 7 días) | Kafka Replay | Preserva secuencia original |
| Reprocesamiento histórico (> 7 días) | PostgreSQL Snapshot | Eventos no disponibles en Kafka |
| Reporte de período específico | PostgreSQL Snapshot | Selectividad y performance |
| Recuperación ante disaster | PostgreSQL Snapshot | Fuente autoritativa de estado |

## Implementación en el Código

El servicio de reconciliación expone los siguientes métodos para soportar los escenarios de reprocesamiento:

- `isMovementProcessed(String eventId, Integer version)`: Verifica si un movimiento ya fue procesado (idempotencia).
- `reprocessReconciliation(UUID id)`: Reprocesa una reconciliación específica desde PostgreSQL.
- `getPendingReconciliationsOlderThan(Instant threshold)`: Obtiene reconciliaciones pendientes anteriores a un umbral temporal.

## Conclusión

La elección entre Kafka replay y PostgreSQL snapshot no es mutuamente exclusiva. Un sistema de conciliación bancaria robusto debe soportar ambos mecanismos, seleccionando automáticamente según el escenario. El sistema implementado proporciona esta flexibilidad mediante la combinación de retención de Kafka para recuperación inmediata y PostgreSQL como fuente autoritativa para reprocesamiento histórico.