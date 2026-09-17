# AGENTS.md

Instrucciones para el agente de IA que abra este repositorio (Claude Code, Cursor, Codex, Copilot, Gemini). Se cargan solas: no hay que pegar nada en ningun chat.

## Que es este repositorio

Es el codigo base de un reto de aprendizaje de Pragma: **Diseño de sistema de conciliación bancaria en tiempo real con reprocesamiento idempotente**.

| | |
|---|---|
| Tema | TEST-CT |
| Nivel | senior-l2 |
| Chapter | Generico |
| Especialidad | Inferido del contexto |
| Stack | Java / Spring Boot 3.4 |
| Patron arquitectonico | hexagonal/clean con CQRS y eventos |
| Tiempo estimado | 12 horas |

## Tu tarea

Dejar este proyecto en estado **verificable**: que el comando de verificacion corra sin errores. Escribi los archivos en disco, en este repositorio. No generes ZIPs ni archivos adjuntos.

En orden:

1. Corre `el comando de build o arranque canonico del stack elegido` y mira que falla.
2. Completa lo que falte de la lista de abajo: manifiesto de dependencias, punto de entrada, capa de interfaz y las capas del patron declarado.
3. Arregla SOLO los errores que impiden compilar o arrancar.
4. Volve a correr `el comando de build o arranque canonico del stack elegido` hasta que pase.
5. Pará ahí.

## Regla dura: las fases son trabajo del humano

**PROHIBIDO implementar los entregables de las fases.** El valor del reto esta en que la persona los resuelva. Tu trabajo es que tenga un proyecto que arranca; el hueco pedagogico se queda como esta.

No resuelvas nada de esto:

- **Fase 1 — Exploración del dominio y definición de requisitos**: Documento que describe las fuentes de movimientos, los estados de la reconciliación y los criterios de tolerancia.
- **Fase 2 — Diseño de la máquina de estados y definición de la ventana de matching**: Diagrama de la máquina de estados de la reconciliación y documento que justifica la elección de la ventana de matching.
- **Fase 3 — Definición de la idempotencia y elección del mecanismo de reprocesamiento**: Documento que describe cómo se manejará la idempotencia y justifica la elección del mecanismo de reprocesamiento.
- **Fase 4 — Definición de la alerta de lag de conciliación**: Documento que describe cómo se medirá el lag de conciliación, los criterios para alertar al equipo de operaciones y el mecanismo de alerta.

Distincion operativa:

- **Arreglar** (si): import faltante, tipo que no existe, dependencia sin declarar, error de sintaxis, archivo referenciado que no existe.
- **No tocar** (no): logica de negocio incompleta, validaciones ausentes, secretos hardcodeados, APIs deprecadas que funcionan, concurrencia insegura, patrones mejorables. Eso es lo que la persona tiene que encontrar.

## Lo que falta y tenes que completar

### 1. Boilerplate del stack (2)

Sin esto el proyecto no compila ni arranca. **Es tu trabajo crearlo**, y no toca nada de lo pedagogico: es andamiaje del stack.

- [ ] **Punto de entrada del stack elegido** — Sin un punto de entrada reconocible, el runtime no tiene por donde arrancar la aplicacion.
- [ ] **Capa de interfaz (controller/handler)** — Sin una capa de interfaz explicita, no hay forma de invocar la logica de negocio desde afuera del proceso.

### 2. Archivos que la arquitectura declara (1 de 18)

La propuesta arquitectonica del reto los lista y no llegaron al repo. Crealos con implementacion real, respetando la capa en la que viven:

- [ ] `docs/alerta_lag.md`

### 3. Referencias colgando (64)

Salieron de un analisis estatico del codigo que SI esta en el repo. Cada una rompe la compilacion:

- [ ] `src/main/java/com/pragma/reconciliation/ReconciliationApplication.java` — `ReconcileMovementCommand`
      ReconcileMovementCommand se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.application.commands.ReconcileMovementCommand.
