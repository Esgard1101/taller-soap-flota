# Metodología ágil: un sprint de 60 minutos

Objetivo: obtener las cinco operaciones SOAP, tres casos de ID inexistente y
evidencias de Boomerang en una sesión de una hora.

Supuestos: NetBeans/JDK/Payara/Maven ya funcionan, dependencias disponibles,
GitHub autenticado, tres colaboradores o agentes trabajando en paralelo y un
integrador. El tiempo de instalación o de resolver autenticación se trata antes
del sprint. El CRUD aún está pendiente en esta base.

## Agenda de la hora

| Minutos | Actividad | Responsable | Resultado |
| --- | --- | --- | --- |
| 00-05 | Acordar alcance y revisar build/servidor | Integrador | Objetivo y bloqueos visibles |
| 05-12 | Integrar modelo, interfaces y Fault comunes | Integrador | Commit base que compila |
| 12-32 | Implementar tareas A, B y C en paralelo | Tres colaboradores | Tres PR pequeños |
| 32-42 | Revisar, integrar y ejecutar build | Integrador y autores | main compila y pasa pruebas |
| 42-55 | Desplegar y ejecutar Boomerang | Integrador con C | CRUD y Fault observados |
| 55-60 | Guardar evidencias y cerrar tareas | Equipo | Entrega y mejora para la siguiente sesión |

Hay una reunión inicial de cinco minutos y una revisión final de cinco.
Durante el desarrollo, comunicar solo cambios, bloqueos o dependencias.
No se necesita una reunión diaria para una sesión de una hora.

**Punto de control en minuto 12:** nadie comienza implementaciones paralelas
sin el commit de interfaces compartidas. Si falla, se corrige esa base primero
y se comunica la nueva previsión, en vez de producir tres contratos distintos.

## Tablero mínimo de GitHub

Estados: **Por hacer -> En curso -> En revisión -> Hecho**.
Usar un milestone `Sprint 1 - Flota SOAP - 60 min`.
Un GitHub Project es opcional: si configurarlo consume tiempo, usar Issues,
el milestone y sus enlaces de PR durante esta primera hora.

Cada issue tiene: objetivo, un responsable, archivos permitidos, dependencias,
criterios de aceptación y evidencia. Usar la plantilla del repositorio.
Los códigos FL-01, FL-02, etc. son identificadores del backlog; no son números
reales de issues hasta crearlas en GitHub.

Límite de trabajo en curso: una tarea activa por colaborador.
El integrador controla los archivos comunes: pom, contrato, modelo e interfaces.
Dividir por componentes evita que tres agentes editen simultáneamente FlotaWS.

## Ramas y revisión

Usar GitHub flow con una rama principal `main` y ramas cortas por tarea:

- `feature/fl-04-repository`
- `feature/fl-05-application`
- `feature/fl-06-soap`

Cada PR enlaza su issue mediante `Closes #numero-real`.
El autor indica qué cambió, comando de validación y resultado observado.
El integrador revisa contrato, comportamiento y alcance; después integra.
La revisión por agente ayuda, pero el responsable humano conserva la decisión.

La primera carga de la base es el punto de partida. Los cambios posteriores
se integran por PR. Configurar una regla de main con revisión y check `build`
cuando la primera CI exista; documentar si el plan de GitHub limita esa regla.
No afirmar que main está protegida hasta verificar la configuración.

## Aislamiento para varios agentes

Cada agente usa su propia carpeta, rama y contexto de conversación. Compartir
una carpeta mientras se cambia de rama afecta los archivos de todos los agentes.

Tras el primer commit, el integrador puede crear worktrees desde la raíz:

```powershell
git worktree add -b feature/fl-04-repository ../flota-agente-a main
git worktree add -b feature/fl-05-application ../flota-agente-b main
git worktree add -b feature/fl-06-soap ../flota-agente-c main
```

En otras computadoras, cada programador usa su propio clon.
Los worktrees comparten el historial Git y mantienen carpetas y ramas separadas.
No usar una carpeta sincronizada por OneDrive como mecanismo para fusionar código:
la integración entre equipos ocurre por Git. Esta ubicación puede conservar la
entrega del curso; para trabajo habitual, un clon fuera de carpetas sincronizadas
reduce interferencias de sincronización.

Solo el integrador despliega en el Payara compartido durante esta hora.
Para despliegues paralelos posteriores, usar dominios y puertos distintos.

## Cómo entregar una tarea a cualquier agente

```text
Tarea: FL-04 - repositorio de vehículos en memoria.
Lee AGENTS.md y docs/02_CONTRATO_Y_ALCANCE.md.
Base: <commit de interfaces>; rama: feature/fl-04-repository.
Edita solo repository/** y sus pruebas.
Implementa listar, buscar, insertar, actualizar y eliminar según la interfaz.
Genera IDs únicos bajo el bloqueo compartido y devuelve copias defensivas.
Aceptación: CRUD correcto, lista vacía, eliminación sin reutilizar ID,
mantenimientos completos y altas simultáneas sin ID duplicado.
Ejecuta mvn verify. Entrega PR, resultados y cualquier bloqueo.
Si necesitas cambiar una firma compartida, comunica el cambio al integrador.
Presupuesto: 20 minutos.
```

Esta estructura sirve para distintas herramientas. No depende de que todos usen
el mismo proveedor. Si una herramienta no lee AGENTS.md automáticamente, pasar
explícitamente su contenido y los archivos de referencia.

## Qué significa Hecho

- Se cumplen los criterios de la issue.
- La rama integrada compila y pasa las pruebas de comportamiento existentes.
- El cambio fue revisado y el contrato coincide.
- Para el endpoint, se observó el WSDL y la petición/respuesta en Payara.
- La demo final muestra cinco operaciones y los Fault requeridos.
- Se guardaron evidencias y se anotó cualquier pendiente real.

## Cuando el reloj se retrasa

Al minuto 32 deben existir entregas integrables. Si una tarea sigue bloqueada,
el integrador reduce cambios adicionales y ayuda en ese componente.
Mantener el mínimo del PDF: CRUD, lista estática, mantenimientos y Fault.
Posponer mejoras de formato, reglas nuevas, BD, autenticación, frontend y
automatizaciones adicionales. Si el mínimo no funciona, registrar el bloqueo;
no marcarlo como completo por agotar el tiempo.

Referencias: [GitHub flow](https://docs.github.com/en/get-started/using-github/github-flow),
[worktrees de Git](https://git-scm.com/docs/git-worktree).
