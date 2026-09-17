# Ventana de Matching

## Concepto de Ventana en Reconciliación

La ventana de matching define el período durante el cual el sistema busca coincidencias para un movimiento recibido. Cuando llega un movimiento, el motor de reconciliación consulta las fuentes de datos dentro de esta ventana temporal para identificar registros correspondientes en las otras fuentes.

## Opciones Evaluadas

### Ventana de 5 Minutos
Ventana corta que limita el tiempo de búsqueda a 300 segundos desde la marca de tiempo del movimiento. Reduce la carga computacional por consulta y minimiza la ventana de exposición a mensajes duplicados o fuera de orden. Adecuada para sistemas con alto volumen de transacciones y baja latencia esperada.

### Ventana de 1 Hora
Ventana amplia que extiende el período de búsqueda a 3600 segundos. Proporciona mayor tolerancia para movimientos que llegan con retraso significativo debido a latency de red o procesamiento asíncrono en las fuentes externas. Incrementa la probabilidad de encontrar coincidencias para movimientos lentos.

## Criterios de Selección

La elección depende de tres factores principales: el SLA delatencia del sistema financiero, el comportamiento típico de arrival de mensajes de las fuentes externas, y la capacidad de procesamiento de la base de datos para consultas con rangos temporales grandes.

El sistema financiero tiene un SLA de 5 minutos para la conciliación, lo que establece un límite superior para la ventana. Los movimientos que llegan más allá de este tiempo se consideran fuera del ciclo normal y requieren reprocesamiento explícito.

## Decisión Implementada

El sistema utiliza una ventana de matching de 5 minutos sincronizada con el SLA delatencia. Esta decisión garantiza que el motor de reconciliación procese movimientos dentro del período aceptable para el negocio, evitando consultas innecesarias sobre datos antiguos.

Los movimientos que arrive después de vencida la ventana se treat como fuera de orden mediante el método isOutOfOrder(), que compara la marca de tiempo del mensaje contra el tiempo actual menos la ventana configurada. Estos mensajes activan el mecanismo de reprocesamiento en lugar de buscar coincidencias en la ventana.

## Configuración

La ventana se configura mediante ReconciliationConfig con la propiedad matching.window.minutes. El valor por defecto es 5 y puede ajustarse según las necesidades operativas del ambiente sin modificar código.

## Métricas Asociadas

El sistema expone métricas sobre movimientos fuera de la ventana mediante Micrometer. El contador reconciliation.window.missed.increment() se incrementa cada vez que un movimiento se detecta como fuera de orden, permitiendo monitorear la salud del sistema y detectar patrones anómalos.