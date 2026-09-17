# Prompt para Mejorar el Codigo Base

Copia y pega el contenido del bloque de abajo en un asistente de IA (Claude, ChatGPT)
para obtener un ZIP con el proyecto completo y arrancable.

Si preferis trabajar en tu editor con un agente local (Claude Code, Cursor, Copilot), usa `AGENTS.md` en vez de este archivo: dice lo mismo pero para que escriba los archivos en disco.

## Las dos reglas que no se negocian

1. **Completa el boilerplate.** Todo lo que el proyecto necesita para compilar y arrancar: manifiesto de dependencias, punto de entrada, configuracion, capa de interfaz, y las capas del patron arquitectonico declarado. Eso es andamiaje y es tu trabajo.
2. **NO resuelvas el reto.** Los entregables de las fases son el trabajo de la persona. El hueco pedagogico se deja como esta: el proyecto arranca, pero lo que el reto pide implementar NO esta implementado.

Dicho de otra forma: si algo impide compilar, arreglalo. Si algo es logica de negocio incompleta, validaciones ausentes, un secreto hardcodeado o un patron mejorable, dejalo exactamente como esta — es lo que la persona tiene que encontrar.

## Lo que le falta a este proyecto

Esto NO lo tenes que adivinar: salio de comparar el proyecto contra la arquitectura declarada del reto y de un analisis estatico del codigo. Completalo TODO.

### Boilerplate del stack que falta

Sin esto no compila ni arranca. Es andamiaje, no toca nada de lo pedagogico:

- **Punto de entrada del stack elegido** — Sin un punto de entrada reconocible, el runtime no tiene por donde arrancar la aplicacion.
- **Capa de interfaz (controller/handler)** — Sin una capa de interfaz explicita, no hay forma de invocar la logica de negocio desde afuera del proceso.

### Archivos que la arquitectura del reto declara y no estan

Creálos con implementacion real, en la capa que les corresponde:

- `docs/alerta_lag.md`

### Referencias colgando en el codigo que si esta

Cada una rompe la compilacion:

- `src/main/java/com/pragma/reconciliation/ReconciliationApplication.java` — `ReconcileMovementCommand`: ReconcileMovementCommand se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.application.commands.ReconcileMovementCommand.
- `src/main/java/com/pragma/reconciliation/domain/services/Reprocesamiento.java` — `ReconcileMovementCommand`: ReconcileMovementCommand se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.application.commands.ReconcileMovementCommand.
- `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitor.java` — `Reconciliation`: El import com.pragma.reconciliation.domain.model.Reconciliation no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- `src/test/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitorTest.java` — `Reconciliation`: El import com.pragma.reconciliation.domain.model.Reconciliation no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- `src/test/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitorTest.java` — `ReconciliationStatus`: El import com.pragma.reconciliation.domain.model.ReconciliationStatus no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- `src/main/java/com/pragma/reconciliation/ReconciliationApplication.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/pragma/reconciliation/ReconciliationApplication.java` — `reactor.core.scheduler`: El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/pragma/reconciliation/domain/ports/in/ReconciliationServicePort.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/pragma/reconciliation/domain/ports/out/ReconciliationPersistencePort.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumer.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumer.java` — `reactor.core.scheduler`: El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitor.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/test/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumerTest.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/test/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitorTest.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/pragma/reconciliation/domain/services/Reprocesamiento.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.eventId`: Se invoca `eventId` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.version`: Se invoca `version` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.source`: Se invoca `source` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.id`: Se invoca `id` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.accountId`: Se invoca `accountId` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.movementId`: Se invoca `movementId` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.amount`: Se invoca `amount` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.currency`: Se invoca `currency` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.transactionDate`: Se invoca `transactionDate` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getId`: Se invoca `getId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setId`: Se invoca `setId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setAccountId`: Se invoca `setAccountId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setMovementId`: Se invoca `setMovementId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setEventId`: Se invoca `setEventId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setVersion`: Se invoca `setVersion` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setAmount`: Se invoca `setAmount` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setCurrency`: Se invoca `setCurrency` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setTransactionDate`: Se invoca `setTransactionDate` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setStatus`: Se invoca `setStatus` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setSource`: Se invoca `setSource` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setCreatedAt`: Se invoca `setCreatedAt` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setProcessedAt`: Se invoca `setProcessedAt` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setMismatchReason`: Se invoca `setMismatchReason` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setIdempotencyProcessed`: Se invoca `setIdempotencyProcessed` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getAccountId`: Se invoca `getAccountId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getMovementId`: Se invoca `getMovementId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getEventId`: Se invoca `getEventId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getVersion`: Se invoca `getVersion` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getAmount`: Se invoca `getAmount` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getCurrency`: Se invoca `getCurrency` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getTransactionDate`: Se invoca `getTransactionDate` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getStatus`: Se invoca `getStatus` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getSource`: Se invoca `getSource` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getCreatedAt`: Se invoca `getCreatedAt` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getProcessedAt`: Se invoca `getProcessedAt` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getMismatchReason`: Se invoca `getMismatchReason` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.isIdempotencyProcessed`: Se invoca `isIdempotencyProcessed` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumer.java` — `ReconcileMovementCommand.eventId`: Se invoca `eventId` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumer.java` — `ReconcileMovementCommand.version`: Se invoca `version` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/config/ReconciliationConfig.java` — `Alerts.getCheckIntervalSeconds`: Se invoca `getCheckIntervalSeconds` sobre `Alerts`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java` — `ReconcileMovementCommand.accountId`: Se invoca `accountId` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java` — `ReconcileMovementCommand.movementId`: Se invoca `movementId` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java` — `ReconcileMovementCommand.eventId`: Se invoca `eventId` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java` — `ReconcileMovementCommand.version`: Se invoca `version` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java` — `ReconcileMovementCommand.currency`: Se invoca `currency` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java` — `ReconcileMovementCommand.transactionDate`: Se invoca `transactionDate` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.

## Como saber que terminaste

```bash
el comando de build o arranque canonico del stack elegido
```

Ese comando corriendo sin errores es la definicion de "listo".

---

```
## Briefing del reto (autoridad)
Este bloque manda sobre los archivos adjuntos. El stack y el rol salen de AQUÍ, no de un topic genérico ni de markdown placeholder.

### Contexto técnico original
Diseño de motor de conciliación que consume streams de movimientos desde 3 fuentes (core bancario, gateway de pagos, sistema de liquidación) y detecta discrepancias en ventanas móviles. Cada movimiento debe reconciliarse contra las 3 fuentes con tolerancia a mensajes fuera de orden y llegadas duplicadas. El desarrollador senior debe diseñar la máquina de estados de cada Reconciliation (Pending, Matched, Mismatched, Manual), justificar la ventana de matching (5 min vs 1 hora), definir cómo maneja idempotencia con eventId + version, y elegir entre reprocesamiento por replay de Kafka vs snapshot desde PostgreSQL. Adicionalmente debe explicar cómo alertar al equipo de operaciones cuando el lag de conciliación supera un SLA.

### Reto
- Tema: TEST-CT
- Seniority: senior-l2
- Tipo: mixed
- Título: Diseño de sistema de conciliación bancaria en tiempo real con reprocesamiento idempotente
- Tiempo estimado: 12 horas

### Fases (trabajo del HUMANO — PROHIBIDO completarlas)
No implementes estos entregables. Dejalos como hueco pedagógico. El asistente solo materializa el proyecto arrancable para que el participante pueda trabajar.
- Fase 1: Exploración del dominio y definición de requisitos — objetivo: Identificar las fuentes de movimientos, definir los estados de la reconciliación y establecer los criterios de tolerancia a mensajes fuera de orden y duplicados. — entregable (NO resolver): Documento que describe las fuentes de movimientos, los estados de la reconciliación y los criterios de tolerancia.
- Fase 2: Diseño de la máquina de estados y definición de la ventana de matching — objetivo: Diseñar la máquina de estados de la reconciliación y justificar la elección de la ventana de matching (5 minutos vs 1 hora). — entregable (NO resolver): Diagrama de la máquina de estados de la reconciliación y documento que justifica la elección de la ventana de matching.
- Fase 3: Definición de la idempotencia y elección del mecanismo de reprocesamiento — objetivo: Definir cómo se manejará la idempotencia con eventId + version y elegir entre reprocesamiento por replay de Kafka vs snapshot desde PostgreSQL. — entregable (NO resolver): Documento que describe cómo se manejará la idempotencia y justifica la elección del mecanismo de reprocesamiento.
- Fase 4: Definición de la alerta de lag de conciliación — objetivo: Definir cómo se alertará al equipo de operaciones cuando el lag de conciliación supere un SLA de 5 minutos. — entregable (NO resolver): Documento que describe cómo se medirá el lag de conciliación, los criterios para alertar al equipo de operaciones y el mecanismo de alerta.

Eres un asistente experto en análisis, corrección y generación de archivos de cualquier tipo:
código fuente, documentación, hojas de cálculo, documentos Word, configuraciones, entre otros.
Voy a enviarte una cadena de texto que contiene uno o más archivos. Cada archivo está delimitado por un marcador con el siguiente formato:
// === ARCHIVO: ruta/del/archivo.extension ===
o también puede aparecer como:
## === ARCHIVO: ruta/del/archivo.extension ===
Lo que sigue al marcador puede ser:

El contenido real del archivo (código, texto, YAML, etc.)
Una descripción en lenguaje natural de lo que debe contener el archivo


TU TAREA
PASO 0 — ¿Esto es un proyecto o una carcasa?
Antes de extraer archivos, leé el Briefing (si está) y diagnosticá el adjunto.

Es CARCASA si ocurre CUALQUIERA de estas:
- No hay manifiesto de dependencias del stack del briefing (manifest.json de VTEX IO / package.json / pom.xml / build.gradle / requirements.txt / go.mod / *.tf / *.csproj, según corresponda)
- Hay un "binario" que en realidad es un comentario ("no puede ser mostrado como texto plano", placeholder .fig/.docx vacío)
- Los markdowns ya completan entregables de fases posteriores ("se implementó fade-in", lista de áreas ya resuelta)

Si es CARCASA:
- MATERIALIZÁ un proyecto que arranca en el stack del briefing (VTEX IO Store Framework, Angular, Terraform, pytest, Nest, etc.). Incluí manifiesto, punto de entrada y capa de interfaz reales.
- NO copies los markdowns de "solución" como si fueran el producto. Son ruido de generación.
- NO resuelvas las fases del briefing (están marcadas PROHIBIDO). Dejá el hueco pedagógico: el flujo existe, las microinteracciones/calidad/infra que el reto pide NO están hechas.
- Después seguí al PASO 5 (ZIP).

Si es un proyecto REAL (manifiesto + código que compila o arranca):
- Seguí PASO 1 en adelante. 🔴 compilación sí. 🟡 pedagógico no.

PASO 1 — Detección y extracción
Identifica todos los archivos presentes en la cadena. Para cada archivo extrae:

Su ruta completa (ej: src/main/java/com/pragma/Service.java)
Su contenido o descripción

PASO 2 — Clasificación por tipo
Clasifica cada archivo en una de estas categorías:
A) Código fuente (Java, Python, TypeScript, JavaScript, Kotlin, etc.)
B) Configuración / documentación (YAML, properties, Markdown, JSON, txt, etc.)
C) Excel (.xlsx, .xls, .csv)
D) Word (.docx, .doc)
E) Otro tipo de archivo binario o especial
PASO 3 — Clasificación de errores en código fuente

Objetivo prioritario: que el proyecto compile. No corrijas flujo de negocio ni lógica funcional.

Antes de modificar cualquier archivo de código fuente, clasifica cada problema encontrado en una de estas dos categorías:
🔴 ERROR DE COMPILACIÓN — corregir siempre
Son errores que impiden que el proyecto arranque, sin valor pedagógico:

