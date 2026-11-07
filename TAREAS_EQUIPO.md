# Tareas del equipo: Flota de Vehículos SOAP

> Acuerdo para esta sesión: todos trabajan sobre `main`, con commits pequeños y
> push directo. Cada colaborador usa su propio clon. No se crean ramas de tarea
> ni se exige pull request.

Repositorio: https://github.com/Esgard1101/taller-soap-flota

Objetivo: desarrollar las cinco operaciones SOAP del caso, probarlas en Boomerang
y guardar evidencias. La base Maven ya existe; el CRUD todavía debe implementarse.

## 1. Organización de la hora

| Minutos | Trabajo | Responsable |
| --- | --- | --- |
| 00-05 | Revisar entorno y confirmar el alcance | Integrador |
| 05-12 | Subir modelo, interfaces y excepción comunes | Integrador |
| 12-32 | Desarrollar repositorio, aplicación y endpoint en paralelo | A, B y C |
| 32-42 | Actualizar main, revisar commits y corregir integración | Integrador con el equipo |
| 42-55 | Desplegar en Payara y ejecutar Boomerang | Integrador con C |
| 55-60 | Guardar capturas, WSDL y pendientes | Equipo |

El plazo supone NetBeans, JDK, Payara, Maven y GitHub ya configurados.
Los colaboradores empiezan cuando el integrador comunica el commit de la base común.

## 2. Acuerdos técnicos para todos

- NetBeans 30+, Eclipse Temurin JDK 21 y Payara Community 7.x.
- Maven WAR; `jakarta.jakartaee-api:10.0.0` con alcance `provided`.
- Paquete raíz: `com.mycompany.taller_soap_flota`.
- Almacenamiento en una lista estática; este ejercicio no usa base de datos.
- Operaciones: listarVehiculos, buscarVehiculo, registrarVehiculo,
  actualizarVehiculo y eliminarVehiculo.
- El servidor asigna el ID autoincremental; no se reutilizan IDs eliminados.
- Buscar, actualizar o eliminar un ID inexistente genera SOAP Fault.
- Registro y actualización reciben el historial de mantenimientos.
- Actualizar reemplaza los datos y el historial completos.
- Lista de mantenimientos nula se normaliza como lista vacía.
- Mantener las firmas de [contrato y alcance](docs/02_CONTRATO_Y_ALCANCE.md).
- No modificar archivos asignados a otro colaborador sin coordinarlo.
- Un solo responsable modifica pom.xml y los contratos comunes: el integrador.

Las decisiones de retornos y edición del historial completan aspectos que el
enunciado no detalla. Son el acuerdo de implementación del equipo.

## 3. Integrador: preparar y subir la base común

