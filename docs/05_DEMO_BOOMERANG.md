# Demo SOAP en Boomerang

Esta guÃ­a es un plan de prueba. Las capturas y respuestas reales estÃ¡n pendientes
hasta implementar y desplegar FlotaWS.

## PreparaciÃ³n

1. Ejecutar verify y desplegar en Payara el proyecto integrado.
2. Obtener la URL del endpoint desde el despliegue; agregar `?wsdl`.
3. Abrir el WSDL y verificar que describe las cinco operaciones.
4. Importar esa URL en Boomerang y generar las solicitudes desde el contrato.
5. Comprobar direcciÃ³n, namespace, nombres de entrada y versiÃ³n SOAP.
6. Guardar el WSDL y sus XSD importados en contracts/ como evidencia del contrato.

Una URL del estilo `http://localhost:8080/taller_soap_flota/FlotaWS?wsdl`
es solo un ejemplo hasta verificar el mapeo real. No estÃ¡ disponible en la base.

Boomerang admite importar WSDL. Usar las plantillas generadas para evitar
inventar etiquetas, envoltorios de listas o SOAPAction.

## Secuencia para los minutos 42-55

| Paso | PeticiÃ³n | Datos / comprobaciÃ³n |
| --- | --- | --- |
| 01 | listarVehiculos | Flota inicialmente vacÃ­a |
| 02 | registrarVehiculo | Placa ABC-123; modelo Volvo FH; Disponible; Cambio de aceite y Alineamiento |
| 03 | listarVehiculos | Aparece el vehÃ­culo y ambos mantenimientos |
| 04 | buscarVehiculo | Usar el ID devuelto por registrar |
| 05 | actualizarVehiculo | Mismo ID; estado En taller; enviar historial completo que se desea conservar |
| 06 | buscarVehiculo | Persisten el cambio de estado y el historial |
| 07 | eliminarVehiculo | Mismo ID; resultado true |
| 08 | listarVehiculos | Se confirma la baja |
| 09 | buscarVehiculo | ID eliminado: SOAP Fault |
| 10 | actualizarVehiculo | ID eliminado: SOAP Fault |
| 11 | eliminarVehiculo | ID eliminado: SOAP Fault |

No asumir ID 1 si se hicieron registros previos. No redesplegar entre pasos:
la lista es estÃ¡tica y el redeploy elimina sus datos.

## QuÃ© observar

En SOAP 1.1, Envelope y Body pertenecen al namespace
`http://schemas.xmlsoap.org/soap/envelope/`.
El error debe ser un elemento Fault nativo dentro del Body, con cÃ³digo,
mensaje y el detalle acordado. El prefijo puede ser soap, S, SOAP-ENV u otro.
No exigir una cadena literal de prefijo.

Si Boomerang resalta un HTTP 500, revisar el XML: puede ser la respuesta SOAP
Fault esperada. Un fallo de conexiÃ³n, HTML del servidor o traza genÃ©rica no
demuestra el Fault de negocio.

Para mantenimientos, repetir la estructura que genere el WSDL. No enviar
una cadena con elementos separados por comas como sustituto de List<String>.

## Evidencia de entrega

En `evidencias/boomerang/`, guardar capturas reales con nombres:

- 01-listar.png
- 02-registrar.png
- 03-buscar.png
- 04-actualizar.png
- 05-eliminar.png
- 06-fault-buscar.png
- 07-fault-actualizar.png
- 08-fault-eliminar.png

Cada captura muestra operaciÃ³n, peticiÃ³n XML y respuesta XML legibles.
La nota de demo incluye URL WSDL, commit probado, versiÃ³n de Payara/JDK y
orden de ejecuciÃ³n. Se pueden guardar tambiÃ©n request/response XML cuando
la herramienta permita copiarlos. No fabricar respuestas ni capturas.

Referencia: [Boomerang en Chrome Web Store](https://chromewebstore.google.com/detail/boomerang-soap-rest-clien/eipdnjedkpcnlmmdfdkgfpljanehloah).
