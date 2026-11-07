package com.mycompany.taller_soap_flota.exception;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;

/** Detalle estructurado del Fault para un ID inexistente. */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vehiculoFault",
        namespace = "http://service.taller_soap_flota.mycompany.com/",
        propOrder = {"id", "mensaje"})
@XmlRootElement(name = "VehiculoNoEncontradoFault",
        namespace = "http://service.taller_soap_flota.mycompany.com/")
public class VehiculoFault implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String mensaje;

    public VehiculoFault() {
        // Constructor requerido por JAXB.
    }

    public VehiculoFault(int id, String mensaje) {
        this.id = id;
        this.mensaje = mensaje;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
