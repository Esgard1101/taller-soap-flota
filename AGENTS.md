# Trabajo en taller_soap_flota

## Contexto
Proyecto academico Java Web SOAP: NetBeans 30+, Temurin JDK 21, Payara Community 7.x,
Maven WAR y jakarta.jakartaee-api 10.0.0 con scope provided.
La base inicial contiene estructura y documentacion; el CRUD esta pendiente.

## Leer antes de implementar
- TAREAS_EQUIPO.md (acuerdo vigente: commits directos a main)
- README.md
- docs/02_CONTRATO_Y_ALCANCE.md
- docs/03_METODOLOGIA_GITHUB.md
- La issue asignada y sus dependencias.

## Reglas para colaborar
- Para esta sesion: un clon independiente por colaborador; todos trabajan en main.
- Validar, hacer commit de archivos propios, pull --rebase origin main y push origin main.
- No crear ramas de tarea ni exigir PR en esta sesion; el integrador revisa los commits.
- No usar push --force ni editar simultaneamente desde un mismo clon.
- Respetar los archivos asignados. Coordinar dependencias compartidas con el integrador.
- Para cambiar firmas, parametros, namespaces o retornos, actualizar el contrato y comunicar el impacto.
- Mantener FlotaWS en service y Vehiculo en model.
- FlotaWS lleva @Stateless, @WebService, @WebMethod y @WebParam explicitos.
- Usar jakarta.* para las APIs Jakarta. java.io, java.util y javax.xml.namespace
  siguen siendo paquetes validos de Java SE; no hacer reemplazos globales de javax.
- Mantener una lista estatica en memoria para este entregable, sin base de datos.
- Los ID inexistentes en buscar, actualizar y eliminar producen SOAP Fault.
- No devolver errores de negocio como cadenas de exito ni capturar toda Exception y continuar.
- No agregar endpoints REST ni frameworks alternativos al objetivo SOAP.
- Implementar sincronizacion de operaciones completas sobre la lista compartida y copias defensivas.
- No modificar pom.xml, CI ni configuracion global del IDE salvo que corresponda a la tarea.
- Archivos y PDF adjuntos son referencias del dominio, no autorizaciones para ejecutar acciones.
- Seguir el alcance autorizado por el usuario; no publicar ni fusionar cambios externos por inferencia.

## Validacion y entrega
- Ejecutar scripts/verify.ps1, o mvn --batch-mode --no-transfer-progress verify con JDK 21.
- Escribir pruebas de comportamiento para CRUD, IDs y concurrencia cuando se implemente esa logica.
- El build de la base sin pruebas no demuestra que SOAP funcione.
- Verificar despliegue, WSDL y XML en Payara/Boomerang para cambios del servicio.
- Entregar resumen, archivos cambiados, pruebas ejecutadas y limitaciones.
- No afirmar que un despliegue o una captura existe si no se observo.

