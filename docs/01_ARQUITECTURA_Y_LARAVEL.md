# De Laravel a Java Web SOAP

Tu experiencia con Laravel sirve para separar transporte, reglas y almacenamiento.
En este caso, Payara administra el ciclo de vida del servicio y genera el XML
del contrato a partir de clases y anotaciones Java.

## Equivalencias prÃ¡cticas

| En Laravel | En este proyecto |
| --- | --- |
| `composer.json` | `pom.xml`: dependencias, plugins y versiÃ³n Java. |
| `composer install` | Maven resuelve dependencias; `mvn verify` compila, prueba y empaqueta. |
| `Controller` y rutas | `service/FlotaWS` con anotaciones SOAP; el WSDL describe operaciones y parÃ¡metros. |
| Servicio de aplicaciÃ³n | `application/FlotaApplicationService`: casos de uso. |
| Modelo de datos | `model/Vehiculo`: POJO con getters, setters y constructor vacÃ­o. |
| Eloquent / repositorio | `repository/VehiculoRepository` y su implementaciÃ³n en memoria; no es Active Record. |
| JSON serialization | JAXB transforma objetos y XML. `Serializable` se conserva por requisito acadÃ©mico. |
| Exceptions y handler | ExcepciÃ³n Java declarada que JAX-WS representa como SOAP Fault. |
| `artisan serve` | Desplegar un WAR en Payara desde NetBeans o asadmin. |
| Postman | Boomerang importando el WSDL del servicio. |
| `.env` | Variables del proceso y configuraciÃ³n del servidor; un archivo `.env` no se carga solo. |
| `config/database.php` | En una futura versiÃ³n con BD: datasource de Payara y JPA/`persistence.xml`. |

`Serializable` no es el mecanismo que convierte a XML: ese trabajo es de JAXB.
JAX-WS es la especificaciÃ³n de servicios; Payara aporta su implementaciÃ³n.

## Recorrido de una peticiÃ³n

```mermaid
flowchart LR
    B[Boomerang: XML SOAP] --> W[service: FlotaWS]
    W --> A[application: FlotaApplicationService]
    A --> R[repository: VehiculoRepository]
    R --> M[Lista estÃ¡tica en memoria]
    A --> F[ExcepciÃ³n declarada]
    F --> X[SOAP Fault]
```

- **service:** protocolo, anotaciones, nombres pÃºblicos y exposiciÃ³n de las cinco operaciones.
- **application:** coordina CRUD y errores; no depende de Boomerang.
- **repository:** almacena, busca, actualiza, elimina y protege el acceso compartido.
- **model:** representa los datos que tambiÃ©n se transmiten mediante JAXB.
- **exception:** excepciÃ³n y detalle estructurado del error.

Para este tamaÃ±o alcanza un solo mÃ³dulo Maven organizado por paquetes.
Separar mÃ³dulos Maven o microservicios se evalÃºa cuando existan lÃ­mites de negocio
y despliegue que lo justifiquen.

## ConfiguraciÃ³n por responsabilidad

| Tipo | DÃ³nde vive | Se versiona |
| --- | --- | --- |
| Dependencias y release 21 | `pom.xml` | SÃ­ |
| Plantilla de rutas de herramientas | `config/local.example.ps1` | SÃ­ |
| Rutas reales de cada equipo | `config/local.ps1` | No |
| Puerto HTTP, dominio y JDK del servidor | InstalaciÃ³n local de Payara | Se documenta; no copiar el dominio al repo |
| Nombres SOAP, namespace, firmas | Java y documento de contrato | SÃ­ |
| Futuras claves o credenciales | Entorno / configuraciÃ³n segura del servidor | No |

`config/local.ps1` lo carga explÃ­citamente `scripts/verify.ps1`.
Sus variables ayudan a la terminal, pero no cambian el JDK de un Payara que ya estÃ¡ ejecutÃ¡ndose.
Para futuras propiedades de aplicaciÃ³n se puede usar MicroProfile Config de Payara,
con variables de entorno y `META-INF/microprofile-config.properties`; documentar
la precedencia y las claves cuando se incorporen.

En una futura versiÃ³n con BD, el cÃ³digo usarÃ­a un datasource JNDI, por ejemplo
`jdbc/FlotaDS`; las credenciales se configurarÃ­an en el servidor. Este caso usa
memoria y no necesita conexiÃ³n a una base de datos.

## Detalle que cambia la implementaciÃ³n

`@Stateless` no serializa el acceso a una lista estÃ¡tica entre peticiones.
El repositorio debe proteger cada operaciÃ³n completa con un bloqueo compartido
por todas sus instancias. Generar ID y agregar deben ser una operaciÃ³n indivisible.
Una lista sincronizada por sÃ­ sola no protege secuencias de buscar y actualizar.

Retornar copias defensivas de vehÃ­culos y de sus mantenimientos impide modificar
el almacenamiento fuera del bloqueo. Los datos se reinician al redesplegar y no
se comparten entre varias instancias del servidor. La arquitectura puede crecer
sustituyendo el repositorio, pero la lista en memoria no escala horizontalmente.

Referencia: [desarrollo SOAP en Payara](https://docs.azul.com/payara-community/technical-documentation/application-development/developing-soap-webservices.html).
