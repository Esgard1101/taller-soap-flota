# MetodologÃ­a Ã¡gil: un sprint de 60 minutos

Objetivo: obtener las cinco operaciones SOAP, tres casos de ID inexistente y
evidencias de Boomerang en una sesiÃ³n de una hora.

Supuestos: NetBeans/JDK/Payara/Maven ya funcionan, dependencias disponibles,
GitHub autenticado, tres colaboradores o agentes trabajando en paralelo y un
integrador. El tiempo de instalaciÃ³n o de resolver autenticaciÃ³n se trata antes
del sprint. El CRUD aÃºn estÃ¡ pendiente en esta base.

## Agenda de la hora

| Minutos | Actividad | Responsable | Resultado |
| --- | --- | --- | --- |
| 00-05 | Acordar alcance y revisar build/servidor | Integrador | Objetivo y bloqueos visibles |
| 05-12 | Integrar modelo, interfaces y Fault comunes | Integrador | Commit base que compila |
| 12-32 | Implementar tareas A, B y C en paralelo | Tres colaboradores | Tres PR pequeÃ±os |
| 32-42 | Revisar, integrar y ejecutar build | Integrador y autores | main compila y pasa pruebas |
| 42-55 | Desplegar y ejecutar Boomerang | Integrador con C | CRUD y Fault observados |
| 55-60 | Guardar evidencias y cerrar tareas | Equipo | Entrega y mejora para la siguiente sesiÃ³n |

Hay una reuniÃ³n inicial de cinco minutos y una revisiÃ³n final de cinco.
Durante el desarrollo, comunicar solo cambios, bloqueos o dependencias.
No se necesita una reuniÃ³n diaria para una sesiÃ³n de una hora.

**Punto de control en minuto 12:** nadie comienza implementaciones paralelas
sin el commit de interfaces compartidas. Si falla, se corrige esa base primero
y se comunica la nueva previsiÃ³n, en vez de producir tres contratos distintos.

## Tablero mÃ­nimo de GitHub

Estados: **Por hacer -> En curso -> En revisiÃ³n -> Hecho**.
Usar un milestone `Sprint 1 - Flota SOAP - 60 min`.
Un GitHub Project es opcional: si configurarlo consume tiempo, usar Issues,
el milestone y sus enlaces de PR durante esta primera hora.

Cada issue tiene: objetivo, un responsable, archivos permitidos, dependencias,
criterios de aceptaciÃ³n y evidencia. Usar la plantilla del repositorio.
Los cÃ³digos FL-01, FL-02, etc. son identificadores del backlog; no son nÃºmeros
reales de issues hasta crearlas en GitHub.

LÃ­mite de trabajo en curso: una tarea activa por colaborador.
El integrador controla los archivos comunes: pom, contrato, modelo e interfaces.
Dividir por componentes evita que tres agentes editen simultÃ¡neamente FlotaWS.

## Ramas y revisiÃ³n

Usar GitHub flow con una rama principal `main` y ramas cortas por tarea:

- `feature/fl-04-repository`
- `feature/fl-05-application`
- `feature/fl-06-soap`

Cada PR enlaza su issue mediante `Closes #numero-real`.
El autor indica quÃ© cambiÃ³, comando de validaciÃ³n y resultado observado.
El integrador revisa contrato, comportamiento y alcance; despuÃ©s integra.
La revisiÃ³n por agente ayuda, pero el responsable humano conserva la decisiÃ³n.

La primera carga de la base es el punto de partida. Los cambios posteriores
se integran por PR. Configurar una regla de main con revisiÃ³n y check `build`
cuando la primera CI exista; documentar si el plan de GitHub limita esa regla.
No afirmar que main estÃ¡ protegida hasta verificar la configuraciÃ³n.

## Aislamiento para varios agentes

Cada agente usa su propia carpeta, rama y contexto de conversaciÃ³n. Compartir
una carpeta mientras se cambia de rama afecta los archivos de todos los agentes.

Tras el primer commit, el integrador puede crear worktrees desde la raÃ­z:

```powershell
git worktree add -b feature/fl-04-repository ../flota-agente-a main
git worktree add -b feature/fl-05-application ../flota-agente-b main
git worktree add -b feature/fl-06-soap ../flota-agente-c main
```

En otras computadoras, cada programador usa su propio clon.
Los worktrees comparten el historial Git y mantienen carpetas y ramas separadas.
No usar una carpeta sincronizada por OneDrive como mecanismo para fusionar cÃ³digo:
la integraciÃ³n entre equipos ocurre por Git. Esta ubicaciÃ³n puede conservar la
entrega del curso; para trabajo habitual, un clon fuera de carpetas sincronizadas
reduce interferencias de sincronizaciÃ³n.

Solo el integrador despliega en el Payara compartido durante esta hora.
Para despliegues paralelos posteriores, usar dominios y puertos distintos.

## CÃ³mo entregar una tarea a cualquier agente

```text
Tarea: FL-04 - repositorio de vehÃ­culos en memoria.
Lee AGENTS.md y docs/02_CONTRATO_Y_ALCANCE.md.
Base: <commit de interfaces>; rama: feature/fl-04-repository.
Edita solo repository/** y sus pruebas.
Implementa listar, buscar, insertar, actualizar y eliminar segÃºn la interfaz.
Genera IDs Ãºnicos bajo el bloqueo compartido y devuelve copias defensivas.
AceptaciÃ³n: CRUD correcto, lista vacÃ­a, eliminaciÃ³n sin reutilizar ID,
mantenimientos completos y altas simultÃ¡neas sin ID duplicado.
Ejecuta mvn verify. Entrega PR, resultados y cualquier bloqueo.
Si necesitas cambiar una firma compartida, comunica el cambio al integrador.
Presupuesto: 20 minutos.
```

Esta estructura sirve para distintas herramientas. No depende de que todos usen
el mismo proveedor. Si una herramienta no lee AGENTS.md automÃ¡ticamente, pasar
explÃ­citamente su contenido y los archivos de referencia.

## QuÃ© significa Hecho

- Se cumplen los criterios de la issue.
- La rama integrada compila y pasa las pruebas de comportamiento existentes.
- El cambio fue revisado y el contrato coincide.
- Para el endpoint, se observÃ³ el WSDL y la peticiÃ³n/respuesta en Payara.
- La demo final muestra cinco operaciones y los Fault requeridos.
- Se guardaron evidencias y se anotÃ³ cualquier pendiente real.

## Cuando el reloj se retrasa

Al minuto 32 deben existir entregas integrables. Si una tarea sigue bloqueada,
el integrador reduce cambios adicionales y ayuda en ese componente.
Mantener el mÃ­nimo del PDF: CRUD, lista estÃ¡tica, mantenimientos y Fault.
Posponer mejoras de formato, reglas nuevas, BD, autenticaciÃ³n, frontend y
automatizaciones adicionales. Si el mÃ­nimo no funciona, registrar el bloqueo;
no marcarlo como completo por agotar el tiempo.

Referencias: [GitHub flow](https://docs.github.com/en/get-started/using-github/github-flow),
[worktrees de Git](https://git-scm.com/docs/git-worktree).
