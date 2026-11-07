package com.mycompany.taller_soap_flota.exception;

import jakarta.xml.ws.WebFault;
import java.io.Serial;

/** Excepcion de negocio declarada que JAX-WS puede mapear a un SOAP Fault. */
@WebFault(name = "VehiculoNoEncontradoFault",
        targetNamespace = "http://service.taller_soap_flota.mycompany.com/",
        faultBean = "com.mycompany.taller_soap_flota.exception.VehiculoFault")
public class VehiculoNoEncontradoException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    private final VehiculoFault faultInfo;

    public VehiculoNoEncontradoException(int id) {
        this("El vehículo con ID " + id
                + " no se encuentra registrado en la flota.",
                new VehiculoFault(id, "El vehículo con ID " + id
                        + " no se encuentra registrado en la flota."));
    }

    public VehiculoNoEncontradoException(String message, VehiculoFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public VehiculoNoEncontradoException(String message, VehiculoFault faultInfo,
            Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    public VehiculoFault getFaultInfo() {
        return faultInfo;
    }
}
