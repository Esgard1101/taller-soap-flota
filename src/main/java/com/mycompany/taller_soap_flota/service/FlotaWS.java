package com.mycompany.taller_soap_flota.service;

import com.mycompany.taller_soap_flota.application.FlotaApplication;
import com.mycompany.taller_soap_flota.exception.VehiculoNoEncontradoException;
import com.mycompany.taller_soap_flota.model.Vehiculo;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.BindingType;
import java.util.List;

/** Endpoint SOAP que delega los casos de uso a la aplicacion. */
@Stateless
@WebService(name = "FlotaWS", serviceName = "FlotaWS", portName = "FlotaWSPort",
        targetNamespace = "http://service.taller_soap_flota.mycompany.com/")
@BindingType(jakarta.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT,
        use = SOAPBinding.Use.LITERAL,
        parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class FlotaWS {

    @Inject
    private FlotaApplication application;

    @WebMethod(operationName = "listarVehiculos")
    public List<Vehiculo> listarVehiculos() {
        return application.listarVehiculos();
    }

    @WebMethod(operationName = "buscarVehiculo")
    public Vehiculo buscarVehiculo(@WebParam(name = "id") int id)
            throws VehiculoNoEncontradoException {
        return application.buscarVehiculo(id);
    }

    @WebMethod(operationName = "registrarVehiculo")
    public Vehiculo registrarVehiculo(
            @WebParam(name = "placa") String placa,
            @WebParam(name = "modelo") String modelo,
            @WebParam(name = "estado") String estado,
            @WebParam(name = "mantenimientos") List<String> mantenimientos) {
        return application.registrarVehiculo(placa, modelo, estado, mantenimientos);
    }

    @WebMethod(operationName = "actualizarVehiculo")
    public Vehiculo actualizarVehiculo(
            @WebParam(name = "id") int id,
            @WebParam(name = "placa") String placa,
            @WebParam(name = "modelo") String modelo,
            @WebParam(name = "estado") String estado,
            @WebParam(name = "mantenimientos") List<String> mantenimientos)
            throws VehiculoNoEncontradoException {
        return application.actualizarVehiculo(id, placa, modelo, estado,
                mantenimientos);
    }

    @WebMethod(operationName = "eliminarVehiculo")
    public boolean eliminarVehiculo(@WebParam(name = "id") int id)
            throws VehiculoNoEncontradoException {
        return application.eliminarVehiculo(id);
    }
}
