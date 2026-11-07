package com.mycompany.taller_soap_flota.model;

import jakarta.xml.bind.JAXBContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VehiculoSerializationTest {

    @Test
    void jaxbConservaTodosLosDatosYVariosMantenimientos() throws Exception {
        var original = new Vehiculo(7, "ABC-123", "Volvo FH", "En taller",
                List.of("Cambio de aceite", "Alineamiento"));

        Vehiculo recibido = xmlRoundTrip(original);

        assertAll(
                () -> assertEquals(original.getId(), recibido.getId()),
                () -> assertEquals(original.getPlaca(), recibido.getPlaca()),
                () -> assertEquals(original.getModelo(), recibido.getModelo()),
                () -> assertEquals(original.getEstado(), recibido.getEstado()),
                () -> assertEquals(original.getMantenimientos(),
                        recibido.getMantenimientos()));
    }

    @Test
    void jaxbPermiteHistorialVacioSinConvertirloEnNull() throws Exception {
        var original = new Vehiculo(1, "XYZ-456", "Camioneta", "Disponible", null);

        Vehiculo recibido = xmlRoundTrip(original);

        assertNotNull(recibido.getMantenimientos());
        assertEquals(List.of(), recibido.getMantenimientos());
    }

    @Test
    void serializacionJavaConservaElHistorial() throws Exception {
        var original = new Vehiculo(3, "DEF-789", "Camion", "En ruta",
                List.of("Cambio de frenos"));
        var bytes = new ByteArrayOutputStream();
        try (var output = new ObjectOutputStream(bytes)) {
            output.writeObject(original);
        }

        Vehiculo recibido;
        try (var input = new ObjectInputStream(
                new ByteArrayInputStream(bytes.toByteArray()))) {
            recibido = (Vehiculo) input.readObject();
        }

        assertEquals(original.getId(), recibido.getId());
        assertEquals(original.getMantenimientos(), recibido.getMantenimientos());
    }

    private static Vehiculo xmlRoundTrip(Vehiculo original) throws Exception {
        var context = JAXBContext.newInstance(Vehiculo.class);
        var xml = new StringWriter();
        context.createMarshaller().marshal(original, xml);
        return (Vehiculo) context.createUnmarshaller()
                .unmarshal(new StringReader(xml.toString()));
    }
}