Import faltante o incorrecto
Clase, método o variable referenciada que no existe en ningún archivo del proyecto
Error de sintaxis
Anotación con atributos inválidos
Dependencia ausente en pom.xml, package.json, etc.
Archivo referenciado que no existe y debe ser creado con implementación mínima

→ CORREGIR estos errores.
🟡 PROBLEMA FUNCIONAL O DE CALIDAD — preservar siempre
Son problemas que no impiden compilar. Pueden ser intencionales para el aprendizaje:

Clave secreta hardcodeada ("secret", "password123")
API deprecada que funciona pero tiene reemplazo moderno
Lógica de negocio incorrecta o incompleta
Código redundante o de baja legibilidad
Falta de validaciones en flujo de negocio
Patrones de diseño incorrectos pero funcionales
Concurrencia no segura
Configuración funcional pero no óptima

→ PRESERVAR tal cual. No corregir, no mejorar, no comentar.
PASO 4 — Procesamiento según tipo de archivo
Tipo A — Código fuente
Aplica únicamente las correcciones clasificadas como 🔴 ERROR DE COMPILACIÓN.
No alteres ningún elemento clasificado como 🟡 PROBLEMA FUNCIONAL O DE CALIDAD.
Si falta un archivo referenciado, créalo con la implementación mínima necesaria para compilar.
Tipo B — Configuración / documentación
Extrae el contenido tal cual, sin modificaciones salvo errores evidentes de sintaxis
(ej: YAML mal indentado).
Tipo C — Excel (.xlsx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un archivo Excel funcional con:

Fila de encabezados en negrita con color de fondo distintivo
Columnas con ancho ajustado al contenido
Tipos de dato correctos por columna
Validaciones si la descripción lo indica
Hojas nombradas descriptivamente si hay más de una
Filas de ejemplo si no hay datos reales

Tipo D — Word (.docx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un documento Word funcional con:

Estilos de título (Título 1, Título 2) para jerarquía de secciones
Fuente legible (Calibri o equivalente), tamaño 11-12pt para cuerpo
Márgenes estándar
Tabla de contenido si tiene múltiples secciones
Tablas con encabezados en negrita si aplica

Tipo E — Otro
Genera el archivo con el contenido o estructura más apropiada según la descripción.
PASO 5 — Exportación en ZIP
Empaqueta todos los archivos en un único archivo ZIP descargable respetando exactamente
la estructura de rutas indicada por los marcadores.
El ZIP debe incluir:

Archivos de código con únicamente los errores de compilación corregidos
Archivos de configuración y documentación sin cambios
Archivos nuevos creados para resolver dependencias de compilación faltantes
Archivos Excel y Word generados desde descripción

IMPORTANTE: El ZIP debe estar listo para descargar al finalizar. No preguntes si el usuario
quiere generarlo. Simplemente genera el archivo y proporciona el enlace de descarga; No debes desplegar en el chat el resumen de lo que arreglaste al Zip, solo entregalo.

REGLAS IMPORTANTES

No omitas ningún archivo aunque no tenga errores ni modificaciones
Respeta los nombres y rutas exactas indicadas por los marcadores
Si un archivo no tiene marcador claro, infiere el nombre desde su contenido
Si la cadena contiene solo documentación, placeholders o binarios fake, NO la reproduzcas:
aplicá PASO 0 (materializar el proyecto del briefing). Reproducir la carcasa es un fallo.
No agregues texto después del enlace de descarga del ZIP
No preguntes si el usuario quiere el ZIP: simplemente generalo siempre
Si detectas que falta un archivo de configuración necesario para compilar
(pom.xml, package.json, requirements.txt, build.gradle, etc.), créalo e inclúyelo
inferiendo su contenido desde los imports y frameworks detectados en el código
Nunca corrijas problemas 🟡 aunque parezcan obvios o fáciles de mejorar.
El participante que recibirá este proyecto los debe encontrar y resolver él mismo.


INPUT
Aquí está la cadena con los archivos:

// === ARCHIVO: pom.xml ===
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.pragma</groupId>
    <artifactId>reconciliation</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>reconciliation</name>
    <description>Sistema de conciliacion bancaria en tiempo real</description>
    
    <properties>
        <java.version>21</java.version>
        <reactor.version>3.6.0</reactor.version>
        <postgresql.version>42.7.0</postgresql.version>
        <lombok.version>1.18.30</lombok.version>
        <micrometer.version>1.13.0</micrometer.version>
        <testcontainers.version>1.19.7</testcontainers.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot WebFlux para programacion reactiva -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        
        <!-- Kafka para procesamiento de eventos -->
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
            <version>3.4.0</version>
        </dependency>
        
        <!-- Reactor Core para programacion reactiva -->
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-core</artifactId>
            <version>${reactor.version}</version>
        </dependency>
        
        <!-- PostgreSQL como base de datos -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>${postgresql.version}</version>
        </dependency>
        
        <!-- Spring Data JPA para persistencia -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <!-- Lombok para reduccion de boilerplate -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>
        
        <!-- Actuator para monitoreo y metricas -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        
        <!-- Micrometer Prometheus para exportacion de metricas -->
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
            <version>${micrometer.version}</version>
        </dependency>
        
        <!-- Testcontainers para pruebas de integracion -->
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>kafka</artifactId>
            <version>${testcontainers.version}</version>
            <scope>test</scope>
        </dependency>
        
        <!-- Spring Boot Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <!-- Spring Kafka Test -->
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version>${lombok.version}</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

// === ARCHIVO: src/main/java/com/pragma/reconciliation/ReconciliationApplication.java ===
package com.pragma.reconciliation;

import com.pragma.reconciliation.application.commands.ReconcileMovementCommand;
import com.pragma.reconciliation.application.events.MovementReconciledEvent;
import com.pragma.reconciliation.domain.services.ReconciliationService;
import com.pragma.reconciliation.infrastructure.adapters.ReconciliationKafkaConsumer;
import com.pragma.reconciliation.infrastructure.config.ReconciliationConfig;
import com.pragma.reconciliation.infrastructure.messaging.KafkaConfig;
import com.pragma.reconciliation.infrastructure.monitoring.LagMonitor;
import com.pragma.reconciliation.infrastructure.persistence.ReconciliationJpaRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Punto de entrada del sistema de conciliacion bancaria en tiempo real.
 * 
 * Esta aplicacion consume streams de movimientos desde tres fuentes:
 * - Core bancario
 * - Gateway de pagos
 * - Sistema de liquidacion
 * 
 * Cada movimiento se reconcilia contra las tres fuentes con tolerancia
 * a mensajes fuera de orden y llegadas duplicadas.
 */
@SpringBootApplication
@EnableConfigurationProperties(ReconciliationConfig.class)
@Import({
    ReconciliationService.class,
    ReconciliationJpaRepository.class,
    ReconciliationKafkaConsumer.class,
    KafkaConfig.class,
    LagMonitor.class
})
public class ReconciliationApplication {
    
    private static final Logger log = LoggerFactory.getLogger(ReconciliationApplication.class);
    
    private final ReconciliationService reconciliationService;
    private final ReconciliationJpaRepository repository;
    private final LagMonitor lagMonitor;
    private final MeterRegistry meterRegistry;
    private final ReconciliationConfig config;
    
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicLong processedCount = new AtomicLong(0);
    private final AtomicLong failedCount = new AtomicLong(0);
    
    public ReconciliationApplication(
            ReconciliationService reconciliationService,
            ReconciliationJpaRepository repository,
            LagMonitor lagMonitor,
            MeterRegistry meterRegistry,
            ReconciliationConfig config) {
        this.reconciliationService = reconciliationService;
        this.repository = repository;
        this.lagMonitor = lagMonitor;
        this.meterRegistry = meterRegistry;
        this.config = config;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(ReconciliationApplication.class, args);
    }
    
    @Bean
    public Timer reconciliationTimer(MeterRegistry registry) {
        return Timer.builder("reconciliation.processing.time")
                .description("Tiempo total de procesamiento de conciliacion")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);
    }
    
    @Bean
    public Flux<MovementReconciledEvent> reconciliationStream() {
        return Flux.defer(() -> {
            if (!running.compareAndSet(false, true)) {
                log.warn("El stream de conciliacion ya esta en ejecucion");
                return Flux.empty();
            }
            
            log.info("Iniciando stream de conciliacion con ventana de {} minutos", 
                    config.getMatchingWindowMinutes());
            
            return repository.findPendingMovements(
                    Instant.now().minus(Duration.ofMinutes(config.getMatchingWindowMinutes()))
            )
            .flatMap(command -> reconciliationService.reconcile(command)
                    .doOnSuccess(event -> {
                        processedCount.incrementAndGet();
                        log.debug("Movimiento conciliado: eventId={}, status={}", 
                                event.eventId(), event.status());
                    })
                    .doOnError(error -> {
                        failedCount.incrementAndGet();
                        log.error("Error al conciliar movimiento: eventId={}, error={}", 
                                command.getEventId(), error.getMessage());
                    })
                    .onErrorResume(error -> {
                        log.error("Reconciliacion fallida para eventId: {}", 
                                command.getEventId(), error);
                        return Mono.empty();
                    }))
            .subscribeOn(Schedulers.boundedElastic())
            .doFinally(signal -> {
                running.set(false);
                log.info("Stream de conciliacion detenido. Procesados: {}, Fallidos: {}", 
                        processedCount.get(), failedCount.get());
            });
        }).repeat().delayUntil(x -> Mono.delay(Duration.ofSeconds(config.getPollIntervalSeconds())));
    }
    
    @Bean
    public ReconciliationKafkaConsumer kafkaConsumer(
            ReconciliationService service,
            LagMonitor monitor) {
        return new ReconciliationKafkaConsumer(service, monitor);
    }
    
    /**
     * Endpoint de salud que verifica el estado del sistema de conciliacion.
     */
    public record HealthStatus(
            boolean running,
            long processedCount,
            long failedCount,
            Instant lastProcessedAt,
            long currentLagSeconds,
            boolean lagAlertActive
    ) {}
    
    public HealthStatus getHealthStatus() {
        return new HealthStatus(
                running.get(),
                processedCount.get(),
                failedCount.get(),
                repository.findLastProcessedTimestamp().orElse(null),
                lagMonitor.getCurrentLagSeconds(),
                lagMonitor.isAlertActive()
        );
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/domain/model/ReconciliationStatus.java ===
package com.pragma.reconciliation.domain.model;

public enum ReconciliationStatus {
    PENDING("Pendiente de procesamiento"),
    MATCHED("Movimiento conciliado exitosamente"),
    MISMATCHED("Discrepancia detectada entre fuentes"),
    MANUAL("Requiere revisión manual del equipo de operaciones");

    private final String description;

    ReconciliationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canTransitionTo(ReconciliationStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == MATCHED || newStatus == MISMATCHED || newStatus == MANUAL;
            case MATCHED -> false;
            case MISMATCHED -> newStatus == MANUAL || newStatus == MATCHED;
            case MANUAL -> false;
        };
    }

    public boolean requiresManualIntervention() {
        return this == MANUAL || this == MISMATCHED;
    }

    public boolean isTerminal() {
        return this == MATCHED || this == MANUAL;
    }

    public boolean requiresProcessing() {
        return this == PENDING;
    }

    public static ReconciliationStatus fromString(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("El estado no puede ser nulo o vacío");
        }
        try {
            return valueOf(status.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Estado de reconciliación inválido: " + status + ". Estados válidos: " + 
                java.util.Arrays.toString(values())
            );
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/domain/model/Reconciliation.java ===
package com.pragma.reconciliation.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Reconciliation {
    private final UUID id;
    private final String accountId;
    private final String movementId;
    private final String eventId;
    private final Integer version;
    private final BigDecimal amount;
    private final String currency;
    private final LocalDate transactionDate;
    private final ReconciliationStatus status;
    private final String source;
    private final Instant createdAt;
    private final Instant processedAt;
    private final String mismatchReason;
    private final boolean idempotencyProcessed;

    public Reconciliation(
            UUID id,
            String accountId,
            String movementId,
            String eventId,
            Integer version,
            BigDecimal amount,
            String currency,
            LocalDate transactionDate,
            ReconciliationStatus status,
            String source,
            Instant createdAt,
            Instant processedAt,
            String mismatchReason,
            boolean idempotencyProcessed) {
        
        this.id = Objects.requireNonNull(id, "El ID de reconciliación no puede ser nulo");
        this.accountId = validateAccountId(accountId);
        this.movementId = validateMovementId(movementId);
        this.eventId = validateEventId(eventId);
        this.version = validateVersion(version);
        this.amount = validateAmount(amount);
        this.currency = validateCurrency(currency);
        this.transactionDate = Objects.requireNonNull(transactionDate, "La fecha de transacción no puede ser nula");
        this.status = Objects.requireNonNull(status, "El estado de reconciliación no puede ser nulo");
        this.source = validateSource(source);
        this.createdAt = Objects.requireNonNull(createdAt, "La fecha de creación no puede ser nula");
        this.processedAt = processedAt;
        this.mismatchReason = mismatchReason;
        this.idempotencyProcessed = idempotencyProcessed;
    }

    private String validateAccountId(String accountId) {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("El ID de cuenta no puede ser nulo o vacío");
        }
        if (accountId.length() > 50) {
            throw new IllegalArgumentException("El ID de cuenta no puede exceder 50 caracteres");
        }
        return accountId;
    }

    private String validateMovementId(String movementId) {
        if (movementId == null || movementId.isBlank()) {
            throw new IllegalArgumentException("El ID de movimiento no puede ser nulo o vacío");
        }
        return movementId;
    }

    private String validateEventId(String eventId) {
        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("El eventId no puede ser nulo o vacío");
        }
        return eventId;
    }

    private Integer validateVersion(Integer version) {
        if (version == null || version < 0) {
            throw new IllegalArgumentException("La versión debe ser mayor o igual a 0");
        }
        return version;
    }

    private BigDecimal validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
        return amount.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private String validateCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("La moneda no puede ser nula o vacía");
        }
        if (!currency.matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException("La moneda debe ser un código ISO de 3 letras mayúsculas");
        }
        return currency;
    }

    private String validateSource(String source) {
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("La fuente del movimiento no puede ser nula o vacía");
        }
        return source;
    }

    public UUID getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getMovementId() {
        return movementId;
    }

    public String getEventId() {
        return eventId;
    }

    public Integer getVersion() {
        return version;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public ReconciliationStatus getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public String getMismatchReason() {
        return mismatchReason;
    }

    public boolean isIdempotencyProcessed() {
        return idempotencyProcessed;
    }

    public boolean matches(Reconciliation other) {
        if (other == null) return false;
        return this.accountId.equals(other.accountId)
            && this.amount.compareTo(other.amount) == 0
            && this.currency.equals(other.currency)
            && this.transactionDate.equals(other.transactionDate);
    }

    public boolean isDuplicateOf(Reconciliation other) {
        if (other == null) return false;
        return this.eventId.equals(other.eventId) 
            && this.version.equals(other.version)
            && this.movementId.equals(other.movementId);
    }

    public boolean isOutOfOrder(Instant currentProcessingTime) {
        if (createdAt == null || currentProcessingTime == null) return false;
        long minutesDifference = java.time.Duration.between(createdAt, currentProcessingTime).toMinutes();
        return minutesDifference > 5;
    }

    public Reconciliation withStatus(ReconciliationStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                "Transición de estado inválida: " + this.status + " -> " + newStatus
            );
        }
        return new Reconciliation(
            this.id, this.accountId, this.movementId, this.eventId, this.version,
            this.amount, this.currency, this.transactionDate, newStatus, this.source,
            this.createdAt, Instant.now(), this.mismatchReason, this.idempotencyProcessed
        );
    }

    public Reconciliation withMismatchReason(String reason) {
        return new Reconciliation(
            this.id, this.accountId, this.movementId, this.eventId, this.version,
            this.amount, this.currency, this.transactionDate, 
            this.status == PENDING ? MISMATCHED : this.status, 
            this.source, this.createdAt, Instant.now(), reason, this.idempotencyProcessed
        );
    }

    public Reconciliation markAsProcessed() {
        return new Reconciliation(
            this.id, this.accountId, this.movementId, this.eventId, this.version,
            this.amount, this.currency, this.transactionDate, this.status,
            this.source, this.createdAt, Instant.now(), this.mismatchReason, true
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reconciliation that = (Reconciliation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reconciliation{" +
                "id=" + id +
                ", accountId='" + accountId + '\'' +
                ", movementId='" + movementId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", version=" + version +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", transactionDate=" + transactionDate +
                ", status=" + status +
                ", source='" + source + '\'' +
                ", createdAt=" + createdAt +
                ", processedAt=" + processedAt +
                ", mismatchReason='" + mismatchReason + '\'' +
                ", idempotencyProcessed=" + idempotencyProcessed +
                '}';
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/domain/ports/in/ReconciliationServicePort.java ===
package com.pragma.reconciliation.domain.ports.in;

import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import com.pragma.reconciliation.application.commands.ReconcileMovementCommand;
import com.pragma.reconciliation.application.events.MovementReconciledEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReconciliationServicePort {

    Mono<Reconciliation> reconcileMovement(ReconcileMovementCommand command);

    Mono<Reconciliation> getReconciliationById(UUID id);

    Flux<Reconciliation> getReconciliationsByAccountId(String accountId);

    Flux<Reconciliation> getReconciliationsByStatus(ReconciliationStatus status);

    Flux<Reconciliation> getReconciliationsByDateRange(Instant startDate, Instant endDate);

    Mono<Reconciliation> updateReconciliationStatus(UUID id, ReconciliationStatus newStatus);

    Mono<Reconciliation> markAsManualReview(UUID id, String reason);

    Flux<MovementReconciledEvent> processReconciliationBatch(List<ReconcileMovementCommand> commands);

    Mono<Boolean> isMovementProcessed(String eventId, Integer version);

    Mono<Reconciliation> reprocessReconciliation(UUID id);

    Flux<Reconciliation> getPendingReconciliationsOlderThan(Instant threshold);
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/domain/ports/out/ReconciliationPersistencePort.java ===
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

// === ARCHIVO: src/main/java/com/pragma/reconciliation/application/commands/ReconcileMovementCommand.java ===
package com.pragma.reconciliation.application.commands;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReconcileMovementCommand(
    UUID id,
    String accountId,
    String movementId,
    String eventId,
    Integer version,
    BigDecimal amount,
    String currency,
    Instant transactionDate,
    String source,
    String referenceId,
    String description,
    Instant receivedAt
) {
    public ReconcileMovementCommand {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("accountId no puede ser nulo o vacío");
        }
        if (movementId == null || movementId.isBlank()) {
            throw new IllegalArgumentException("movementId no puede ser nulo o vacío");
        }
        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("eventId no puede ser nulo o vacío");
        }
        if (version == null || version < 0) {
            throw new IllegalArgumentException("version debe ser mayor o igual a 0");
        }
        if (amount == null) {
            throw new IllegalArgumentException("amount no puede ser nulo");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("currency no puede ser nulo o vacío");
        }
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("source no puede ser nulo o vacío");
        }
    }

    public boolean isFromCoreBanking() {
        return "CORE_BANKING".equalsIgnoreCase(source);
    }

    public boolean isFromPaymentGateway() {
        return "PAYMENT_GATEWAY".equalsIgnoreCase(source);
    }

    public boolean isFromSettlementSystem() {
        return "SETTLEMENT_SYSTEM".equalsIgnoreCase(source);
    }

    public boolean isFromValidSource() {
        return isFromCoreBanking() || isFromPaymentGateway() || isFromSettlementSystem();
    }

    public String getUniqueEventKey() {
        return eventId + "|" + version;
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java ===
package com.pragma.reconciliation.domain.services;

import com.pragma.reconciliation.application.commands.ReconcileMovementCommand;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import com.pragma.reconciliation.domain.ports.in.ReconciliationServicePort;
import com.pragma.reconciliation.domain.ports.out.ReconciliationPersistencePort;
import com.pragma.reconciliation.application.events.MovementReconciledEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ReconciliationService implements ReconciliationServicePort {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationService.class);
    private static final Duration DEFAULT_MATCHING_WINDOW = Duration.ofMinutes(5);
    private static final Duration OUT_OF_ORDER_THRESHOLD = Duration.ofMinutes(10);

    private final ReconciliationPersistencePort persistencePort;

    public ReconciliationService(ReconciliationPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Mono<Reconciliation> reconcileMovement(ReconcileMovementCommand command) {
        log.info("Iniciando reconciliación para eventId={}, version={}, source={}",
                command.eventId(), command.version(), command.source());

        if (!command.isFromValidSource()) {
            log.warn("Fuente de movimiento inválida: {}", command.source());
            return Mono.error(new IllegalArgumentException("Fuente de movimiento inválida: " + command.source()));
        }

        return persistencePort.existsByEventIdAndVersion(command.eventId(), command.version())
                .flatMap(exists -> {
                    if (exists) {
                        log.info("Movimiento duplicado detectado, eventId={}, version={}",
                                command.eventId(), command.version());
                        return findExistingAndMarkAsProcessed(command.eventId(), command.version());
                    }
                    return processNewMovement(command);
                })
                .doOnSuccess(reconciliation -> log.info("Reconciliación completada: id={}, status={}",
                        reconciliation.getId(), reconciliation.getStatus()))
                .doOnError(error -> log.error("Error en reconciliación: {}", error.getMessage()));
    }

    private Mono<Reconciliation> findExistingAndMarkAsProcessed(String eventId, Integer version) {
        return persistencePort.findByEventIdAndVersion(eventId, version)
                .flatMap(existing -> {
                    if (existing.isIdempotencyProcessed()) {
                        log.debug("Movimiento ya procesado completamente, retornando existente: {}", existing.getId());
                        return Mono.just(existing);
                    }
                    return Mono.just(existing.markAsProcessed());
                });
    }

    private Mono<Reconciliation> processNewMovement(ReconcileMovementCommand command) {
        Reconciliation reconciliation = createReconciliationFromCommand(command);

        return persistencePort.save(reconciliation)
                .flatMap(saved -> performMatching(saved)
                        .flatMap(matched -> {
                            if (matched.getStatus() == ReconciliationStatus.MISMATCHED) {
                                return persistencePort.update(matched);
                            }
                            return Mono.just(matched);
                        })
                        .flatMap(updated -> publishReconciliationEvent(updated)
                                .thenReturn(updated)));
    }

    private Reconciliation createReconciliationFromCommand(ReconcileMovementCommand command) {
        return new Reconciliation(
                command.id() != null ? command.id() : UUID.randomUUID(),
                command.accountId(),
                command.movementId(),
                command.eventId(),
                command.version(),
                command.amount(),
                command.currency(),
                command.transactionDate(),
                ReconciliationStatus.PENDING,
                command.source(),
                Instant.now(),
                null,
                null,
                false
        );
    }

    private Mono<Reconciliation> performMatching(Reconciliation reconciliation) {
        Instant transactionTime = reconciliation.getTransactionDate() != null
                ? reconciliation.getTransactionDate().atStartOfDay().toInstant(ZoneOffset.UTC)
                : Instant.now();

        Instant windowStart = transactionTime.minus(DEFAULT_MATCHING_WINDOW);
        Instant windowEnd = transactionTime.plus(DEFAULT_MATCHING_WINDOW);

        log.debug("Ventana de matching para {}: {} a {}",
                reconciliation.getMovementId(), windowStart, windowEnd);

        return persistencePort.findByDateRange(windowStart, windowEnd)
                .filter(r -> r.getAccountId().equals(reconciliation.getAccountId()))
                .filter(r -> !r.getId().equals(reconciliation.getId()))
                .collectList()
                .flatMap(candidates -> {
                    if (candidates.isEmpty()) {
                        log.info("No se encontraron candidatos para matching, movimiento queda pendiente: {}",
                                reconciliation.getMovementId());
                        return Mono.just(reconciliation);
                    }
                    return findMatchingCandidate(reconciliation, candidates);
                });
    }

    private Mono<Reconciliation> findMatchingCandidate(Reconciliation reconciliation, List<Reconciliation> candidates) {
        BigDecimal tolerance = reconciliation.getAmount().multiply(BigDecimal.valueOf(0.01));

        return Flux.fromIterable(candidates)
                .filter(candidate -> hasMatchingSource(candidate, reconciliation))
                .filter(candidate -> isAmountWithinTolerance(reconciliation.getAmount(), candidate.getAmount(), tolerance))
                .next()
                .flatMap(matched -> {
                    log.info("Match encontrado para {} con {}", reconciliation.getMovementId(), matched.getMovementId());
                    return Mono.just(reconciliation.withStatus(ReconciliationStatus.MATCHED));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No se encontró match para {}, marcando como discrepancia", reconciliation.getMovementId());
                    String reason = String.format("No se encontró movimiento coincidente en ventana de %d minutos. Candidatos encontrados: %d",
                            DEFAULT_MATCHING_WINDOW.toMinutes(), candidates.size());
                    return Mono.just(reconciliation.withStatus(ReconciliationStatus.MISMATCHED)
                            .withMismatchReason(reason));
                }));
    }

    private boolean hasMatchingSource(Reconciliation existing, Reconciliation incoming) {
        return !existing.getSource().equals(incoming.getSource());
    }

    private boolean isAmountWithinTolerance(BigDecimal amount1, BigDecimal amount2, BigDecimal tolerance) {
        BigDecimal difference = amount1.subtract(amount2).abs();
        return difference.compareTo(tolerance) <= 0;
    }

    private Mono<Void> publishReconciliationEvent(Reconciliation reconciliation) {
        MovementReconciledEvent event = new MovementReconciledEvent(
                reconciliation.getId(),
                reconciliation.getAccountId(),
                reconciliation.getMovementId(),
                reconciliation.getEventId(),
                reconciliation.getVersion(),
                reconciliation.getAmount(),
                reconciliation.getCurrency(),
                reconciliation.getStatus(),
                reconciliation.getProcessedAt(),
                reconciliation.getMismatchReason()
        );
        log.info("Evento de reconciliación publicado: {}", event);
        return Mono.empty();
    }

    @Override
    public Mono<Reconciliation> getReconciliationById(UUID id) {
        return persistencePort.findById(id);
    }

    @Override
    public Flux<Reconciliation> getReconciliationsByAccountId(String accountId) {
        return persistencePort.findByAccountId(accountId);
    }

    @Override
    public Flux<Reconciliation> getReconciliationsByStatus(ReconciliationStatus status) {
        return persistencePort.findByStatus(status);
    }

    @Override
    public Flux<Reconciliation> getReconciliationsByDateRange(Instant startDate, Instant endDate) {
        return persistencePort.findByDateRange(startDate, endDate);
    }

    @Override
    public Mono<Reconciliation> updateReconciliationStatus(UUID id, ReconciliationStatus newStatus) {
        return persistencePort.findById(id)
                .flatMap(reconciliation -> {
                    if (!reconciliation.getStatus().canTransitionTo(newStatus)) {
                        return Mono.error(new IllegalStateException(
                                "Transición de estado inválida: " + reconciliation.getStatus() + " -> " + newStatus));
                    }
                    Reconciliation updated = reconciliation.withStatus(newStatus).markAsProcessed();
                    return persistencePort.update(updated);
                });
    }

    @Override
    public Mono<Reconciliation> markAsManualReview(UUID id, String reason) {
        return persistencePort.findById(id)
                .flatMap(reconciliation -> {
                    Reconciliation updated = reconciliation.withStatus(ReconciliationStatus.MANUAL)
                            .withMismatchReason(reason)
                            .markAsProcessed();
                    return persistencePort.update(updated);
                });
    }

    @Override
    public Flux<MovementReconciledEvent> processReconciliationBatch(List<ReconcileMovementCommand> commands) {
        log.info("Procesando lote de {} movimientos", commands.size());

        return Flux.fromIterable(commands)
                .flatMap(this::reconcileMovement)
                .map(this::toMovementReconciledEvent);
    }

    private MovementReconciledEvent toMovementReconciledEvent(Reconciliation reconciliation) {
        return new MovementReconciledEvent(
                reconciliation.getId(),
                reconciliation.getAccountId(),
                reconciliation.getMovementId(),
                reconciliation.getEventId(),
                reconciliation.getVersion(),
                reconciliation.getAmount(),
                reconciliation.getCurrency(),
                reconciliation.getStatus(),
                reconciliation.getProcessedAt(),
                reconciliation.getMismatchReason()
        );
    }

    @Override
    public Mono<Boolean> isMovementProcessed(String eventId, Integer version) {
        return persistencePort.existsByEventIdAndVersion(eventId, version);
    }

    @Override
    public Mono<Reconciliation> reprocessReconciliation(UUID id) {
        return persistencePort.findById(id)
                .flatMap(reconciliation -> {
                    if (reconciliation.getStatus() == ReconciliationStatus.MATCHED) {
                        log.info("Reconciliación {} ya está conciliada, no se reprocesa", id);
                        return Mono.just(reconciliation);
                    }
                    Reconciliation reset = reconciliation.withStatus(ReconciliationStatus.PENDING);
                    return persistencePort.update(reset)
                            .flatMap(this::performMatching)
                            .flatMap(matched -> persistencePort.update(matched));
                });
    }

    @Override
    public Flux<Reconciliation> getPendingReconciliationsOlderThan(Instant threshold) {
        return persistencePort.findPendingOlderThan(threshold);
    }

    private static class ZoneOffset {
        static java.time.ZoneOffset UTC = java.time.ZoneOffset.UTC;
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java ===
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

// === ARCHIVO: src/main/java/com/pragma/reconciliation/application/events/MovementReconciledEvent.java ===
package com.pragma.reconciliation.application.events;

import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record MovementReconciledEvent(
    UUID id,
    String accountId,
    String movementId,
    String eventId,
    Integer version,
    BigDecimal amount,
    String currency,
    LocalDate transactionDate,
    ReconciliationStatus status,
    String source,
    Instant createdAt,
    Instant processedAt,
    String mismatchReason,
    boolean idempotencyProcessed
) {
    public static MovementReconciledEvent fromReconciliation(com.pragma.reconciliation.domain.model.Reconciliation reconciliation) {
        return new MovementReconciledEvent(
            reconciliation.getId(),
            reconciliation.getAccountId(),
            reconciliation.getMovementId(),
            reconciliation.getEventId(),
            reconciliation.getVersion(),
            reconciliation.getAmount(),
            reconciliation.getCurrency(),
            reconciliation.getTransactionDate(),
            reconciliation.getStatus(),
            reconciliation.getSource(),
            reconciliation.getCreatedAt(),
            reconciliation.getProcessedAt(),
            reconciliation.getMismatchReason(),
            reconciliation.isIdempotencyProcessed()
        );
    }

    public boolean isSuccessful() {
        return status == ReconciliationStatus.MATCHED;
    }

    public boolean requiresManualIntervention() {
        return status == ReconciliationStatus.MANUAL;
    }

    public boolean hasMismatch() {
        return status == ReconciliationStatus.MISMATCHED;
    }

    public String getEventKey() {
        return eventId + "_v" + version;
    }

    public Instant getProcessingLatency() {
        if (createdAt != null && processedAt != null) {
            return processedAt.minusMillis(createdAt.toEpochMilli());
        }
        return Instant.EPOCH;
    }

    public long getLatencySeconds() {
        if (createdAt != null && processedAt != null) {
            return java.time.Duration.between(createdAt, processedAt).getSeconds();
        }
        return 0L;
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumer.java ===
package com.pragma.reconciliation.infrastructure.adapters;

import com.pragma.reconciliation.application.commands.ReconcileMovementCommand;
import com.pragma.reconciliation.application.events.MovementReconciledEvent;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.ports.in.ReconciliationServicePort;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ReconciliationKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationKafkaConsumer.class);
    private static final String CONSUMER_GROUP = "reconciliation-processor";
    private static final int BATCH_SIZE = 100;
    private static final Duration PROCESSING_TIMEOUT = Duration.ofSeconds(30);

    private final ReconciliationServicePort reconciliationService;
    private final MeterRegistry meterRegistry;
    private final Counter messagesReceivedCounter;
    private final Counter messagesProcessedCounter;
    private final Counter messagesFailedCounter;
    private final Timer processingTimer;

    public ReconciliationKafkaConsumer(
            ReconciliationServicePort reconciliationService,
            MeterRegistry meterRegistry
    ) {
        this.reconciliationService = reconciliationService;
        this.meterRegistry = meterRegistry;
        this.messagesReceivedCounter = Counter.builder("kafka.messages.received")
                .description("Total de mensajes recibidos desde Kafka")
                .register(meterRegistry);
        this.messagesProcessedCounter = Counter.builder("kafka.messages.processed")
                .description("Total de mensajes procesados exitosamente")
                .register(meterRegistry);
        this.messagesFailedCounter = Counter.builder("kafka.messages.failed")
                .description("Total de mensajes que fallaron en procesamiento")
                .register(meterRegistry);
        this.processingTimer = Timer.builder("kafka.messages.processing.time")
                .description("Tiempo de procesamiento de mensajes")
                .register(meterRegistry);
    }

    @KafkaListener(
            topics = "${reconciliation.kafka.topics.movements:banking.movements}",
            groupId = "${reconciliation.kafka.consumer.group:reconciliation-processor}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeMovements(List<ReconcileMovementCommand> commands, Acknowledgment ack) {
        log.info("Recibidos {} movimientos para conciliación", commands.size());
        messagesReceivedCounter.increment(commands.size());

        AtomicInteger processedCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);

        Flux.fromIterable(commands)
                .flatMap(this::processMovement, BATCH_SIZE)
                .doOnNext(result -> {
                    if (result.isPresent()) {
                        processedCount.incrementAndGet();
                        messagesProcessedCounter.increment();
                        log.debug("Movimiento procesado exitosamente: {}", result.get().getMovementId());
                    } else {
                        failedCount.incrementAndGet();
                        messagesFailedCounter.increment();
                        log.warn("Movimiento no procesado correctamente");
                    }
                })
                .doOnError(error -> {
                    log.error("Error en procesamiento por lotes: {}", error.getMessage(), error);
                    messagesFailedCounter.increment(commands.size());
                })
                .timeout(PROCESSING_TIMEOUT)
                .doFinally(signal -> {
                    log.info("Lote procesado - Exitosos: {}, Fallidos: {}", 
                            processedCount.get(), failedCount.get());
                    ack.acknowledge();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    private Mono<Reconciliation> processMovement(ReconcileMovementCommand command) {
        return Timer.resource(processingTimer, () -> 
            reconciliationService.reconcileMovement(command)
                .doOnSuccess(reconciliation -> {
                    if (reconciliation.isIdempotencyProcessed()) {
                        log.info("Movimiento ya procesado (idempotente): eventId={}, version={}",
                                command.eventId(), command.version());
                    }
                })
                .doOnError(error -> {
                    log.error("Error procesando movimiento: eventId={}, error={}",
                            command.eventId(), error.getMessage(), error);
                })
        );
    }

    @KafkaListener(
            topics = "${reconciliation.kafka.topics.reprocess:banking.movements.reprocess}",
            groupId = "${reconciliation.kafka.consumer.group:reconciliation-processor}-reprocess",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeReprocessRequests(ReconcileMovementCommand command, Acknowledgment ack) {
        log.info("Recibido request de reprocesamiento para eventId={}, version={}",
                command.eventId(), command.version());

        reconciliationService.reconcileMovement(command)
                .doOnSuccess(reconciliation -> {
                    log.info("Reprocesamiento completado: id={}, status={}",
                            reconciliation.getId(), reconciliation.getStatus());
                    ack.acknowledge();
                })
                .doOnError(error -> {
                    log.error("Error en reprocesamiento: eventId={}, error={}",
                            command.eventId(), error.getMessage(), error);
                    ack.acknowledge();
                })
                .subscribe();
    }

    public Flux<MovementReconciledEvent> consumeMovementsStream() {
        return Flux.empty();
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/messaging/KafkaConfig.java ===
package com.pragma.reconciliation.infrastructure.messaging;

import com.pragma.reconciliation.application.commands.ReconcileMovementCommand;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.ExponentialBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:reconciliation-processor}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset:earliest}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.enable-auto-commit:false}")
    private boolean enableAutoCommit;

    @Value("${spring.kafka.consumer.max-poll-records:100}")
    private int maxPollRecords;

    @Value("${spring.kafka.consumer.max-poll-interval-ms:300000}")
    private int maxPollIntervalMs;

    @Value("${reconciliation.kafka.retry.max-attempts:3}")
    private int maxRetryAttempts;

    @Value("${reconciliation.kafka.retry.initial-interval-ms:1000}")
    private long initialIntervalMs;

    @Value("${reconciliation.kafka.retry.multiplier:2.0}")
    private double multiplier;

    @Value("${reconciliation.kafka.retry.max-interval-ms:10000}")
    private long maxIntervalMs;

    @Value("${reconciliation.kafka.topics.movements:banking.movements}")
    private String movementsTopic;

    @Value("${reconciliation.kafka.topics.reprocess:banking.movements.reprocess}")
    private String reprocessTopic;

    @Value("${reconciliation.kafka.topics.reconciled:banking.movements.reconciled}")
    private String reconciledTopic;

    @Value("${reconciliation.kafka.topics.dlq:banking.movements.dlq}")
    private String dlqTopic;

    @Bean
    public ConsumerFactory<String, ReconcileMovementCommand> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, 30000);

        JsonDeserializer<ReconcileMovementCommand> deserializer = new JsonDeserializer<>(ReconcileMovementCommand.class);
        deserializer.addTrustedPackages("com.pragma.reconciliation.application.commands");
        deserializer.setUseTypeMapperForKey(true);
        deserializer.setTypeResolver(null);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean("kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, ReconcileMovementCommand> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ReconcileMovementCommand> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setSyncCommits(true);
        factory.setCommonErrorHandler(createErrorHandler());
        factory.setRecordFilterStrategy(record -> {
            if (record.value() == null) {
                log.warn("Mensaje nulo recibido, filtrando");
                return true;
            }
            return false;
        });
        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        return createErrorHandler();
    }

    private DefaultErrorHandler createErrorHandler() {
        ExponentialBackOff backOff = new ExponentialBackOff(initialIntervalMs, multiplier);
        backOff.setMaxInterval(maxIntervalMs);
        backOff.setMaxElapsedTime(maxRetryAttempts * (int) maxIntervalMs);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                (record, exception) -> {
                    log.error("Error después de reintentos, enviando a DLQ: topic={}, key={}, error={}",
                            record.topic(), record.key(), exception.getMessage(), exception);
                },
                backOff
        );

        errorHandler.addNotRetryableExceptions(
                IllegalArgumentException.class,
                NullPointerException.class
        );

        return errorHandler;
    }

    @Bean
    public Map<String, Object> kafkaProducerProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("acks", "all");
        props.put("retries", maxRetryAttempts);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.springframework.kafka.support.serializer.JsonSerializer");
        return props;
    }

    public String getMovementsTopic() {
        return movementsTopic;
    }

    public String getReprocessTopic() {
        return reprocessTopic;
    }

    public String getReconciledTopic() {
        return reconciledTopic;
    }

    public String getDlqTopic() {
        return dlqTopic;
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitor.java ===
package com.pragma.reconciliation.infrastructure.monitoring;

import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import com.pragma.reconciliation.domain.ports.in.ReconciliationServicePort;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class LagMonitor {

    private static final Logger log = LoggerFactory.getLogger(LagMonitor.class);
    private static final Duration SLA_THRESHOLD = Duration.ofMinutes(5);
    private static final Duration CHECK_INTERVAL = Duration.ofSeconds(30);

    private final ReconciliationServicePort reconciliationService;
    private final MeterRegistry meterRegistry;
    private final AtomicLong currentLagSeconds;
    private final AtomicReference<Instant> lastAlertTime;
    private final AtomicLong pendingCount;
    private final Timer lagCheckTimer;

    public LagMonitor(ReconciliationServicePort reconciliationService, MeterRegistry meterRegistry) {
        this.reconciliationService = reconciliationService;
        this.meterRegistry = meterRegistry;
        this.currentLagSeconds = new AtomicLong(0);
        this.lastAlertTime = new AtomicReference<>(Instant.EPOCH);
        this.pendingCount = new AtomicLong(0);

        registerMetrics();
        this.lagCheckTimer = Timer.builder("reconciliation.lag.check")
                .description("Tiempo de verificación del lag de conciliación")
                .register(meterRegistry);
    }

    private void registerMetrics() {
        Gauge.builder("reconciliation.lag.seconds", currentLagSeconds, AtomicLong::get)
                .description("Lag actual de conciliación en segundos")
                .tag("sla_threshold", String.valueOf(SLA_THRESHOLD.toSeconds()))
                .register(meterRegistry);

        Gauge.builder("reconciliation.pending.count", pendingCount, AtomicLong::get)
                .description("Cantidad de conciliaciones pendientes")
                .register(meterRegistry);

        Gauge.builder("reconciliation.lag.alert.active", this, LagMonitor::isAlertActive)
                .description("Indica si hay una alerta de lag activa")
                .register(meterRegistry);
    }

    public int isAlertActive() {
        return (currentLagSeconds.get() > SLA_THRESHOLD.toSeconds()) ? 1 : 0;
    }

    public Mono<Void> checkAndAlert() {
        return Timer.resource(lagCheckTimer)
                .flatMap(timer -> {
                    log.debug("Iniciando verificación de lag de conciliación");
                    return calculateCurrentLag()
                            .flatMap(this::updateMetricsAndCheckAlert)
                            .doOnSuccess(v -> log.debug("Verificación de lag completada, lag actual: {} segundos", 
                                    currentLagSeconds.get()))
                            .doOnError(e -> log.error("Error al verificar lag de conciliación", e));
                });
    }

    private Mono<Long> calculateCurrentLag() {
        Instant threshold = Instant.now().minus(CHECK_INTERVAL.multipliedBy(2));
        
        return reconciliationService.getReconciliationsByStatus(ReconciliationStatus.PENDING)
                .take(1)
                .switchIfEmpty(Flux.empty().then(Mono.just(Instant.now())))
                .flatMap(reconciliation -> {
                    Instant createdAt = reconciliation.getCreatedAt();
                    long lagSeconds = Duration.between(createdAt, Instant.now()).getSeconds();
                    return Mono.just(lagSeconds);
                })
                .defaultIfEmpty(0L)
                .single();
    }

    private Mono<Void> updateMetricsAndCheckAlert(Long lagSeconds) {
        currentLagSeconds.set(lagSeconds);
        
        return reconciliationService.getReconciliationsByStatus(ReconciliationStatus.PENDING)
                .count()
                .doOnNext(count -> pendingCount.set(count))
                .flatMap(count -> {
                    if (lagSeconds > SLA_THRESHOLD.toSeconds()) {
                        return handleLagAlert(lagSeconds, count);
                    }
                    return Mono.empty();
                });
    }

    private Mono<Void> handleLagAlert(Long lagSeconds, Long pendingCount) {
        Instant now = Instant.now();
        Instant lastAlert = lastAlertTime.get();
        
        if (Duration.between(lastAlert, now).compareTo(CHECK_INTERVAL) > 0) {
            if (lastAlertTime.compareAndSet(lastAlert, now)) {
                log.warn("ALERTA: Lag de conciliación excede el SLA de {} minutos. " +
                        "Lag actual: {} segundos, Movimientos pendientes: {}", 
                        SLA_THRESHOLD.toMinutes(), lagSeconds, pendingCount);
                
                emitAlertMetric(lagSeconds, pendingCount);
                return Mono.empty();
            }
        }
        return Mono.empty();
    }

    private void emitAlertMetric(Long lagSeconds, Long pendingCount) {
        meterRegistry.counter("reconciliation.lag.alerts.triggered",
                "reason", "sla_exceeded")
                .increment();
        
        log.error("MÉTRICAS DE ALERTA - Lag: {}s, Pendientes: {}, Umbral SLA: {}s",
                lagSeconds, pendingCount, SLA_THRESHOLD.toSeconds());
    }

    public Mono<Long> getCurrentLagSeconds() {
        return calculateCurrentLag();
    }

    public long getSlaThresholdSeconds() {
        return SLA_THRESHOLD.toSeconds();
    }

    public boolean isLagExceedingSla() {
        return currentLagSeconds.get() > SLA_THRESHOLD.toSeconds();
    }

    public Mono<Long> getPendingCount() {
        return reconciliationService.getReconciliationsByStatus(ReconciliationStatus.PENDING)
                .count();
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/config/ReconciliationConfig.java ===
package com.pragma.reconciliation.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "reconciliation")
public class ReconciliationConfig {

    private MatchingWindow matchingWindow = new MatchingWindow();
    private Tolerances tolerances = new Tolerances();
    private Processing processing = new Processing();
    private Alerts alerts = new Alerts();

    public MatchingWindow getMatchingWindow() {
        return matchingWindow;
    }

    public void setMatchingWindow(MatchingWindow matchingWindow) {
        this.matchingWindow = matchingWindow;
    }

    public Tolerances getTolerances() {
        return tolerances;
    }

    public void setTolerances(Tolerances tolerances) {
        this.tolerances = tolerances;
    }

    public Processing getProcessing() {
        return processing;
    }

    public void setProcessing(Processing processing) {
        this.processing = processing;
    }

    public Alerts getAlerts() {
        return alerts;
    }

    public void setAlerts(Alerts alerts) {
        this.alerts = alerts;
    }

    public static class MatchingWindow {
        private Duration defaultWindow = Duration.ofMinutes(5);
        private Duration maxWindow = Duration.ofHours(1);
        private Duration minWindow = Duration.ofMinutes(1);
        private String strategy = "SLIDING";

        public Duration getDefaultWindow() {
            return defaultWindow;
        }

        public void setDefaultWindow(Duration defaultWindow) {
            this.defaultWindow = defaultWindow;
        }

        public Duration getMaxWindow() {
            return maxWindow;
        }

        public void setMaxWindow(Duration maxWindow) {
            this.maxWindow = maxWindow;
        }

        public Duration getMinWindow() {
            return minWindow;
        }

        public void setMinWindow(Duration minWindow) {
            this.minWindow = minWindow;
        }

        public String getStrategy() {
            return strategy;
        }

        public void setStrategy(String strategy) {
            this.strategy = strategy;
        }
    }

    public static class Tolerances {
        private AmountTolerance amount = new AmountTolerance();
        private TimeTolerance time = new TimeTolerance();

        public AmountTolerance getAmount() {
            return amount;
        }

        public void setAmount(AmountTolerance amount) {
            this.amount = amount;
        }

        public TimeTolerance getTime() {
            return time;
        }

        public void setTime(TimeTolerance time) {
            this.time = time;
        }

        public static class AmountTolerance {
            private double absoluteThreshold = 0.01;
            private double percentageThreshold = 0.001;
            private String currency = "USD";

            public double getAbsoluteThreshold() {
                return absoluteThreshold;
            }

            public void setAbsoluteThreshold(double absoluteThreshold) {
                this.absoluteThreshold = absoluteThreshold;
            }

            public double getPercentageThreshold() {
                return percentageThreshold;
            }

            public void setPercentageThreshold(double percentageThreshold) {
                this.percentageThreshold = percentageThreshold;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }
        }

        public static class TimeTolerance {
            private Duration maxOutOfOrder = Duration.ofMinutes(10);
            private Duration lateArrivalWindow = Duration.ofHours(24);
            private boolean allowOutOfOrder = true;

            public Duration getMaxOutOfOrder() {
                return maxOutOfOrder;
            }

            public void setMaxOutOfOrder(Duration maxOutOfOrder) {
                this.maxOutOfOrder = maxOutOfOrder;
            }

            public Duration getLateArrivalWindow() {
                return lateArrivalWindow;
            }

            public void setLateArrivalWindow(Duration lateArrivalWindow) {
                this.lateArrivalWindow = lateArrivalWindow;
            }

            public boolean isAllowOutOfOrder() {
                return allowOutOfOrder;
            }

            public void setAllowOutOfOrder(boolean allowOutOfOrder) {
                this.allowOutOfOrder = allowOutOfOrder;
            }
        }
    }

    public static class Processing {
        private int batchSize = 100;
        private Duration batchTimeout = Duration.ofSeconds(30);
        private int maxRetries = 3;
        private Duration retryDelay = Duration.ofSeconds(5);
        private String reprocessStrategy = "SNAPSHOT";

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }

        public Duration getBatchTimeout() {
            return batchTimeout;
        }

        public void setBatchTimeout(Duration batchTimeout) {
            this.batchTimeout = batchTimeout;
        }

        public int getMaxRetries() {
            return maxRetries;
        }

        public void setMaxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
        }

        public Duration getRetryDelay() {
            return retryDelay;
        }

        public void setRetryDelay(Duration retryDelay) {
            this.retryDelay = retryDelay;
        }

        public String getReprocessStrategy() {
            return reprocessStrategy;
        }

        public void setReprocessStrategy(String reprocessStrategy) {
            this.reprocessStrategy = reprocessStrategy;
        }
    }

    public static class Alerts {
        private Duration slaThreshold = Duration.ofMinutes(5);
        private Duration alertInterval = Duration.ofMinutes(1);
        private boolean enabled = true;

        public Duration getSlaThreshold() {
            return slaThreshold;
        }

        public void setSlaThreshold(Duration slaThreshold) {
            this.slaThreshold = slaThreshold;
        }

        public Duration getAlertInterval() {
            return alertInterval;
        }

        public void setAlertInterval(Duration alertInterval) {
            this.alertInterval = alertInterval;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}

// === ARCHIVO: src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java ===
package com.pragma.reconciliation.domain.services;

import com.pragma.reconciliation.application.commands.ReconcileMovementCommand;
import com.pragma.reconciliation.application.events.MovementReconciledEvent;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import com.pragma.reconciliation.domain.ports.out.ReconciliationPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

// === ARCHIVO: src/test/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumerTest.java ===
package com.pragma.reconciliation.infrastructure.adapters;

import com.pragma.reconciliation.application.commands.ReconcileMovementCommand;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import com.pragma.reconciliation.domain.ports.in.ReconciliationServicePort;
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
    private Acknowledgment acknowledgment;

    private ReconciliationKafkaConsumer kafkaConsumer;

    @BeforeEach
    void setUp() {
        kafkaConsumer = new ReconciliationKafkaConsumer(reconciliationServicePort);
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

// === ARCHIVO: src/test/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitorTest.java ===
package com.pragma.reconciliation.infrastructure.monitoring;

import com.pragma.reconciliation.infrastructure.config.ReconciliationConfig;
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

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

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
    private ReconciliationConfig config;

    @Mock
    private Counter lagAlertCounter;

    @Mock
    private Timer processingTimer;

    private LagMonitor lagMonitor;

    @BeforeEach
    void setUp() {
        when(config.getSlaThresholdMinutes()).thenReturn(5);
        when(config.getAlertCheckIntervalSeconds()).thenReturn(30);
        when(meterRegistry.counter(anyString())).thenReturn(lagAlertCounter);
        when(meterRegistry.timer(anyString())).thenReturn(processingTimer);

        lagMonitor = new LagMonitor(meterRegistry, config);
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
                        assertThat(lag.getSeconds()).isLessThan(300);
                        assertThat(lag.isAlertTriggered()).isFalse();
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
                        assertThat(lag.getSeconds()).isGreaterThan(300);
                        assertThat(lag.isAlertTriggered()).isTrue();
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

            verify(meterRegistry).counter("reconciliation.lag.alerts");
            verify(meterRegistry).timer("reconciliation.lag.measurement");
        }
    }

    @Nested
    @DisplayName("Escenario: Monitoreo continuo del lag")
    class MonitoreoContinuo {

        @Test
        @DisplayName("Debe iniciar flujo de monitoreo con intervalo configurado")
        void debeIniciarMonitoreo() {
            when(config.getAlertCheckIntervalSeconds()).thenReturn(1);

            LagMonitor monitor = new LagMonitor(meterRegistry, config);

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
                        assertThat(alert.getLagSeconds()).isGreaterThan(300);
                        assertThat(alert.getMessage()).contains("SUPERO");
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

        @Test
        @DisplayName("Debe permitir configurar umbral de SLA dinamicamente")
        void debePermitirConfiguracionDinamica() {
            when(config.getSlaThresholdMinutes()).thenReturn(10);

            LagMonitor monitor10 = new LagMonitor(meterRegistry, config);
            Instant eventoTime = Instant.now().minusSeconds(400);

            StepVerifier.create(monitor10.measureLag(eventoTime))
                    .assertNext(lag -> {
                        assertThat(lag.isAlertTriggered()).isFalse();
                    })
                    .verifyComplete();
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

// === ARCHIVO: docs/estados_reconciliacion.md ===
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

// === ARCHIVO: docs/ventana_matching.md ===
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

// === ARCHIVO: docs/idempotencia.md ===
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


// === ARCHIVO: docs/mecanismo_reprocesamiento.md ===
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

// === ARCHIVO: pom.xml ===
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.pragma</groupId>
    <artifactId>reconciliation</artifactId>
    <version>1.0.0</version>
    <name>reconciliation</name>
    <description>Reconciliation Service</description>
    
    <properties>
        <java.version>21</java.version>
        <reactor.version>3.6.0</reactor.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
            <version>3.4.0</version>
        </dependency>
        
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-core</artifactId>
            <version>3.6.0</version>
        </dependency>
        
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.0</version>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.30</version>
            <scope>provided</scope>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
            <version>1.13.0</version>
        </dependency>
        
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.9</version>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <version>3.4.0</version>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>kafka</artifactId>
            <version>1.19.7</version>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-test</artifactId>
            <version>3.6.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

// === ARCHIVO: src/main/java/com/pragma/reconciliation/application/events/MovementReconciledEvent.java ===
package com.pragma.reconciliation.application.events;

import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record MovementReconciledEvent(
    UUID id,
    String accountId,
    String movementId,
    String eventId,
    Integer version,
    BigDecimal amount,
    String currency,
    LocalDate transactionDate,
    ReconciliationStatus status,
    String source,
    Instant createdAt,
    Instant processedAt,
    String mismatchReason,
    boolean idempotencyProcessed
) {
    public static MovementReconciledEvent fromReconciliation(Reconciliation reconciliation) {
        return new MovementReconciledEvent(
            reconciliation.getId(),
            reconciliation.getAccountId(),
            reconciliation.getMovementId(),
            reconciliation.getEventId(),
            reconciliation.getVersion(),
            reconciliation.getAmount(),
            reconciliation.getCurrency(),
            reconciliation.getTransactionDate(),
            reconciliation.getStatus(),
            reconciliation.getSource(),
            reconciliation.getCreatedAt(),
            reconciliation.getProcessedAt(),
            reconciliation.getMismatchReason(),
            reconciliation.isIdempotencyProcessed()
        );
    }

    public boolean isSuccessful() {
        return status == ReconciliationStatus.MATCHED;
    }

    public boolean requiresManualIntervention() {
        return status == ReconciliationStatus.MANUAL;
    }

    public boolean hasMismatch() {
        return status == ReconciliationStatus.MISMATCHED;
    }

    public String getEventKey() {
        return eventId + "_v" + version;
    }

    public Instant getProcessingLatency() {
        if (createdAt != null && processedAt != null) {
            return processedAt.minusMillis(createdAt.toEpochMilli());
        }
        return Instant.EPOCH;
    }

    public long getLatencySeconds() {
        if (createdAt != null && processedAt != null) {
            return java.time.Duration.between(createdAt, processedAt).getSeconds();
        }
        return 0L;
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/domain/services/Reprocesamiento.java ===
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

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumer.java ===
package com.pragma.reconciliation.infrastructure.adapters;

import com.pragma.reconciliation.application.commands.ReconcileMovementCommand;
import com.pragma.reconciliation.application.events.MovementReconciledEvent;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.ports.in.ReconciliationServicePort;
import com.pragma.reconciliation.domain.services.Reprocesamiento;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ReconciliationKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationKafkaConsumer.class);
    private static final String CONSUMER_GROUP = "reconciliation-processor";
    private static final int BATCH_SIZE = 100;
    private static final Duration PROCESSING_TIMEOUT = Duration.ofSeconds(30);
    private static final int MAX_RETRY_ATTEMPTS = 3;

    private final ReconciliationServicePort reconciliationService;
    private final Reprocesamiento reprocesamiento;
    private final MeterRegistry meterRegistry;
    private final Counter messagesReceivedCounter;
    private final Counter messagesProcessedCounter;
    private final Counter messagesFailedCounter;
    private final Timer processingTimer;

    public ReconciliationKafkaConsumer(
            ReconciliationServicePort reconciliationService,
            Reprocesamiento reprocesamiento,
            MeterRegistry meterRegistry
    ) {
        this.reconciliationService = reconciliationService;
        this.reprocesamiento = reprocesamiento;
        this.meterRegistry = meterRegistry;
        this.messagesReceivedCounter = Counter.builder("kafka.messages.received")
                .description("Total de mensajes recibidos desde Kafka")
                .register(meterRegistry);
        this.messagesProcessedCounter = Counter.builder("kafka.messages.processed")
                .description("Total de mensajes procesados exitosamente")
                .register(meterRegistry);
        this.messagesFailedCounter = Counter.builder("kafka.messages.failed")
                .description("Total de mensajes que fallaron en procesamiento")
                .register(meterRegistry);
        this.processingTimer = Timer.builder("kafka.messages.processing.time")
                .description("Tiempo de procesamiento de mensajes")
                .register(meterRegistry);
    }

    @KafkaListener(
            topics = "${reconciliation.kafka.topics.movements:banking.movements}",
            groupId = "${reconciliation.kafka.consumer.group:reconciliation-processor}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeMovements(List<ReconcileMovementCommand> commands, Acknowledgment ack) {
        log.info("Recibidos {} movimientos para conciliación", commands.size());
        messagesReceivedCounter.increment(commands.size());

        AtomicInteger processedCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);

        Flux.fromIterable(commands)
                .flatMap(this::processMovement, BATCH_SIZE)
                .doOnNext(result -> {
                    if (result.isPresent()) {
                        processedCount.incrementAndGet();
                        messagesProcessedCounter.increment();
                        log.debug("Movimiento procesado exitosamente: {}", result.get().getMovementId());
                    } else {
                        failedCount.incrementAndGet();
                        messagesFailedCounter.increment();
                        log.warn("Movimiento no procesado correctamente");
                    }
                })
                .doOnError(error -> {
                    log.error("Error en procesamiento por lotes: {}", error.getMessage(), error);
                    messagesFailedCounter.increment(commands.size());
                })
                .timeout(PROCESSING_TIMEOUT)
                .doFinally(signal -> {
                    log.info("Lote procesado - Exitosos: {}, Fallidos: {}", 
                            processedCount.get(), failedCount.get());
                    ack.acknowledge();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    private Mono<Reconciliation> processMovement(ReconcileMovementCommand command) {
        return Timer.resource(processingTimer, () -> 
            reconciliationService.reconcileMovement(command)
                .doOnSuccess(reconciliation -> {
                    if (reconciliation.isIdempotencyProcessed()) {
                        log.info("Movimiento ya procesado (idempotente): eventId={}, version={}",
                                command.eventId(), command.version());
                    }
                })
                .doOnError(error -> {
                    log.error("Error procesando movimiento: eventId={}, error={}",
                            command.eventId(), error.getMessage(), error);
                })
        );
    }

    @KafkaListener(
            topics = "${reconciliation.kafka.topics.reprocess:banking.movements.reprocess}",
            groupId = "${reconciliation.kafka.consumer.group:reconciliation-processor}-reprocess",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeReprocessRequests(ReconcileMovementCommand command, Acknowledgment ack) {
        log.info("Recibido request de reprocesamiento para eventId={}, version={}",
                command.eventId(), command.version());

        reconciliationService.reconcileMovement(command)
                .doOnSuccess(reconciliation -> {
                    log.info("Reprocesamiento completado: id={}, status={}",
                            reconciliation.getId(), reconciliation.getStatus());
                    ack.acknowledge();
                })
                .doOnError(error -> {
                    log.error("Error en reprocesamiento: eventId={}, error={}",
                            command.eventId(), error.getMessage(), error);
                    ack.acknowledge();
                })
                .subscribe();
    }

    public Flux<MovementReconciledEvent> consumeMovementsStream() {
        return Flux.empty();
    }

    public Mono<Reconciliation> consume(ReconcileMovementCommand command) {
        return reconciliationService.reconcileMovement(command)
                .doOnSuccess(reconciliation -> {
                    log.info("Mensaje procesado: id={}, status={}", 
                            reconciliation.getId(), reconciliation.getStatus());
                })
                .doOnError(error -> {
                    log.error("Error al consumir mensaje: {}", error.getMessage(), error);
                });
    }

    public Flux<Reconciliation> consumeBatch(List<ReconcileMovementCommand> commands) {
        return reconciliationService.processReconciliationBatch(commands)
                .doOnNext(reconciliation -> {
                    log.debug("Procesado en batch: {}", reconciliation.getMovementId());
                })
                .doOnComplete(() -> {
                    log.info("Batch procesado completamente: {} mensajes", commands.size());
                });
    }

    public Mono<Reconciliation> consumeWithRetry(ReconcileMovementCommand command, int maxAttempts) {
        return consume(command)
                .retryWhen(reactor.util.retry.Retry.backoff(maxAttempts, Duration.ofSeconds(2))
                        .doBeforeRetry(signal -> {
                            log.warn("Reintentando mensaje, intento {} para eventId={}",
                                    signal.totalRetries() + 1, command.eventId());
                        })
                        .onRetryExhaustedThrow((spec, signal) -> {
                            log.error("Exhaustos los reintentos para eventId={}", command.eventId());
                            return signal.failure();
                        }));
    }

    public Mono<Reconciliation> reprocessFromOffset(long offset) {
        Instant threshold = Instant.now().minusSeconds(offset * 60);
        return reprocesamiento.reprocessOlderThan(threshold)
                .take(1)
                .singleOrEmpty()
                .doOnNext(r -> log.info("Reprocesado desde offset {}: id={}", offset, r.getId()));
    }
}

// === ARCHIVO: src/test/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumerTest.java ===
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

// === ARCHIVO: src/main/java/com/pragma/reconciliation/ReconciliationApplication.java ===
package com.pragma.reconciliation;

import com.pragma.reconciliation.application.events.MovementReconciledEvent;
import com.pragma.reconciliation.domain.services.ReconciliationService;
import com.pragma.reconciliation.infrastructure.adapters.ReconciliationKafkaConsumer;
import com.pragma.reconciliation.infrastructure.config.ReconciliationConfig;
import com.pragma.reconciliation.infrastructure.messaging.KafkaConfig;
import com.pragma.reconciliation.infrastructure.monitoring.LagMonitor;
import com.pragma.reconciliation.infrastructure.persistence.ReconciliationJpaRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@EnableConfigurationProperties(ReconciliationConfig.class)
@Import({
    ReconciliationService.class,
    ReconciliationJpaRepository.class,
    ReconciliationKafkaConsumer.class,
    KafkaConfig.class,
    LagMonitor.class
})
public class ReconciliationApplication {
    
    private static final Logger log = LoggerFactory.getLogger(ReconciliationApplication.class);
    
    private final ReconciliationService reconciliationService;
    private final ReconciliationJpaRepository repository;
    private final LagMonitor lagMonitor;
    private final MeterRegistry meterRegistry;
    private final ReconciliationConfig config;
    
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicLong processedCount = new AtomicLong(0);
    private final AtomicLong failedCount = new AtomicLong(0);
    
    public ReconciliationApplication(
            ReconciliationService reconciliationService,
            ReconciliationJpaRepository repository,
            LagMonitor lagMonitor,
            MeterRegistry meterRegistry,
            ReconciliationConfig config) {
        this.reconciliationService = reconciliationService;
        this.repository = repository;
        this.lagMonitor = lagMonitor;
        this.meterRegistry = meterRegistry;
        this.config = config;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(ReconciliationApplication.class, args);
    }
    
    @Bean
    public Timer reconciliationTimer(MeterRegistry registry) {
        return Timer.builder("reconciliation.processing.time")
                .description("Tiempo total de procesamiento de conciliacion")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);
    }
    
    @Bean
    public Flux<MovementReconciledEvent> reconciliationStream() {
        return Flux.defer(() -> {
            if (!running.compareAndSet(false, true)) {
                log.warn("El stream de conciliacion ya esta en ejecucion");
                return Flux.empty();
            }
            
            int matchingWindowMinutes = config.getMatchingWindow() != null 
                ? config.getMatchingWindow().getMinutes() 
                : 60;
            int pollIntervalSeconds = config.getProcessing() != null 
                ? config.getProcessing().getPollIntervalSeconds() 
                : 30;
            
            log.info("Iniciando stream de conciliacion con ventana de {} minutos", 
                    matchingWindowMinutes);
            
            Instant threshold = Instant.now().minus(Duration.ofMinutes(matchingWindowMinutes));
            
            return repository.findPendingOlderThan(threshold)
            .flatMap(reconciliation -> {
                var command = new com.pragma.reconciliation.application.commands.ReconcileMovementCommand(
                    reconciliation.getAccountId(),
                    reconciliation.getMovementId(),
                    reconciliation.getSource(),
                    reconciliation.getEventId(),
                    reconciliation.getVersion(),
                    reconciliation.getAmount(),
                    reconciliation.getCurrency(),
                    reconciliation.getTransactionDate(),
                    java.util.List.of()
                );
                return reconciliationService.reconcileMovement(command)
                    .doOnSuccess(event -> {
                        processedCount.incrementAndGet();
                        log.debug("Movimiento conciliado: eventId={}, status={}", 
                                event.eventId(), event.status());
                    })
                    .doOnError(error -> {
                        failedCount.incrementAndGet();
                        log.error("Error al conciliar movimiento: eventId={}, error={}", 
                                command.eventId(), error.getMessage());
                    })
                    .onErrorResume(error -> {
                        log.error("Reconciliacion fallida para eventId: {}", 
                                command.eventId(), error);
                        return Mono.empty();
                    });
            })
            .subscribeOn(Schedulers.boundedElastic())
            .doFinally(signal -> {
                running.set(false);
                log.info("Stream de conciliacion detenido. Procesados: {}, Fallidos: {}", 
                        processedCount.get(), failedCount.get());
            });
        }).repeat().delayUntil(x -> Mono.delay(Duration.ofSeconds(pollIntervalSeconds)));
    }
    
    @Bean
    public ReconciliationKafkaConsumer kafkaConsumer(
            ReconciliationService service,
            LagMonitor monitor) {
        return new ReconciliationKafkaConsumer(service, monitor);
    }
    
    public record HealthStatus(
            boolean running,
            long processedCount,
            long failedCount,
            Instant lastProcessedAt,
            long currentLagSeconds,
            boolean lagAlertActive
    ) {}
    
    public HealthStatus getHealthStatus() {
        return new HealthStatus(
                running.get(),
                processedCount.get(),
                failedCount.get(),
                null,
                lagMonitor.getCurrentLagSeconds().block() != null ? lagMonitor.getCurrentLagSeconds().block() : 0,
                lagMonitor.isAlertActive() > 0
        );
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/domain/ports/in/ReconciliationServicePort.java ===
package com.pragma.reconciliation.domain.ports.in;

import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import com.pragma.reconciliation.application.commands.ReconcileMovementCommand;
import com.pragma.reconciliation.application.events.MovementReconciledEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReconciliationServicePort {

    Mono<Reconciliation> reconcileMovement(ReconcileMovementCommand command);

    Mono<Reconciliation> getReconciliationById(UUID id);

    Flux<Reconciliation> getReconciliationsByAccountId(String accountId);

    Flux<Reconciliation> getReconciliationsByStatus(ReconciliationStatus status);

    Flux<Reconciliation> getReconciliationsByDateRange(Instant startDate, Instant endDate);

    Mono<Reconciliation> updateReconciliationStatus(UUID id, ReconciliationStatus newStatus);

    Mono<Reconciliation> markAsManualReview(UUID id, String reason);

    Flux<MovementReconciledEvent> processReconciliationBatch(List<ReconcileMovementCommand> commands);

    Mono<Boolean> isMovementProcessed(String eventId, Integer version);

    Mono<Reconciliation> reprocessReconciliation(UUID id);

    Flux<Reconciliation> getPendingReconciliationsOlderThan(Instant threshold);
}

// === ARCHIVO: src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java ===
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


// === ARCHIVO: pom.xml ===
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.pragma</groupId>
    <artifactId>reconciliation</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>reconciliation</name>
    <description>Reconciliation Service</description>
    
    <properties>
        <java.version>21</java.version>
        <reactor.version>3.6.0</reactor.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
            <version>3.4.0</version>
        </dependency>
        
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-core</artifactId>
            <version>3.6.0</version>
        </dependency>
        
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.0</version>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.30</version>
            <scope>provided</scope>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
            <version>1.13.0</version>
        </dependency>
        
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.9</version>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>kafka</artifactId>
            <version>1.19.7</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>


// === ARCHIVO: pom.xml ===
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.pragma</groupId>
    <artifactId>reconciliation</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>reconciliation</name>
    <description>Reconciliation Service</description>
    
    <properties>
        <java.version>21</java.version>
        <reactor.version>3.6.0</reactor.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
            <version>3.4.0</version>
            <scope>compile</scope>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
            <version>3.4.0</version>
            <scope>compile</scope>
        </dependency>
        
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-core</artifactId>
            <version>3.6.0</version>
            <scope>compile</scope>
        </dependency>
        
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.0</version>
            <scope>compile</scope>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
            <version>3.4.0</version>
            <scope>compile</scope>
        </dependency>
        
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.30</version>
            <scope>provided</scope>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
            <version>3.4.0</version>
            <scope>compile</scope>
        </dependency>
        
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
            <version>1.13.0</version>
            <scope>compile</scope>
        </dependency>
        
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.9</version>
            <scope>compile</scope>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <version>3.4.0</version>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>kafka</artifactId>
            <version>1.19.7</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/config/ReconciliationConfig.java ===
package com.pragma.reconciliation.infrastructure.config;

public class ReconciliationConfig {
    private MatchingWindow matchingWindow;
    private Tolerances tolerances;
    private Processing processing;
    private Alerts alerts;

    public MatchingWindow getMatchingWindow() {
        return matchingWindow;
    }

    public void setMatchingWindow(MatchingWindow matchingWindow) {
        this.matchingWindow = matchingWindow;
    }

    public Tolerances getTolerances() {
        return tolerances;
    }

    public void setTolerances(Tolerances tolerances) {
        this.tolerances = tolerances;
    }

    public Processing getProcessing() {
        return processing;
    }

    public void setProcessing(Processing processing) {
        this.processing = processing;
    }

    public Alerts getAlerts() {
        return alerts;
    }

    public void setAlerts(Alerts alerts) {
        this.alerts = alerts;
    }

    public int getSlaThresholdMinutes() {
        return alerts != null && alerts.getSlaThresholdMinutes() > 0 
            ? alerts.getSlaThresholdMinutes() 
            : 5;
    }

    public int getAlertCheckIntervalSeconds() {
        return alerts != null && alerts.getCheckIntervalSeconds() > 0 
            ? alerts.getCheckIntervalSeconds() 
            : 30;
    }

    public static class MatchingWindow {
        private int defaultMinutes = 60;
        private int maxMinutes = 1440;

        public int getDefaultMinutes() {
            return defaultMinutes;
        }

        public void setDefaultMinutes(int defaultMinutes) {
            this.defaultMinutes = defaultMinutes;
        }

        public int getMaxMinutes() {
            return maxMinutes;
        }

        public void setMaxMinutes(int maxMinutes) {
            this.maxMinutes = maxMinutes;
        }
    }

    public static class Tolerances {
        private double amountPercentage = 0.01;
        private double amountAbsolute = 0.01;

        public double getAmountPercentage() {
            return amountPercentage;
        }

        public void setAmountPercentage(double amountPercentage) {
            this.amountPercentage = amountPercentage;
        }

        public double getAmountAbsolute() {
            return amountAbsolute;
        }

        public void setAmountAbsolute(double amountAbsolute) {
            this.amountAbsolute = amountAbsolute;
        }
    }

    public static class Processing {
        private int batchSize = 100;
        private int maxConcurrent = 10;
        private int retryAttempts = 3;

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }

        public int getMaxConcurrent() {
            return maxConcurrent;
        }

        public void setMaxConcurrent(int maxConcurrent) {
            this.maxConcurrent = maxConcurrent;
        }

        public int getRetryAttempts() {
            return retryAttempts;
        }

        public void setRetryAttempts(int retryAttempts) {
            this.retryAttempts = retryAttempts;
        }
    }

    public static class Alerts {
        private int slaThresholdMinutes = 5;
        private int checkIntervalSeconds = 30;
        private boolean enabled = true;

        public int getSlaThresholdMinutes() {
            return slaThresholdMinutes;
        }

        public void setSlaThresholdMinutes(int slaThresholdMinutes) {
            this.slaThresholdMinutes = slaThresholdMinutes;
        }

        public int getCheckIntervalSeconds() {
            return checkIntervalSeconds;
        }

        public void setCheckIntervalSeconds(int checkIntervalSeconds) {
            this.checkIntervalSeconds = checkIntervalSeconds;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitor.java ===
package com.pragma.reconciliation.infrastructure.monitoring;

import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.ReconciliationStatus;
import com.pragma.reconciliation.domain.ports.in.ReconciliationServicePort;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class LagMonitor {

    private static final Logger log = LoggerFactory.getLogger(LagMonitor.class);
    private static final Duration DEFAULT_SLA_THRESHOLD = Duration.ofMinutes(5);
    private static final Duration DEFAULT_CHECK_INTERVAL = Duration.ofSeconds(30);

    private final ReconciliationServicePort reconciliationService;
    private final MeterRegistry meterRegistry;
    private final AtomicLong currentLagSeconds;
    private final AtomicReference<Instant> lastAlertTime;
    private final AtomicLong pendingCount;
    private final Timer lagCheckTimer;
    private final AtomicReference<Duration> slaThreshold;
    private final AtomicReference<Duration> checkInterval;
    private final java.util.List<Long> lagHistory = java.util.Collections.synchronizedList(new java.util.ArrayList<>());

    public LagMonitor(ReconciliationServicePort reconciliationService, MeterRegistry meterRegistry) {
        this.reconciliationService = reconciliationService;
        this.meterRegistry = meterRegistry;
        this.currentLagSeconds = new AtomicLong(0);
        this.lastAlertTime = new AtomicReference<>(Instant.EPOCH);
        this.pendingCount = new AtomicLong(0);
        this.slaThreshold = new AtomicReference<>(DEFAULT_SLA_THRESHOLD);
        this.checkInterval = new AtomicReference<>(DEFAULT_CHECK_INTERVAL);

        registerMetrics();
        this.lagCheckTimer = Timer.builder("reconciliation.lag.check")
                .description("Tiempo de verificación del lag de conciliación")
                .register(meterRegistry);
    }

    private void registerMetrics() {
        Gauge.builder("reconciliation.lag.seconds", currentLagSeconds, AtomicLong::get)
                .description("Lag actual de conciliación en segundos")
                .tag("sla_threshold", String.valueOf(slaThreshold.get().toSeconds()))
                .register(meterRegistry);

        Gauge.builder("reconciliation.pending.count", pendingCount, AtomicLong::get)
                .description("Cantidad de conciliaciones pendientes")
                .register(meterRegistry);

        Gauge.builder("reconciliation.lag.alert.active", this, LagMonitor::isAlertActive)
                .description("Indica si hay una alerta de lag activa")
                .register(meterRegistry);
    }

    public int isAlertActive() {
        return (currentLagSeconds.get() > slaThreshold.get().toSeconds()) ? 1 : 0;
    }

    public Mono<LagMeasurement> measureLag(Instant eventTime) {
        Duration sla = slaThreshold.get();
        long lagSeconds = Duration.between(eventTime, Instant.now()).getSeconds();
        boolean alertTriggered = lagSeconds > sla.toSeconds();
        
        return Mono.just(new LagMeasurement(lagSeconds, alertTriggered, eventTime));
    }

    public record LagMeasurement(long seconds, boolean alertTriggered, Instant eventTime) {}

    public void recordEvent(Instant eventTime) {
        long lagSeconds = Duration.between(eventTime, Instant.now()).getSeconds();
        lagHistory.add(lagSeconds);
        if (lagHistory.size() > 100) {
            lagHistory.remove(0);
        }
    }

    public Mono<Void> checkAndAlert() {
        return Timer.resource(lagCheckTimer)
                .flatMap(timer -> {
                    log.debug("Iniciando verificación de lag de conciliación");
                    return calculateCurrentLag()
                            .flatMap(this::updateMetricsAndCheckAlert)
                            .doOnSuccess(v -> log.debug("Verificación de lag completada, lag actual: {} segundos", 
                                    currentLagSeconds.get()))
                            .doOnError(e -> log.error("Error al verificar lag de conciliación", e));
                });
    }

    public Mono<Void> checkAndAlert(Instant eventTime) {
        return measureLag(eventTime)
                .flatMap(measurement -> {
                    if (measurement.alertTriggered()) {
                        return triggerAlert(measurement.seconds(), pendingCount.get());
                    }
                    return Mono.empty();
                });
    }

    private Mono<Long> calculateCurrentLag() {
        Duration interval = checkInterval.get();
        Instant threshold = Instant.now().minus(interval.multipliedBy(2));
        
        return reconciliationService.getReconciliationsByStatus(ReconciliationStatus.PENDING)
                .take(1)
                .switchIfEmpty(Flux.empty().then(Mono.just(Instant.now())))
                .flatMap(reconciliation -> {
                    Instant createdAt = reconciliation.getCreatedAt();
                    long lagSeconds = Duration.between(createdAt, Instant.now()).getSeconds();
                    return Mono.just(lagSeconds);
                })
                .defaultIfEmpty(0L)
                .single();
    }

    private Mono<Void> updateMetricsAndCheckAlert(Long lagSeconds) {
        currentLagSeconds.set(lagSeconds);
        
        return reconciliationService.getReconciliationsByStatus(ReconciliationStatus.PENDING)
                .count()
                .doOnNext(count -> pendingCount.set(count))
                .flatMap(count -> {
                    if (lagSeconds > slaThreshold.get().toSeconds()) {
                        return handleLagAlert(lagSeconds, count);
                    }
                    return Mono.empty();
                });
    }

    private Mono<Void> handleLagAlert(Long lagSeconds, Long pendingCount) {
        Instant now = Instant.now();
        Instant lastAlert = lastAlertTime.get();
        Duration interval = checkInterval.get();
        
        if (Duration.between(lastAlert, now).compareTo(interval) > 0) {
            if (lastAlertTime.compareAndSet(lastAlert, now)) {
                log.warn("ALERTA: Lag de conciliación excede el SLA de {} minutos. " +
                        "Lag actual: {} segundos, Movimientos pendientes: {}", 
                        slaThreshold.get().toMinutes(), lagSeconds, pendingCount);
                
                emitAlertMetric(lagSeconds, pendingCount);
                return Mono.empty();
            }
        }
        return Mono.empty();
    }

    private Mono<Void> triggerAlert(Long lagSeconds, Long pendingCount) {
        meterRegistry.counter("reconciliation.lag.alerts.triggered",
                "reason", "sla_exceeded")
                .increment();
        
        log.error("ALERTA - Lag: {}s, Pendientes: {}, Umbral SLA: {}s",
                lagSeconds, pendingCount, slaThreshold.get().toSeconds());
        
        return Mono.just(new AlertEvent(lagSeconds, "Lag SUPERO el umbral de " + slaThreshold.get().toMinutes() + " minutos"))
                .then();
    }

    public record AlertEvent(long lagSeconds, String message) {}

    private void emitAlertMetric(Long lagSeconds, Long pendingCount) {
        meterRegistry.counter("reconciliation.lag.alerts.triggered",
                "reason", "sla_exceeded")
                .increment();
        
        log.error("MÉTRICAS DE ALERTA - Lag: {}s, Pendientes: {}, Umbral SLA: {}s",
                lagSeconds, pendingCount, slaThreshold.get().toSeconds());
    }

    public Mono<Long> getCurrentLagSeconds() {
        return calculateCurrentLag();
    }

    public long getSlaThresholdSeconds() {
        return slaThreshold.get().toSeconds();
    }

    public boolean isLagExceedingSla() {
        return currentLagSeconds.get() > slaThreshold.get().toSeconds();
    }

    public Mono<Long> getPendingCount() {
        return reconciliationService.getReconciliationsByStatus(ReconciliationStatus.PENDING)
                .count();
    }

    public Flux<Long> startMonitoring() {
        return Flux.interval(checkInterval.get())
                .flatMap(tick -> calculateCurrentLag()
                        .doOnNext(lag -> {
                            currentLagSeconds.set(lag);
                            if (lag > slaThreshold.get().toSeconds()) {
                                handleLagAlert(lag, pendingCount.get()).block();
                            }
                        })
                );
    }

    public Mono<Double> getLagTrend() {
        return Mono.fromCallable(() -> {
            if (lagHistory.size() < 2) {
                return 0.0;
            }
            synchronized (lagHistory) {
                long first = lagHistory.get(0);
                long last = lagHistory.get(lagHistory.size() - 1);
                return (double) (last - first);
            }
        });
    }

    public void recordProcessingTime(Duration duration) {
        meterRegistry.timer("reconciliation.lag.measurement")
                .record(duration);
    }

    public String exportMetrics() {
        StringBuilder sb = new StringBuilder();
        sb.append("# TYPE reconciliation_lag gauge\n");
        sb.append("reconciliation_lag ").append(currentLagSeconds.get()).append("\n");
        sb.append("# TYPE reconciliation_pending gauge\n");
        sb.append("reconciliation_pending ").append(pendingCount.get()).append("\n");
        sb.append("# TYPE reconciliation_lag_alert gauge\n");
        sb.append("reconciliation_lag_alert ").append(isAlertActive()).append("\n");
        return sb.toString();
    }
}

// === ARCHIVO: src/test/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitorTest.java ===
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

```
