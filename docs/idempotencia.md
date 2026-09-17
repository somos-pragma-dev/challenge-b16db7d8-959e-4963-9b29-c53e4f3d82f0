# Idempotencia en el Sistema de Reconciliación

## Principio de Idempotencia

El sistema de conciliación procesa eventos de múltiples fuentes que pueden llegar duplicados debido a retransmisiones en Kafka, reintentos de las fuentes emitentes, o fallos de red durante el consumo. La idempotencia garantiza que procesar el mismo evento múltiples veces produzca el mismo resultado que procesarlo una sola vez.

## Clave de Identificación Única

Cada movimiento se identifica mediante la combinación de dos campos: eventId y version. El eventId es un identificador único proporcionado por la fuente emitente, mientras que version indica la versión del evento ante posibles correcciones o actualizaciones.

El par (eventId, version) forma una clave única en el sistema. Si la fuente reenvía el mismo evento con la misma versión, el sistema reconoce el duplicado y evita procesarlo nuevamente.

## Implementación del Check de Duplicados

Antes de procesar cualquier movimiento, el servicio verifica la existencia del par (eventId, version) en la base de datos mediante el método existsByEventIdAndVersion del puerto de persistencia. Si el registro existe, se retorna el movimiento previamente procesado sin ejecutar la lógica de matching.

```
Recibir evento → Extraer eventId y version → Consultar BD → 
Si existe: retornar resultado anterior → Si no existe: procesar normalmente
```

## Control de Versiones

El campo version permite manejar actualizaciones de eventos. Si la fuente envía el mismo eventId con version superior, el sistema procesa la nueva versión como un movimiento independiente, actualizando el registro existente si ya existe un reconciliation para ese eventId con versión anterior.

Esta estrategia soporta el patrón de eventos de actualización donde un movimiento puede ser corregido posteriormente por la fuente emitente. El sistema mantiene el historial de versiones, permitiendo auditoría completa.

## Impacto en el Flujo de Reconciliación

El flag idempotencyProcessed en la entidad Reconciliation indica si el movimiento fue reconocido como duplicado durante el procesamiento. Este flag permite distinguir entre movimientos procesados por primera vez y duplicados detectados, útil para métricas y debugging.

El método isDuplicateOf() compara dos objetos Reconciliation para determinar si representan el mismo evento base, utilizando eventId como criterio de comparación principal.

## Consideraciones de Rendimiento

La verificación de idempotencia se realiza antes de cualquier operación costosa (consultas a fuentes externas, lógica de matching). El índice único sobre la columna compuesta (event_id, version) en PostgreSQL garantiza que la consulta de verificación sea eficiente incluso con alto volumen de datos.

## Integración con Kafka

El consumidor Kafka utiliza el grupo de consumidores configurado para gestionar el offset de procesamiento. Cuando un mensaje se procesa exitosamente, el offset avanza. Si el consumidor falla antes de confirmar, Kafka reenvía el mensaje, y el sistema de idempotencia evita el procesamiento duplicado al detectar el eventId existente.