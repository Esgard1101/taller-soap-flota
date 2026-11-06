# Flota vehicular SOAP

Base de trabajo del escenario 04 de Programación de Aplicaciones Distribuidas.
Proyecto Maven WAR para NetBeans 30+, Temurin JDK 21 y Payara Community 7.x.
El API de compilación es `jakarta.jakartaee-api:10.0.0` con alcance `provided`.

**Estado:** base y metodología preparadas. El CRUD, el endpoint y sus pruebas todavía
no están implementados. El WAR inicial solo valida la estructura de compilación.

## Cómo empezar

1. Abrir esta carpeta en NetBeans con **File > Open Project**.
2. Registrar JDK 21 y seleccionar Payara 7.x en las propiedades del proyecto.
3. Copiar `config/local.example.ps1` a `config/local.ps1` y ajustar rutas.
4. En PowerShell, ejecutar `.\scripts\verify.ps1`.
5. Seguir el backlog; obtener y verificar el WSDL antes de preparar la demo.

También se puede compilar con `mvn --batch-mode --no-transfer-progress verify`.
El resultado se genera en `target/taller_soap_flota.war`.

## Guías del equipo

| Documento | Para qué sirve |
| --- | --- |
| [Arquitectura y Laravel](docs/01_ARQUITECTURA_Y_LARAVEL.md) | Traducir tus conocimientos de backend a Java Web. |
| [Contrato y alcance](docs/02_CONTRATO_Y_ALCANCE.md) | Firmas y decisiones comunes antes de repartir tareas. |
| [Metodología GitHub](docs/03_METODOLOGIA_GITHUB.md) | Issues, ramas, PR, worktrees y entrega entre agentes. |
| [Backlog y sprints](docs/04_BACKLOG_Y_SPRINTS.md) | Orden, dependencias, responsables y aceptación. |
| [Demo Boomerang](docs/05_DEMO_BOOMERANG.md) | Secuencia de pruebas y evidencias del caso. |
| [Entorno](docs/06_ENTORNO.md) | Maven, NetBeans, JDK y Payara. |
| [Instrucciones para agentes](AGENTS.md) | Reglas compartidas del repositorio. |

## Sprint de 60 minutos

5 min de alcance + 7 min de base compartida + 20 min en paralelo +
10 min de integración + 13 min de Boomerang + 5 min de cierre.
Requiere tres colaboradores y el entorno preparado; ver la metodología.

## Flujo propuesto

`Issue -> rama y carpeta propias -> implementación -> mvn verify -> PR -> revisión -> main -> demo`

Tú eres el integrador. Un colaborador se hace responsable de cada tarea.
La CI preparada compila y ejecuta las pruebas que existan; no inicia Payara.
Una compilación exitosa no demuestra que las operaciones SOAP funcionen.

## Entregable del curso

Cinco operaciones SOAP sobre una lista estática de vehículos, IDs autoincrementales,
historial de mantenimientos y SOAP Fault para buscar, actualizar o eliminar IDs
inexistentes. Se entregan el código Maven, la URL WSDL y capturas de Boomerang.

El PDF del enunciado permanece fuera del repositorio. Las decisiones de diseño
adicionales se identifican en el contrato y pueden ajustarse antes de implementar.
