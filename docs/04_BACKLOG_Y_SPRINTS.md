# Backlog para la sesión de 60 minutos

## Preparación disponible

- [x] FL-01: base Maven WAR con Java 21 y Jakarta EE API 10.0.0 provided.
- [x] FL-02: arquitectura, contrato propuesto, reglas de agentes y plantillas.
- [x] Repositorio público creado: https://github.com/Esgard1101/taller-soap-flota
- [ ] Integrar las clases e interfaces comunes antes del trabajo paralelo.
- [ ] Implementar y comprobar el CRUD real.

Los tiempos siguientes son posiciones dentro de una sesión de una hora,
no fechas ni garantías de duración. Los responsables A/B/C son roles por asignar.

## Sprint 1: CRUD SOAP y demostración

| Código | Ventana | Dueño | Archivos | Aceptación |
| --- | --- | --- | --- | --- |
| FL-03 Base compartida | 05-12 | Integrador | model, exception, interfaces de repository y application | Vehiculo serializable con constructor vacío; Fault JAXB; interfaces comunes; compila |
| FL-04 Repositorio | 12-32 | A | repository y sus pruebas | CRUD sobre lista estática; ID autoincremental; bloqueo compartido; copias defensivas; pruebas de concurrencia |
| FL-05 Casos de uso | 12-32 | B | implementación application y sus pruebas | Cinco operaciones; normaliza lista nula; convierte ausencia en excepción en buscar/editar/eliminar |
| FL-06 Endpoint SOAP | 12-32 | C | service | FlotaWS con anotaciones; parámetros públicos explícitos; delega por interfaz; Fault declarado; compila |
| FL-07 Integración | 32-42 | Integrador con A/B/C | revisión y resolución de conflictos | Un solo contrato; main pasa verify; componentes CDI resolubles |
| FL-08 Demo y cierre | 42-60 | Integrador con C | contracts, evidencias y notas | WSDL accesible; 5 operaciones y 3 Fault; capturas y commit de entrega |

### FL-03: condición para paralelizar

El integrador implementa el POJO Vehiculo, VehiculoFault,
VehiculoNoEncontradoException, VehiculoRepository y **FlotaApplication**.
Esta última interfaz declara las cinco firmas públicas del documento de contrato.

B implementa `FlotaApplicationService implements FlotaApplication`.
C inyecta `FlotaApplication`, y B inyecta `VehiculoRepository`.
A y B anotan sus implementaciones con `@ApplicationScoped`.
Así cada rama puede compilar sin copiar la implementación de otra.
La resolución de inyección se verifica al reunirlas y desplegar.

No implementar métodos ficticios de éxito para simular que el CRUD está listo.
Ningún agente cambia las interfaces por su cuenta.

### Pruebas mínimas por responsable

**A:** registrar/listar/buscar/actualizar/eliminar; lista vacía; historial
independiente del original; ID eliminado no reutilizado; altas simultáneas sin
duplicados usando un ejecutor de pruebas fuera del servidor.

**B:** propagar datos y mantenimientos; búsqueda inexistente; actualización
inexistente; eliminación inexistente. Un repositorio falso pequeño permite
probar aplicación sin depender de la rama A.

**C e integrador:** cinco operaciones en el WSDL, entradas nombradas, XML
válido con varios mantenimientos y Fault nativo en las tres operaciones.
Una prueba unitaria de B no sustituye esa verificación del protocolo.

Añadir JUnit y sus dependencias una sola vez desde la base común si se van a usar
en esta sesión. Evitar que cada agente modifique pom.xml por separado.

## Orden de integración

1. FL-03 en main y difusión del commit base.
2. Actualizar los clones independientes de A/B/C en main con ese commit.
3. Revisar los commits directos de FL-04, FL-05 y FL-06 en main.
4. Ejecutar verify y desplegar el WAR integrado.
5. Ejecutar FL-08 con la secuencia de Boomerang.

## Fuera de esta hora

BD/JPA, frontend, JWT, Docker, despliegue en nube, microservicios, validación
estricta de placa, auditoría y pipelines de despliegue.
Se incorporan como historias futuras cuando el alcance lo requiera.

## Revisión final de cinco minutos

Registrar tres datos: qué se terminó, qué sigue abierto y qué cambiar en la
siguiente sesión. Una mejora suficiente: reducir un conflicto, aclarar una
firma o automatizar un paso que consumió tiempo.

Acuerdo vigente: [TAREAS_EQUIPO.md](../TAREAS_EQUIPO.md). Se usan commits y push directos a main; no se exige PR.
