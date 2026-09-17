# Diseño de sistema de conciliación bancaria en tiempo real con reprocesamiento idempotente

Diseña un motor de conciliación que consume streams de movimientos desde tres fuentes (core bancario, gateway de pagos, sistema de liquidación) y detecta discrepancias en ventanas móviles. Cada movimiento debe reconciliarse contra las tres fuentes con tolerancia a mensajes fuera de orden y llegadas duplicadas. El motor debe manejar idempotencia con eventId + version y elegir entre reprocesamiento por replay de Kafka vs snapshot desde PostgreSQL. Además, debe alertar al equipo de operaciones cuando el lag de conciliación supera un SLA de 5 minutos.

## Informacion General

| Campo | Valor |
|-------|-------|
| **Tema** | TEST-CT |
| **Nivel** | senior-l2 |
| **Tipo** | mixed |
| **Tiempo estimado** | 12 horas |

## Fases del Reto

### Fase 0: Configuración del Proyecto

**Objetivo:** Obtener el proyecto base funcional enviando el Código Base a un asistente de IA, que lo analizará, corregirá errores y generará un ZIP listo para usar.

**Tiempo estimado:** 15-30 minutos

**Instrucciones:**

- Asegúrate de tener instalado para ejecutar el proyecto: JDK 17+, Maven 3.9+, IDE con soporte Java.
- Copia todo el contenido del campo **Código Base** de este reto — incluyendo el texto de instrucciones que aparece al inicio.
- Abre un asistente de IA (Claude en claude.ai, ChatGPT o Gemini — se recomienda Claude), pega el contenido copiado en el chat y envíalo.
- El asistente analizará los archivos, corregirá errores y generará un archivo ZIP descargable. Descárgalo y extráelo en la carpeta donde quieras trabajar.
- Ejecuta `mvn compile` en la raíz. Si no hay errores, estás listo.

**Entregable:** El proyecto compila/arranca sin errores.

<details>
<summary>Pistas de conocimiento</summary>

- Copia el Código Base completo incluyendo el texto de instrucciones al inicio — esas instrucciones le indican al asistente exactamente qué hacer con los archivos.
- Si el asistente no genera el ZIP automáticamente al terminar el análisis, escríbele: "genera el ZIP ahora".
- Si el proyecto tiene errores al arrancar, comparte el mensaje de error con el mismo asistente para que lo corrija.

</details>

### Fase 1: Exploración del dominio y definición de requisitos

**Objetivo:** Identificar las fuentes de movimientos, definir los estados de la reconciliación y establecer los criterios de tolerancia a mensajes fuera de orden y duplicados.

**Tiempo estimado:** 3 horas

**Instrucciones:**

- Enumera las tres fuentes de movimientos y describe sus características.
- Define los estados de la reconciliación (Pending, Matched, Mismatched, Manual) y justifica cada uno.
- Establece los criterios de tolerancia a mensajes fuera de orden y llegadas duplicadas.

**Entregable:** Documento que describe las fuentes de movimientos, los estados de la reconciliación y los criterios de tolerancia.

<details>
<summary>Pistas de conocimiento</summary>

- Considera los posibles edge cases en la recepción de movimientos.
- Piensa en cómo se pueden manejar los mensajes duplicados sin afectar la consistencia.

</details>

### Fase 2: Diseño de la máquina de estados y definición de la ventana de matching

**Objetivo:** Diseñar la máquina de estados de la reconciliación y justificar la elección de la ventana de matching (5 minutos vs 1 hora).

**Tiempo estimado:** 3 horas

**Instrucciones:**

- Diseña la máquina de estados de la reconciliación y describe las transiciones entre estados.
- Justifica la elección de la ventana de matching (5 minutos vs 1 hora) y describe cómo se manejarán los movimientos que caigan fuera de la ventana.

**Entregable:** Diagrama de la máquina de estados de la reconciliación y documento que justifica la elección de la ventana de matching.

<details>
<summary>Pistas de conocimiento</summary>

- Considera los impactos en la latencia y la precisión de la reconciliación al elegir la ventana de matching.
- Piensa en cómo se pueden manejar los movimientos que caigan fuera de la ventana sin afectar la consistencia.

