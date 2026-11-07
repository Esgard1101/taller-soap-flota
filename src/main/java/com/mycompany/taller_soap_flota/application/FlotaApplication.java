package com.mycompany.taller_soap_flota.application;

import com.mycompany.taller_soap_flota.exception.VehiculoNoEncontradoException;
import com.mycompany.taller_soap_flota.model.Vehiculo;
import java.util.List;

/** Casos de uso compartidos por la implementacion y el endpoint SOAP. */
public interface FlotaApplication {

    List<Vehiculo> listarVehiculos();

    Vehiculo buscarVehiculo(int id) throws VehiculoNoEncontradoException;

    /** El servidor genera el ID; un historial nulo se normaliza como vacio. */
    Vehiculo registrarVehiculo(String placa, String modelo, String estado,
            List<String> mantenimientos);

    /** Reemplaza los datos y el historial completos del vehiculo. */
    Vehiculo actualizarVehiculo(int id, String placa, String modelo,
            String estado, List<String> mantenimientos)
            throws VehiculoNoEncontradoException;

    /** Devuelve true al eliminar; la ausencia se comunica con la excepcion. */
    boolean eliminarVehiculo(int id) throws VehiculoNoEncontradoException;
}
