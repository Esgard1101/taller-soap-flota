# Contrato y alcance v0.1

Este acuerdo permite repartir el trabajo. Es una propuesta del equipo basada en
el PDF, no un WSDL escrito a mano. El enfoque obligatorio es **code-first**:
Java y sus anotaciones producen el WSDL; se comprueba el resultado en Payara.

## Requisitos del enunciado

- NetBeans 30+, Eclipse Temurin JDK 21 portable, Payara Community 7.x.
- Maven WAR y `jakarta.jakartaee-api:10.0.0` con `scope=provided`.
- `model.Vehiculo implements Serializable`, atributos privados, constructor vacío.
- Campos: `int id`, `String placa`, `String modelo`, `String estado`,
  `List<String> mantenimientos`.
- `service.FlotaWS` con `@Stateless` y `@WebService`.
- Cinco operaciones con `@WebMethod` y nombres de entrada explícitos mediante `@WebParam`.
- Lista estática en memoria e ID autoincremental generado por el servidor.
- Buscar, actualizar y eliminar un ID inexistente deben producir SOAP Fault.
- Entrega de código, URL WSDL y capturas de las cinco operaciones y el error en Boomerang.

## Decisiones propuestas para la implementación

Paquete base: `com.mycompany.taller_soap_flota`.
Endpoint SOAP: clase `service.FlotaWS`.
Namespace propuesto: `http://service.taller_soap_flota.mycompany.com/`.
Nombre de servicio propuesto: `FlotaWS`; nombre de puerto: `FlotaWSPort`.
Preferir SOAP 1.1 document/literal wrapped para la demo.
Todos se deben fijar explícitamente y contrastar con el WSDL generado.

| Operación | Entradas en orden | Retorno propuesto |
| --- | --- | --- |
| `listarVehiculos` | Ninguna | `List<Vehiculo>` |
| `buscarVehiculo` | `int id` | `Vehiculo` |
| `registrarVehiculo` | `String placa, String modelo, String estado, List<String> mantenimientos` | `Vehiculo` con ID asignado |
| `actualizarVehiculo` | `int id, String placa, String modelo, String estado, List<String> mantenimientos` | `Vehiculo` actualizado |
| `eliminarVehiculo` | `int id` | `boolean` (`true` al eliminar; Fault si no existe) |

El PDF no fija todos los retornos ni la semántica de edición del historial.
Decidimos que actualizar sustituya placa, modelo, estado e historial completos.
Un historial nulo se normaliza a una lista vacía en registro y actualización.
Para conservarlo al editar, el cliente debe enviar los elementos existentes.
Estas decisiones deben constar en el WSDL y la guía de uso, no quedar implícitas.

El endpoint depende de la interfaz application.FlotaApplication, con estas mismas
firmas. Su implementación es FlotaApplicationService. La
inyección propuesta es CDI `@Inject`, con implementaciones de aplicación y
repositorio `@ApplicationScoped`; FlotaWS conserva su ciclo EJB `@Stateless`.

## Contrato del repositorio

Antes del trabajo paralelo, el integrador incorpora `Vehiculo`, las excepciones
y esta interfaz para que todas las ramas compilen contra las mismas firmas:

```java
public interface VehiculoRepository {
    List<Vehiculo> listar();
    Optional<Vehiculo> buscarPorId(int id);
    Vehiculo insertar(String placa, String modelo, String estado,
                      List<String> mantenimientos);
    Optional<Vehiculo> actualizar(int id, String placa, String modelo,
                                 String estado, List<String> mantenimientos);
    boolean eliminar(int id);
}
```

La implementación `VehiculoRepositoryMemoria` encapsula la lista estática,
un contador que comienza en 1 y un bloqueo estático compartido.
`insertar` asigna y persiste el ID bajo el mismo bloqueo.
No se reutilizan IDs eliminados. El repositorio devuelve copias defensivas.
La aplicación traduce Optional vacío o eliminación falsa a la excepción del caso.

## Fault acordado

Propuesta: `VehiculoNoEncontradoException extends Exception`, declarada en
`buscarVehiculo`, `actualizarVehiculo` y `eliminarVehiculo`.
Usar `@WebFault` y un detalle JAXB `VehiculoFault` con `id` y `mensaje`
para obtener un contrato de error explícito y estable.

Mensaje propuesto: `El vehículo con ID <id> no se encuentra registrado en la flota.`

No devolver ese mensaje como resultado exitoso ni transformar el error en
`null` o en `false` desde el endpoint. Los prefijos XML son variables:
se comprueba el elemento Fault y su namespace, no el texto literal del prefijo.

## Criterios transversales

- Estado inicial: lista vacía; un redeploy reinicia los datos.
- Manejar al menos los estados de ejemplo: Disponible, En ruta y En taller.
- No agregar reglas de unicidad de placa, límites de flota ni validaciones de formato
  como obligaciones del PDF. Si se solicitan, crear una tarea y definir su Fault.
- La colección de mantenimientos se prueba con varios valores y con lista vacía.
- IDs de peticiones simultáneas no se repiten; toda modificación es indivisible.
- Serialización JAXB válida, sin exponer campos internos del almacenamiento.

## Antes de repartir

- [ ] Integrar modelo, excepción, detalle de Fault e interfaz de repositorio.
- [ ] Fijar nombres, entradas, retornos y firmas del servicio de aplicación.
- [ ] Conseguir build reproducible para todos los colaboradores.
- [ ] Asignar propietario de cada archivo compartido.

Al finalizar el endpoint, guardar WSDL y todos sus XSD importados en `contracts/`.
Documentar URL local y commit de captura. El código es la fuente y el snapshot
permite revisar cambios del contrato; Boomerang usa el WSDL realmente desplegado.