- [ ] `src/main/java/com/pragma/reconciliation/domain/services/Reprocesamiento.java` — `ReconcileMovementCommand`
      ReconcileMovementCommand se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.application.commands.ReconcileMovementCommand.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitor.java` — `Reconciliation`
      El import com.pragma.reconciliation.domain.model.Reconciliation no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- [ ] `src/test/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitorTest.java` — `Reconciliation`
      El import com.pragma.reconciliation.domain.model.Reconciliation no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- [ ] `src/test/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitorTest.java` — `ReconciliationStatus`
      El import com.pragma.reconciliation.domain.model.ReconciliationStatus no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- [ ] `src/main/java/com/pragma/reconciliation/ReconciliationApplication.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/pragma/reconciliation/ReconciliationApplication.java` — `reactor.core.scheduler`
      El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/pragma/reconciliation/domain/ports/in/ReconciliationServicePort.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/pragma/reconciliation/domain/ports/out/ReconciliationPersistencePort.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumer.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumer.java` — `reactor.core.scheduler`
      El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitor.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/test/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumerTest.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/test/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitorTest.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/pragma/reconciliation/domain/services/Reprocesamiento.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.eventId`
      Se invoca `eventId` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.version`
      Se invoca `version` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.source`
      Se invoca `source` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.id`
      Se invoca `id` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.accountId`
      Se invoca `accountId` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.movementId`
      Se invoca `movementId` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.amount`
      Se invoca `amount` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.currency`
      Se invoca `currency` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java` — `ReconcileMovementCommand.transactionDate`
      Se invoca `transactionDate` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getId`
      Se invoca `getId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setId`
      Se invoca `setId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setAccountId`
      Se invoca `setAccountId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setMovementId`
      Se invoca `setMovementId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setEventId`
      Se invoca `setEventId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setVersion`
      Se invoca `setVersion` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setAmount`
      Se invoca `setAmount` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setCurrency`
      Se invoca `setCurrency` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setTransactionDate`
      Se invoca `setTransactionDate` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setStatus`
      Se invoca `setStatus` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setSource`
      Se invoca `setSource` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setCreatedAt`
      Se invoca `setCreatedAt` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setProcessedAt`
      Se invoca `setProcessedAt` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setMismatchReason`
      Se invoca `setMismatchReason` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.setIdempotencyProcessed`
      Se invoca `setIdempotencyProcessed` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getAccountId`
      Se invoca `getAccountId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getMovementId`
      Se invoca `getMovementId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getEventId`
      Se invoca `getEventId` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getVersion`
      Se invoca `getVersion` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getAmount`
      Se invoca `getAmount` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getCurrency`
      Se invoca `getCurrency` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getTransactionDate`
      Se invoca `getTransactionDate` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getStatus`
      Se invoca `getStatus` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getSource`
      Se invoca `getSource` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getCreatedAt`
      Se invoca `getCreatedAt` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getProcessedAt`
      Se invoca `getProcessedAt` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.getMismatchReason`
      Se invoca `getMismatchReason` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java` — `ReconciliationEntity.isIdempotencyProcessed`
      Se invoca `isIdempotencyProcessed` sobre `ReconciliationEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumer.java` — `ReconcileMovementCommand.eventId`
      Se invoca `eventId` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumer.java` — `ReconcileMovementCommand.version`
      Se invoca `version` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/config/ReconciliationConfig.java` — `Alerts.getCheckIntervalSeconds`
      Se invoca `getCheckIntervalSeconds` sobre `Alerts`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java` — `ReconcileMovementCommand.accountId`
      Se invoca `accountId` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java` — `ReconcileMovementCommand.movementId`
      Se invoca `movementId` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java` — `ReconcileMovementCommand.eventId`
      Se invoca `eventId` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java` — `ReconcileMovementCommand.version`
      Se invoca `version` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java` — `ReconcileMovementCommand.currency`
      Se invoca `currency` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java` — `ReconcileMovementCommand.transactionDate`
      Se invoca `transactionDate` sobre `ReconcileMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.

### Presentes (22)

- `pom.xml`
- `src/main/java/com/pragma/reconciliation/ReconciliationApplication.java`
- `src/main/java/com/pragma/reconciliation/domain/model/ReconciliationStatus.java`
- `src/main/java/com/pragma/reconciliation/domain/model/Reconciliation.java`
- `src/main/java/com/pragma/reconciliation/domain/ports/in/ReconciliationServicePort.java`
- `src/main/java/com/pragma/reconciliation/domain/ports/out/ReconciliationPersistencePort.java`
- `src/main/java/com/pragma/reconciliation/application/commands/ReconcileMovementCommand.java`
- `src/main/java/com/pragma/reconciliation/domain/services/ReconciliationService.java`
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence/ReconciliationJpaRepository.java`
- `src/main/java/com/pragma/reconciliation/application/events/MovementReconciledEvent.java`
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumer.java`
- `src/main/java/com/pragma/reconciliation/infrastructure/messaging/KafkaConfig.java`
- `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitor.java`
- `src/main/java/com/pragma/reconciliation/infrastructure/config/ReconciliationConfig.java`
- `src/test/java/com/pragma/reconciliation/domain/services/ReconciliationServiceTest.java`
- `src/test/java/com/pragma/reconciliation/infrastructure/adapters/ReconciliationKafkaConsumerTest.java`
- `src/test/java/com/pragma/reconciliation/infrastructure/monitoring/LagMonitorTest.java`
- `docs/estados_reconciliacion.md`
- `docs/ventana_matching.md`
- `docs/idempotencia.md`
- `docs/mecanismo_reprocesamiento.md`
- `src/main/java/com/pragma/reconciliation/domain/services/Reprocesamiento.java`

### Capas del patron declarado

Cada una tiene que existir como directorio real con al menos un archivo. Codigo plano en la raiz no satisface el patron.

- `src/main/java/com/pragma/reconciliation`
- `src/main/java/com/pragma/reconciliation/application`
- `src/main/java/com/pragma/reconciliation/application/commands`
- `src/main/java/com/pragma/reconciliation/application/queries`
- `src/main/java/com/pragma/reconciliation/application/events`
- `src/main/java/com/pragma/reconciliation/domain`
- `src/main/java/com/pragma/reconciliation/domain/model`
- `src/main/java/com/pragma/reconciliation/domain/ports`
- `src/main/java/com/pragma/reconciliation/domain/services`
- `src/main/java/com/pragma/reconciliation/infrastructure`
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters`
- `src/main/java/com/pragma/reconciliation/infrastructure/config`
- `src/main/java/com/pragma/reconciliation/infrastructure/messaging`
- `src/main/java/com/pragma/reconciliation/infrastructure/persistence`
- `src/main/java/com/pragma/reconciliation/infrastructure/monitoring`
- `src/test/java/com/pragma/reconciliation`

