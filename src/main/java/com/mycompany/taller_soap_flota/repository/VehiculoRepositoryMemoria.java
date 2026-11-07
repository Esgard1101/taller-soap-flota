
package com.mycompany.taller_soap_flota.repository;

import com.mycompany.taller_soap_flota.model.Vehiculo;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VehiculoRepositoryMemoria implements VehiculoRepository {

    private static final Object LOCK = new Object();
    private static final List<Vehiculo> VEHICULOS = new ArrayList<>();
    private static int siguienteId = 1;

    private Vehiculo copiar(Vehiculo original) {
        return new Vehiculo(
                original.getId(),
                original.getPlaca(),
                original.getModelo(),
                original.getEstado(),
                original.getMantenimientos()
        );
    }

    @Override
    public List<Vehiculo> listar() {
        synchronized (LOCK) {
            List<Vehiculo> resultado = new ArrayList<>();

            for (Vehiculo vehiculo : VEHICULOS) {
                resultado.add(copiar(vehiculo));
            }

            return resultado;
        }
    }

    @Override
    public Optional<Vehiculo> buscarPorId(int id) {
        synchronized (LOCK) {
            for (Vehiculo vehiculo : VEHICULOS) {
                if (vehiculo.getId() == id) {
                    return Optional.of(copiar(vehiculo));
                }
            }
            return Optional.empty();
        }
    }

    @Override
    public Vehiculo insertar(String placa, String modelo,
            String estado, List<String> mantenimientos) {

        synchronized (LOCK) {
            Vehiculo nuevo = new Vehiculo(
                    siguienteId++,
                    placa,
                    modelo,
                    estado,
                    mantenimientos
            );

            VEHICULOS.add(nuevo);
            return copiar(nuevo);
        }
    }

    @Override
    public Optional<Vehiculo> actualizar(int id, String placa,
            String modelo, String estado,
            List<String> mantenimientos) {

        synchronized (LOCK) {
            for (Vehiculo vehiculo : VEHICULOS) {
                if (vehiculo.getId() == id) {
                    vehiculo.setPlaca(placa);
                    vehiculo.setModelo(modelo);
                    vehiculo.setEstado(estado);
                    vehiculo.setMantenimientos(mantenimientos);

                    return Optional.of(copiar(vehiculo));
                }
            }
            return Optional.empty();
        }
    }

    @Override
    public boolean eliminar(int id) {
        synchronized (LOCK) {
            for (int i = 0; i < VEHICULOS.size(); i++) {
                if (VEHICULOS.get(i).getId() == id) {
                    VEHICULOS.remove(i);
                    return true;
                }
            }
            return false;
        }
    }
}