</details>

### Fase 3: Definición de la idempotencia y elección del mecanismo de reprocesamiento

**Objetivo:** Definir cómo se manejará la idempotencia con eventId + version y elegir entre reprocesamiento por replay de Kafka vs snapshot desde PostgreSQL.

**Tiempo estimado:** 3 horas

**Instrucciones:**

- Define cómo se manejará la idempotencia con eventId + version y describe los casos de uso.
- Elige entre reprocesamiento por replay de Kafka vs snapshot desde PostgreSQL y justifica tu elección.

**Entregable:** Documento que describe cómo se manejará la idempotencia y justifica la elección del mecanismo de reprocesamiento.

<details>
<summary>Pistas de conocimiento</summary>

- Considera los pros y contras de cada mecanismo de reprocesamiento en términos de latencia, consistencia y complejidad.
- Piensa en cómo se pueden manejar los casos de fallo en cada mecanismo de reprocesamiento.

</details>

### Fase 4: Definición de la alerta de lag de conciliación

**Objetivo:** Definir cómo se alertará al equipo de operaciones cuando el lag de conciliación supere un SLA de 5 minutos.

**Tiempo estimado:** 3 horas

**Instrucciones:**

- Define cómo se medirá el lag de conciliación y cuáles serán los criterios para alertar al equipo de operaciones.
- Describe el mecanismo de alerta y cómo se integrará con el sistema de monitoreo.

**Entregable:** Documento que describe cómo se medirá el lag de conciliación, los criterios para alertar al equipo de operaciones y el mecanismo de alerta.

<details>
<summary>Pistas de conocimiento</summary>

- Considera los posibles impactos en la operación al alertar al equipo de operaciones.
- Piensa en cómo se pueden integrar las alertas con el sistema de monitoreo existente.

</details>

## Dimensiones Evaluadas

- **queEs**: ¿Qué es la conciliación bancaria en tiempo real y por qué es importante?
- **paraQueSirve**: ¿Para qué sirve la máquina de estados en la conciliación bancaria?
- **comoSeUsa**: ¿Cómo se usa la idempotencia en la conciliación bancaria?
- **erroresComunes**: ¿Cuáles son los errores comunes en la conciliación bancaria y cómo se pueden evitar?
- **queDecisionesImplica**: ¿Qué decisiones implica la elección del mecanismo de reprocesamiento en la conciliación bancaria?

## Criterios de Evaluacion

- Identificar correctamente las fuentes de movimientos y sus características.
- Definir adecuadamente los estados de la reconciliación y justificar cada uno.
- Establecer los criterios de tolerancia a mensajes fuera de orden y duplicados.
- Diseñar la máquina de estados de la reconciliación y describir las transiciones entre estados.
- Justificar la elección de la ventana de matching y describir cómo se manejarán los movimientos que caigan fuera de la ventana.
- Definir cómo se manejará la idempotencia con eventId + version y describir los casos de uso.
- Elegir entre reprocesamiento por replay de Kafka vs snapshot desde PostgreSQL y justificar la elección.
- Definir cómo se medirá el lag de conciliación y los criterios para alertar al equipo de operaciones.
- Describir el mecanismo de alerta y cómo se integrará con el sistema de monitoreo.

## Como trabajar con un asistente de IA

Hay dos caminos, elegi uno:

- **AGENTS.md** (recomendado) — instrucciones nativas del repo. Abri esta carpeta con tu agente local (Claude Code, Cursor, Codex, Copilot, Gemini) y las carga solo. Sabe que archivos faltan y con que comando se verifica, y completa el scaffold escribiendo en disco.
- **PROMPT_MEJORA.md** — para copiar y pegar en un chat (claude.ai, ChatGPT). Devuelve un ZIP con el proyecto. Sirve si no tenes un agente en el IDE.

Ninguno de los dos resuelve las fases del reto: eso es tu trabajo.

## Verificacion

El proyecto esta listo para trabajar cuando este comando corre sin errores:

```bash
el comando de build o arranque canonico del stack elegido
```

---

*Reto generado automaticamente por Challenge Generator - Pragma*
