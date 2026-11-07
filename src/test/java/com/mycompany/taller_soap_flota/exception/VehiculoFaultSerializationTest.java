package com.mycompany.taller_soap_flota.exception;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.ws.WebFault;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.Test;
import org.xml.sax.InputSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VehiculoFaultSerializationTest {

    @Test
    void detalleXmlCoincideConElContratoDeLaExcepcion() throws Exception {
        var exception = new VehiculoNoEncontradoException(999);
        var annotation = VehiculoNoEncontradoException.class
                .getAnnotation(WebFault.class);
        var context = JAXBContext.newInstance(VehiculoFault.class);
        var xml = new StringWriter();
        context.createMarshaller().marshal(exception.getFaultInfo(), xml);

        var factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        var root = factory.newDocumentBuilder()
                .parse(new InputSource(new StringReader(xml.toString())))
                .getDocumentElement();
        assertEquals(annotation.name(), root.getLocalName());
        assertEquals(annotation.targetNamespace(), root.getNamespaceURI());
        assertEquals(VehiculoFault.class.getName(), annotation.faultBean());

        var recibido = (VehiculoFault) context.createUnmarshaller()
                .unmarshal(new StringReader(xml.toString()));
        assertEquals(999, recibido.getId());
        assertEquals(exception.getMessage(), recibido.getMensaje());
    }
}
