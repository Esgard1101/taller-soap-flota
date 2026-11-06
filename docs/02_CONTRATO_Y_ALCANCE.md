# Contrato y alcance v0.1

Este acuerdo permite repartir el trabajo. Es una propuesta del equipo basada en
el PDF, no un WSDL escrito a mano. El enfoque obligatorio es **code-first**:
Java y sus anotaciones producen el WSDL; se comprueba el resultado en Payara.

## Requisitos del enunciado

- NetBeans 30+, Eclipse Temurin JDK 21 portable, Payara Community 7.x.
- Maven WAR y `jakarta.jakartaee-api:10.0.0` con `scope=provided`.
- `model.Vehiculo implements Serializable`, atributos privados, constructor vacÃ­o.
- Campos: `int id`, `String placa`, `String modelo`, `String estado`,
  `List<String> mantenimientos`.
- `service.FlotaWS` con `@Stateless` y `@WebService`.
- Cinco operaciones con `@WebMethod` y nombres de entrada explÃ­citos mediante `@WebParam`.
- Lista estÃ¡tica en memoria e ID autoincremental generado por el servidor.
- Buscar, actualizar y eliminar un ID inexistente deben producir SOAP Fault.
- Entrega de cÃ³digo, URL WSDL y capturas de las cinco operaciones y el error en Boomerang.

## Decisiones propuestas para la implementaciÃ³n

Paquete base: `com.mycompany.taller_soap_flota`.
Endpoint SOAP: clase `service.FlotaWS`.
Namespace propuesto: `http://service.taller_soap_flota.mycompany.com/`.
Nombre de servicio propuesto: `FlotaWS`; nombre de puerto: `FlotaWSPort`.
Preferir SOAP 1.1 document/literal wrapped para la demo.
Todos se deben fijar explÃ­citamente y contrastar con el WSDL generado.

| OperaciÃ³n | Entradas en orden | Retorno propuesto |
| --- | --- | --- |
| `listarVehiculos` | Ninguna | `List<Vehiculo>` |
| `buscarVehiculo` | `int id` | `Vehiculo` |
| `registrarVehiculo` | `String placa, String modelo, String estado, List<String> mantenimientos` | `Vehiculo` con ID asignado |
| `actualizarVehiculo` | `int id, String placa, String modelo, String estado, List<String> mantenimientos` | `Vehiculo` actualizado |
| `eliminarVehiculo` | `int id` | `boolean` (`true` al eliminar; Fault si no existe) |

El PDF no fija todos los retornos ni la semÃ¡ntica de ediciÃ³n del historial.
Decidimos que actualizar sustituya placa, modelo, estado e historial completos.
Un historial nulo se normaliza a una lista vacÃ­a en registro y actualizaciÃ³n.
Para conservarlo al editar, el cliente debe enviar los elementos existentes.
Estas decisiones deben constar en el WSDL y la guÃ­a de uso, no quedar implÃ­citas.

El endpoint llama a un servicio de aplicaciÃ³n con estas mismas firmas. La
inyecciÃ³n propuesta es CDI `@Inject`, con implementaciones de aplicaciÃ³n y
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

La implementaciÃ³n `VehiculoRepositoryMemoria` encapsula la lista estÃ¡tica,
un contador que comienza en 1 y un bloqueo estÃ¡tico compartido.
`insertar` asigna y persiste el ID bajo el mismo bloqueo.
No se reutilizan IDs eliminados. El repositorio devuelve copias defensivas.
La aplicaciÃ³n traduce Optional vacÃ­o o eliminaciÃ³n falsa a la excepciÃ³n del caso.

## Fault acordado

Propuesta: `VehiculoNoEncontradoException extends Exception`, declarada en
`buscarVehiculo`, `actualizarVehiculo` y `eliminarVehiculo`.
Usar `@WebFault` y un detalle JAXB `VehiculoFault` con `id` y `mensaje`
para obtener un contrato de error explÃ­cito y estable.

Mensaje propuesto: `El vehÃ­culo con ID <id> no se encuentra registrado en la flota.`

No devolver ese mensaje como resultado exitoso ni transformar el error en
`null` o en `false` desde el endpoint. Los prefijos XML son variables:
se comprueba el elemento Fault y su namespace, no el texto literal del prefijo.

## Criterios transversales

- Estado inicial: lista vacÃ­a; un redeploy reinicia los datos.
- Manejar al menos los estados de ejemplo: Disponible, En ruta y En taller.
- No agregar reglas de unicidad de placa, lÃ­mites de flota ni validaciones de formato
  como obligaciones del PDF. Si se solicitan, crear una tarea y definir su Fault.
- La colecciÃ³n de mantenimientos se prueba con varios valores y con lista vacÃ­a.
- IDs de peticiones simultÃ¡neas no se repiten; toda modificaciÃ³n es indivisible.
- SerializaciÃ³n JAXB vÃ¡lida, sin exponer campos internos del almacenamiento.

## Antes de repartir

- [ ] Integrar modelo, excepciÃ³n, detalle de Fault e interfaz de repositorio.
- [ ] Fijar nombres, entradas, retornos y firmas del servicio de aplicaciÃ³n.
- [ ] Conseguir build reproducible para todos los colaboradores.
- [ ] Asignar propietario de cada archivo compartido.

Al finalizar el endpoint, guardar WSDL y todos sus XSD importados en `contracts/`.
Documentar URL local y commit de captura. El cÃ³digo es la fuente y el snapshot
permite revisar cambios del contrato; Boomerang usa el WSDL realmente desplegado.
