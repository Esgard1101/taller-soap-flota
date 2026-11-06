# Entorno y primera compilaciÃ³n

## Objetivo del curso

| Herramienta | Acuerdo |
| --- | --- |
| NetBeans | 30 o superior |
| Java | Eclipse Temurin JDK 21, distribuciÃ³n portable zip segÃºn el PDF |
| Servidor | Payara Server Community 7.x |
| CompilaciÃ³n | Maven, release 21 |
| Dependencia | jakarta.jakartaee-api 10.0.0 provided |
| Empaquetado | WAR |

No confundir la versiÃ³n de Java, la del API y la del servidor: son ejes distintos.
El POM fija el API 10 que exige el ejercicio, aunque Payara 7 se basa en Jakarta
EE 11. SOAP ya no es requisito de la plataforma Jakarta EE 11; se comprueba que
la distribuciÃ³n de Payara elegida incluya y ejecute su soporte SOAP.

No introducir una implementaciÃ³n Metro completa en el WAR por defecto:
primero verificar los mÃ³dulos provistos por Payara para evitar duplicarlos.

## Observado en el equipo de preparaciÃ³n (2026-11-06)

- Log de NetBeans: versiÃ³n 30, ejecutÃ¡ndose con Temurin 21.0.12.1.
- Maven de NetBeans: 3.9.15.
- Servidor registrado: C:\payara7; dominio domain1.
- Branding de esa instalaciÃ³n: Payara 7.2026.10, build 18.
- MÃ³dulos locales presentes: webservices-api-osgi, webservices-connector,
  webservices-osgi y soap-tcp.
- El jar Jakarta EE API 10 de la cachÃ© contiene WebService, WebFault,
  Stateless y XmlType.

La instalaciÃ³n de Temurin observada estÃ¡ en Program Files; esto no prueba el
uso de una distribuciÃ³n portable zip. Para cumplir ese detalle formal del PDF,
registrar el JDK portable solicitado cuando se prepare la entrega.

Estas observaciones no verifican el despliegue de FlotaWS, que estÃ¡ pendiente.

## Diferencias con el proyecto previo

El POM inspeccionado del proyecto previo tenÃ­a Jakarta EE 11.0.0-M1 y
source/target 17. La base de este repositorio usa API 10.0.0 y release 21.
El proyecto anterior permanece separado. Abrir la carpeta de este repositorio
en NetBeans para trabajar con la base nueva.

## Terminal

1. Copiar config/local.example.ps1 a config/local.ps1.
2. Ajustar JAVA_HOME a la raÃ­z del JDK 21 y MAVEN_HOME a Maven.
3. Ejecutar `.\scripts\verify.ps1`.

Si todas las dependencias ya estÃ¡n en la cachÃ©, se puede usar
`.\scripts\verify.ps1 -Offline`.
La primera resoluciÃ³n online puede tomar tiempo y debe hacerse antes del sprint.
El script no cambia configuraciones globales ni despliega el WAR.

En Linux o en la CI: `mvn --batch-mode --no-transfer-progress verify`,
con JDK 21 seleccionado.

## NetBeans

- File > Open Project: seleccionar la carpeta que contiene pom.xml.
- Tools > Java Platforms: registrar el JDK 21.
- Services > Servers: registrar Payara 7.x si aÃºn no aparece.
- Project Properties > Run: seleccionar Payara y el dominio de trabajo.
- Revisar el JDK usado por el servidor, ademÃ¡s del JDK del proyecto.
- Build / Run cuando las clases del servicio estÃ©n implementadas.

La configuraciÃ³n compartida no fija rutas absolutas del servidor de una persona.
El WAR estable es target/taller_soap_flota.war.

## QuÃ© verifica cada control

- Maven verify: compilaciÃ³n, pruebas existentes y WAR.
- CI preparada: el mismo ciclo con Temurin 21; no inicia Payara.
- Despliegue local: resoluciÃ³n EJB/CDI y compatibilidad del runtime.
- WSDL y Boomerang: contrato, serializaciÃ³n, operaciones y Fault reales.

ValidaciÃ³n inicial observada: verify offline terminÃ³ con BUILD SUCCESS y generÃ³
el WAR. No habÃ­a pruebas de negocio ni endpoint implementados; no se interpreta
esa compilaciÃ³n como aceptaciÃ³n del caso.

Referencias: [Payara SOAP](https://docs.azul.com/payara-community/technical-documentation/application-development/developing-soap-webservices.html),
[Jakarta EE 11](https://jakarta.ee/release/11/),
[Actions checkout](https://github.com/actions/checkout),
[Actions setup-java](https://github.com/actions/setup-java).
