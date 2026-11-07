package com.mycompany.taller_soap_flota.application;

import com.mycompany.taller_soap_flota.exception.VehiculoNoEncontradoException;
import com.mycompany.taller_soap_flota.model.Vehiculo;
import com.mycompany.taller_soap_flota.repository.VehiculoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/** Coordina los casos de uso y traduce la ausencia al Fault de negocio. */
@ApplicationScoped
public class FlotaApplicationService implements FlotaApplication {

    @Inject
    VehiculoRepository repository;

    @Override
    public List<Vehiculo> listarVehiculos() {
        return repository.listar();
    }

    @Override
    public Vehiculo buscarVehiculo(int id) throws VehiculoNoEncontradoException {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new VehiculoNoEncontradoException(id));
    }

    @Override
    public Vehiculo registrarVehiculo(String placa, String modelo, String estado,
            List<String> mantenimientos) {
        return repository.insertar(placa, modelo, estado,
                normalizarMantenimientos(mantenimientos));
    }

    @Override
    public Vehiculo actualizarVehiculo(int id, String placa, String modelo,
            String estado, List<String> mantenimientos)
            throws VehiculoNoEncontradoException {
        return repository.actualizar(id, placa, modelo, estado,
                normalizarMantenimientos(mantenimientos))
                .orElseThrow(() -> new VehiculoNoEncontradoException(id));
    }

    @Override
    public boolean eliminarVehiculo(int id) throws VehiculoNoEncontradoException {
        if (!repository.eliminar(id)) {
            throw new VehiculoNoEncontradoException(id);
        }
        return true;
    }

    private List<String> normalizarMantenimientos(List<String> mantenimientos) {
        return mantenimientos == null ? new ArrayList<>() : new ArrayList<>(mantenimientos);
    }
}
