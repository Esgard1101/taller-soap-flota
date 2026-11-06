# Backlog para la sesiÃ³n de 60 minutos

## PreparaciÃ³n disponible

- [x] FL-01: base Maven WAR con Java 21 y Jakarta EE API 10.0.0 provided.
- [x] FL-02: arquitectura, contrato propuesto, reglas de agentes y plantillas.
- [x] Repositorio pÃºblico creado: https://github.com/Esgard1101/taller-soap-flota
- [ ] Integrar las clases e interfaces comunes antes del trabajo paralelo.
- [ ] Implementar y comprobar el CRUD real.

Los tiempos siguientes son posiciones dentro de una sesiÃ³n de una hora,
no fechas ni garantÃ­as de duraciÃ³n. Los responsables A/B/C son roles por asignar.

## Sprint 1: CRUD SOAP y demostraciÃ³n

| CÃ³digo | Ventana | DueÃ±o | Archivos | AceptaciÃ³n |
| --- | --- | --- | --- | --- |
| FL-03 Base compartida | 05-12 | Integrador | model, exception, interfaces de repository y application | Vehiculo serializable con constructor vacÃ­o; Fault JAXB; interfaces comunes; compila |
| FL-04 Repositorio | 12-32 | A | repository y sus pruebas | CRUD sobre lista estÃ¡tica; ID autoincremental; bloqueo compartido; copias defensivas; pruebas de concurrencia |
| FL-05 Casos de uso | 12-32 | B | implementaciÃ³n application y sus pruebas | Cinco operaciones; normaliza lista nula; convierte ausencia en excepciÃ³n en buscar/editar/eliminar |
| FL-06 Endpoint SOAP | 12-32 | C | service | FlotaWS con anotaciones; parÃ¡metros pÃºblicos explÃ­citos; delega por interfaz; Fault declarado; compila |
| FL-07 IntegraciÃ³n | 32-42 | Integrador con A/B/C | revisiÃ³n y resoluciÃ³n de conflictos | Un solo contrato; main pasa verify; componentes CDI resolubles |
| FL-08 Demo y cierre | 42-60 | Integrador con C | contracts, evidencias y notas | WSDL accesible; 5 operaciones y 3 Fault; capturas y commit de entrega |

### FL-03: condiciÃ³n para paralelizar

El integrador implementa el POJO Vehiculo, VehiculoFault,
VehiculoNoEncontradoException, VehiculoRepository y **FlotaApplication**.
Esta Ãºltima interfaz declara las cinco firmas pÃºblicas del documento de contrato.

B implementa `FlotaApplicationService implements FlotaApplication`.
C inyecta `FlotaApplication`, y B inyecta `VehiculoRepository`.
A y B anotan sus implementaciones con `@ApplicationScoped`.
AsÃ­ cada rama puede compilar sin copiar la implementaciÃ³n de otra.
La resoluciÃ³n de inyecciÃ³n se verifica al reunirlas y desplegar.

No implementar mÃ©todos ficticios de Ã©xito para simular que el CRUD estÃ¡ listo.
NingÃºn agente cambia las interfaces por su cuenta.

### Pruebas mÃ­nimas por responsable

**A:** registrar/listar/buscar/actualizar/eliminar; lista vacÃ­a; historial
independiente del original; ID eliminado no reutilizado; altas simultÃ¡neas sin
duplicados usando un ejecutor de pruebas fuera del servidor.

**B:** propagar datos y mantenimientos; bÃºsqueda inexistente; actualizaciÃ³n
inexistente; eliminaciÃ³n inexistente. Un repositorio falso pequeÃ±o permite
probar aplicaciÃ³n sin depender de la rama A.

**C e integrador:** cinco operaciones en el WSDL, entradas nombradas, XML
vÃ¡lido con varios mantenimientos y Fault nativo en las tres operaciones.
Una prueba unitaria de B no sustituye esa verificaciÃ³n del protocolo.

AÃ±adir JUnit y sus dependencias una sola vez desde la base comÃºn si se van a usar
en esta sesiÃ³n. Evitar que cada agente modifique pom.xml por separado.

## Orden de integraciÃ³n

1. FL-03 en main y difusiÃ³n del commit base.
2. Abrir worktrees A/B/C desde ese commit.
3. Revisar e integrar FL-04, FL-05 y FL-06.
4. Ejecutar verify y desplegar el WAR integrado.
5. Ejecutar FL-08 con la secuencia de Boomerang.

## Fuera de esta hora

BD/JPA, frontend, JWT, Docker, despliegue en nube, microservicios, validaciÃ³n
estricta de placa, auditorÃ­a y pipelines de despliegue.
Se incorporan como historias futuras cuando el alcance lo requiera.

## RevisiÃ³n final de cinco minutos

Registrar tres datos: quÃ© se terminÃ³, quÃ© sigue abierto y quÃ© cambiar en la
siguiente sesiÃ³n. Una mejora suficiente: reducir un conflicto, aclarar una
firma o automatizar un paso que consumiÃ³ tiempo.
