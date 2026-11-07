
package com.mycompany.taller_soap_flota.repository;

import com.mycompany.taller_soap_flota.model.Vehiculo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VehiculoRepositoryMemoriaTest {

    private final VehiculoRepository repo =
            new VehiculoRepositoryMemoria();

    @Test
    void registrarYBuscarVehiculo() {
        Vehiculo v = repo.insertar(
                "ABC-123", "Toyota", "Disponible",
                List.of("Cambio de aceite"));

        assertTrue(v.getId() > 0);
        assertEquals("ABC-123",
                repo.buscarPorId(v.getId()).orElseThrow().getPlaca());
    }

    @Test
    void actualizarVehiculo() {
        Vehiculo v = repo.insertar(
                "DEF-456", "Volvo", "En ruta", List.of());

        Vehiculo actualizado = repo.actualizar(
                v.getId(), "DEF-456", "Volvo", "En taller",
                List.of("Cambio de frenos")).orElseThrow();

        assertEquals("En taller", actualizado.getEstado());
        assertEquals(List.of("Cambio de frenos"),
                actualizado.getMantenimientos());
    }

    @Test
    void eliminarVehiculo() {
        Vehiculo v = repo.insertar(
                "GHI-789", "Nissan", "Disponible", List.of());

        assertTrue(repo.eliminar(v.getId()));
        assertTrue(repo.buscarPorId(v.getId()).isEmpty());
        assertFalse(repo.eliminar(v.getId()));
    }

    @Test
    void idsNoSeReutilizan() {
        Vehiculo primero = repo.insertar(
                "AAA-111", "Modelo A", "Disponible", List.of());

        repo.eliminar(primero.getId());

        Vehiculo segundo = repo.insertar(
                "BBB-222", "Modelo B", "Disponible", List.of());

        assertTrue(segundo.getId() > primero.getId());
    }

    @Test
    void devuelveCopiasDefensivas() {
        List<String> historial = new ArrayList<>();
        historial.add("Aceite");

        Vehiculo v = repo.insertar(
                "CCC-333", "Modelo C", "Disponible", historial);

        historial.add("Frenos");

        Vehiculo copia = repo.buscarPorId(v.getId()).orElseThrow();
        copia.setEstado("Modificado");
        copia.getMantenimientos().add("Alterado");

        Vehiculo guardado = repo.buscarPorId(v.getId()).orElseThrow();

        assertEquals("Disponible", guardado.getEstado());
        assertEquals(List.of("Aceite"), guardado.getMantenimientos());
    }

    @Test
    void idsUnicosEnAltasSimultaneas() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(8);

        try {
            List<Callable<Integer>> tareas = new ArrayList<>();

            for (int i = 0; i < 50; i++) {
                tareas.add(() -> repo.insertar(
                        "TEST", "Camion", "Disponible",
                        List.of()).getId());
            }

            List<Future<Integer>> resultados =
                    executor.invokeAll(tareas);

            Set<Integer> ids = resultados.stream()
                    .map(f -> {
                        try {
                            return f.get();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());

            assertEquals(50, ids.size());
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void idsInexistentes() {
        assertTrue(repo.buscarPorId(-1).isEmpty());
        assertTrue(repo.actualizar(
                -1, "X", "Y", "Disponible", List.of()).isEmpty());
        assertFalse(repo.eliminar(-1));
    }

    @Test
    void listaDevuelveCopias() {
        Vehiculo v = repo.insertar(
                "DDD-444", "Modelo D", "Disponible", List.of());

        List<Vehiculo> lista = repo.listar();
        Vehiculo encontrado = lista.stream()
                .filter(x -> x.getId() == v.getId())
                .findFirst().orElseThrow();

        encontrado.setEstado("Alterado");

        assertEquals("Disponible",
                repo.buscarPorId(v.getId()).orElseThrow().getEstado());
    }
}