**Issue:** [FL-03 / #1](https://github.com/Esgard1101/taller-soap-flota/issues/1).
**Antes de A, B y C.**

Crear:

- `model/Vehiculo.java`: implements Serializable, constructor vacío,
  getters/setters, id, placa, modelo, estado y List<String> mantenimientos.
- `exception/VehiculoFault.java`: detalle de error compatible con JAXB.
- `exception/VehiculoNoEncontradoException.java`: excepción declarada,
  anotada con @WebFault y con su detalle accesible.
- `repository/VehiculoRepository.java`: interfaz acordada.
- `application/FlotaApplication.java`: interfaz con las cinco firmas públicas.
- Dependencias de pruebas JUnit en pom.xml, una sola vez.

**Criterio de cierre:** la base compila con JDK 21, las interfaces coinciden con
el contrato y el commit está en main remoto. Comunicar ese hash al equipo.
No llenar métodos con respuestas de éxito ficticias.

## 4. Colaborador A: repositorio en memoria

**Issue:** [FL-04 / #2](https://github.com/Esgard1101/taller-soap-flota/issues/2).

**Archivos propios:**

- `src/main/java/com/mycompany/taller_soap_flota/repository/VehiculoRepositoryMemoria.java`
- `src/test/java/com/mycompany/taller_soap_flota/repository/`

**Trabajo:**

1. Implementar VehiculoRepository como bean CDI @ApplicationScoped.
2. Encapsular una lista estática y un contador de IDs que comience en 1.
3. Implementar listar, buscarPorId, insertar, actualizar y eliminar.
4. Proteger operaciones completas con un bloqueo estático compartido.
   Asignar ID y agregar el vehículo deben ocurrir dentro del mismo bloqueo.
5. Devolver copias de vehículos e historiales para que nadie altere la lista
   fuera del repositorio.
6. Para ausencia, usar Optional vacío o false según la interfaz. La aplicación
   convierte esa ausencia en la excepción de negocio.

**Pruebas:** CRUD completo, flota vacía, historial independiente del original,
ID eliminado no reutilizado y altas concurrentes sin IDs duplicados.

**Commit sugerido:** `feat: implementar repositorio de flota en memoria`.

## 5. Colaborador B: casos de uso y errores

**Issue:** [FL-05 / #3](https://github.com/Esgard1101/taller-soap-flota/issues/3).

**Archivos propios:**

- `src/main/java/com/mycompany/taller_soap_flota/application/FlotaApplicationService.java`
- `src/test/java/com/mycompany/taller_soap_flota/application/`

**Trabajo:**

1. Implementar FlotaApplication mediante FlotaApplicationService,
   con @ApplicationScoped.
2. Inyectar VehiculoRepository con @Inject.
3. Implementar las cinco operaciones usando ese repositorio.
4. Normalizar mantenimientos nulos como lista vacía.
5. En buscar, actualizar y eliminar, lanzar VehiculoNoEncontradoException
   si el ID no existe.
6. No devolver el mensaje de error como si fuera una respuesta exitosa.
7. Probar con un repositorio falso pequeño para no depender de la implementación de A.

**Pruebas:** datos e historial correctos y ausencia de ID en las tres operaciones
obligatorias. Las excepciones deben propagarse al endpoint.

**Commit sugerido:** `feat: implementar casos de uso y errores de flota`.

## 6. Colaborador C: endpoint SOAP

**Issue:** [FL-06 / #4](https://github.com/Esgard1101/taller-soap-flota/issues/4).

**Archivo propio:**

- `src/main/java/com/mycompany/taller_soap_flota/service/FlotaWS.java`

**Trabajo:**

1. Declarar FlotaWS con @Stateless y @WebService.
2. Inyectar FlotaApplication con @Inject.
3. Exponer las cinco operaciones con @WebMethod.
4. Nombrar las entradas con @WebParam: id, placa, modelo, estado y mantenimientos,
   según corresponda a cada operación.
5. Declarar el Fault en buscar, actualizar y eliminar.
6. Delegar a la aplicación; evitar duplicar la lógica de almacenamiento.

**Acuerdo del contrato:**

| Operación | Retorno |
| --- | --- |
| listarVehiculos | List<Vehiculo> |
| buscarVehiculo | Vehiculo |
| registrarVehiculo | Vehiculo con ID generado |
| actualizarVehiculo | Vehiculo actualizado |
| eliminarVehiculo | boolean true; Fault si el ID no existe |

Namespace propuesto: `http://service.taller_soap_flota.mycompany.com/`.
Servicio: `FlotaWS`; puerto: `FlotaWSPort`.
Confirmar estos nombres con el WSDL generado por Payara.

**Criterio de cierre:** compila y, al integrar A/B/C, Payara expone el contrato
con cinco operaciones y el Fault. No dar el protocolo por validado solo por compilar.

**Commit sugerido:** `feat: exponer FlotaWS con operaciones SOAP`.

## 7. Cómo trabajar y subir los commits directamente

### Una copia por colaborador

Cada persona o agente necesita su propia carpeta y acceso de escritura al repositorio.
En una misma computadora, usar tres clones en carpetas diferentes.
No compartir una carpeta mientras varios agentes editan o hacen commits.

Cada colaborador ejecuta una sola vez, desde su carpeta de trabajo:

```powershell
git clone https://github.com/Esgard1101/taller-soap-flota.git
cd taller-soap-flota
git switch main
git pull --ff-only origin main
```

Abrir esa carpeta en NetBeans. El integrador debe haber subido primero el contrato
común. Antes de implementar, leer AGENTS.md y el documento de contrato.

### Antes de subir una tarea

1. Ejecutar la verificación con JDK 21.
2. Revisar los archivos modificados.
3. Agregar solo los archivos propios.
4. Crear el commit.
5. Incorporar los commits que otros ya subieron.
6. Verificar otra vez si la integración incorporó cambios.
7. Hacer push a main.

Ejemplo para **A**, desde la raíz de su clon:

```powershell
mvn --batch-mode --no-transfer-progress verify
git status --short
git diff

git add src/main/java/com/mycompany/taller_soap_flota/repository/VehiculoRepositoryMemoria.java
git add src/test/java/com/mycompany/taller_soap_flota/repository
git commit -m "feat: implementar repositorio de flota en memoria"

git pull --rebase origin main
mvn --batch-mode --no-transfer-progress verify
git push origin main
```

B y C cambian los archivos de git add y el mensaje por los de su tarea.
Si una carpeta de pruebas aún no existe, no ejecutar git add sobre esa ruta.
Si mvn no está en PATH, usar Maven desde NetBeans o scripts/verify.ps1 con
config/local.ps1 ajustado.

**Si falla la compilación o una prueba, corregir antes del push.**
El comando pull --rebase se ejecuta después de guardar los cambios propios
en un commit; no comenzar un rebase con modificaciones sin resolver.

### Si otra persona hizo push antes

Un rechazo por non-fast-forward significa que main remoto avanzó.
No usar push --force. Ejecutar:

```powershell
git pull --rebase origin main
mvn --batch-mode --no-transfer-progress verify
git push origin main
```

### Si aparece un conflicto

1. Abrir el archivo y coordinar con su responsable.
2. Resolver los marcadores conservando el comportamiento acordado.
3. Agregar el archivo resuelto con git add.
4. Continuar el rebase:

```powershell
git rebase --continue
```

Si no se puede resolver con certeza, salir con `git rebase --abort` y avisar al
integrador. Eso conserva los commits propios previos al rebase.
No borrar el trabajo de otro para conseguir que Git acepte el push.

No subir target/, credenciales, archivos de configuración privados ni carpetas
de instalación de Java, NetBeans o Payara.

### Qué comunicar al terminar

```text
Tarea: FL-04
Commit: <hash>
Push: main actualizado
Validación: mvn verify correcto; <casos probados>
Pendientes o bloqueos: <si existen>
```

El integrador revisa los commits ya subidos y coordina cualquier corrección
mediante otro commit. En esta sesión no hay paso obligatorio de pull request.

## 8. Integración y demo

**Issues:** [FL-07 / #5](https://github.com/Esgard1101/taller-soap-flota/issues/5)
y [FL-08 / #6](https://github.com/Esgard1101/taller-soap-flota/issues/6).

El integrador actualiza su clon y revisa la compilación del conjunto:

```powershell
git pull --ff-only origin main
git log --oneline -8
mvn --batch-mode --no-transfer-progress verify
```

Solo el integrador despliega en el Payara compartido durante esta sesión.

**Secuencia de Boomerang:**

1. Abrir la URL WSDL real e importarla en Boomerang.
2. Listar la flota vacía.
3. Registrar con varios mantenimientos y guardar el ID devuelto.
4. Buscar ese ID.
5. Actualizar estado a En taller e historial completo.
6. Buscar y comprobar el cambio.
7. Eliminar y volver a listar.
8. Buscar, actualizar y eliminar un ID inexistente: comprobar tres SOAP Faults.

No redesplegar entre peticiones: la lista en memoria se reinicia.
Guardar WSDL y sus XSD en contracts/, y las capturas reales en evidencias/boomerang/.
Incluir la URL WSDL y el hash del commit demostrado.

## 9. Cuándo está terminado

- Las cinco operaciones funcionan sobre el mismo almacenamiento.
- Se transmiten correctamente los mantenimientos.
- Las tres operaciones de ID inexistente devuelven Fault nativo.
- El código integrado compila y las pruebas pasan.
- Los cambios están en main remoto.
- Existen WSDL y capturas reales de Boomerang.
- Se anotaron los pendientes que queden.

La encuesta de nombres y reserva de puestos del panel queda para una siguiente
versión. Para esta sesión, el integrador comunica quién asume A, B y C.