## Verificacion

```bash
el comando de build o arranque canonico del stack elegido
```

Ese comando pasando es la definicion de "terminado" para vos.

## Convenciones que tenes que respetar

- Un solo ecosistema: no declares librerias de otro lenguaje ni mezcles gestores de paquetes.
- Toda libreria que uses tiene que estar declarada en el manifiesto de dependencias.
- Todo import declarado tiene que usarse; todo tipo usado tiene que existir o venir de una dependencia declarada.
- El patron es **hexagonal/clean con CQRS y eventos**: los contratos (interfaces, puertos) los define la capa interna y los implementa la externa, nunca al revés.
- Los archivos que crees llevan implementacion real, no stubs: sin `TODO`, sin cuerpos vacios, sin `// getters y setters`.

## Contexto del candidato

Sirve para calibrar el nivel del codigo, no para resolver las fases.

- Brecha que el reto ataca: Diseño de motor de conciliación que consume streams de movimientos desde 3 fuentes (core bancario, gateway de pagos, sistema de liquidación) y detecta discrepancias en ventanas móviles. Cada movimiento debe reconciliarse contra las 3 fuentes con tolerancia a mensajes fuera de orden y llegadas duplicadas. El desarrollador senior debe diseñar la máquina de estados de cada Reconciliation (Pending, Matched, Mismatched, Manual), justificar la ventana de matching (5 min vs 1 hora), definir cómo maneja idempotencia con eventId + version, y elegir entre reprocesamiento por replay de Kafka vs snapshot desde PostgreSQL. Adicionalmente debe explicar cómo alertar al equipo de operaciones cuando el lag de conciliación supera un SLA.

---

*Generado por Challenge Generator — Pragma. `README.md` tiene el enunciado completo del reto para la persona. `PROMPT_MEJORA.md` es la variante para pegar en un chat, si se prefiere ese flujo.*
