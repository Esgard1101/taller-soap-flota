package com.mycompany.taller_soap_flota.repository;

import com.mycompany.taller_soap_flota.model.Vehiculo;
import java.util.List;
import java.util.Optional;

/**
 * Contrato del almacenamiento. La implementacion genera IDs y protege las
 * operaciones completas sobre la lista compartida. Sus retornos son copias.
 */
public interface VehiculoRepository {

    List<Vehiculo> listar();

    Optional<Vehiculo> buscarPorId(int id);

    Vehiculo insertar(String placa, String modelo, String estado,
            List<String> mantenimientos);

    /** Reemplaza todos los datos, incluido el historial; vacio si no existe. */
    Optional<Vehiculo> actualizar(int id, String placa, String modelo,
            String estado, List<String> mantenimientos);

    /** Devuelve false si el ID no existe. */
    boolean eliminar(int id);
}
