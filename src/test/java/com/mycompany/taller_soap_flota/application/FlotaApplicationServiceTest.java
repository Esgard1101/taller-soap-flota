package com.mycompany.taller_soap_flota.application;

import com.mycompany.taller_soap_flota.exception.VehiculoNoEncontradoException;
import com.mycompany.taller_soap_flota.model.Vehiculo;
import com.mycompany.taller_soap_flota.repository.VehiculoRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlotaApplicationServiceTest {

    private final RepositorioFalso repository = new RepositorioFalso();
    private final FlotaApplicationService application = new FlotaApplicationService();

    @BeforeEach
    void inyectarRepositorio() {
        application.repository = repository;
    }

    @Test
    void listarFlotaVacia() {
        assertTrue(application.listarVehiculos().isEmpty());
    }

    @Test
    void registrarListarYBuscarConservaDatosEIdDelRepositorio() throws Exception {
        Vehiculo registrado = application.registrarVehiculo("ABC-123", "Toyota",
                "Disponible", List.of("Aceite", "Frenos"));

        assertEquals(41, registrado.getId());
        assertDatos(registrado, 41, "ABC-123", "Toyota", "Disponible",
                List.of("Aceite", "Frenos"));
        assertDatos(application.buscarVehiculo(41), 41, "ABC-123", "Toyota",
                "Disponible", List.of("Aceite", "Frenos"));
        assertEquals(1, application.listarVehiculos().size());
        assertDatos(application.listarVehiculos().get(0), 41, "ABC-123", "Toyota",
                "Disponible", List.of("Aceite", "Frenos"));
    }

    @Test
    void actualizarReemplazaDatosEHistorialCompleto() throws Exception {
        Vehiculo registrado = application.registrarVehiculo("ABC-123", "Toyota",
                "Disponible", List.of("Aceite", "Frenos"));

        Vehiculo actualizado = application.actualizarVehiculo(registrado.getId(),
                "DEF-456", "Volvo", "En taller", List.of("Motor", "Llantas"));

        assertDatos(actualizado, registrado.getId(), "DEF-456", "Volvo",
                "En taller", List.of("Motor", "Llantas"));
        assertDatos(application.buscarVehiculo(registrado.getId()), registrado.getId(),
                "DEF-456", "Volvo", "En taller", List.of("Motor", "Llantas"));
    }

    @Test
    void registrarNormalizaHistorialNuloAntesDeInvocarRepositorio() {
        Vehiculo registrado = application.registrarVehiculo("ABC-123", "Toyota",
                "Disponible", null);

        assertNotNull(repository.ultimoHistorial);
        assertTrue(repository.ultimoHistorial.isEmpty());
        assertTrue(registrado.getMantenimientos().isEmpty());
    }

    @Test
    void actualizarConHistorialNuloBorraHistorialAnterior() throws Exception {
        Vehiculo registrado = application.registrarVehiculo("ABC-123", "Toyota",
                "Disponible", List.of("Aceite"));

        Vehiculo actualizado = application.actualizarVehiculo(registrado.getId(),
                "ABC-123", "Toyota", "En ruta", null);

        assertNotNull(repository.ultimoHistorial);
        assertTrue(repository.ultimoHistorial.isEmpty());
        assertTrue(actualizado.getMantenimientos().isEmpty());
        assertTrue(application.buscarVehiculo(registrado.getId()).getMantenimientos().isEmpty());
    }

    @Test
    void admiteHistorialVacioEnRegistroYActualizacion() throws Exception {
        Vehiculo registrado = application.registrarVehiculo("ABC-123", "Toyota",
                "Disponible", List.of());
        assertTrue(registrado.getMantenimientos().isEmpty());

        Vehiculo actualizado = application.actualizarVehiculo(registrado.getId(),
                "ABC-123", "Toyota", "En ruta", List.of());
        assertTrue(actualizado.getMantenimientos().isEmpty());
    }

    @Test
    void historialEnviadoAlRepositorioEsIndependienteDelCliente() throws Exception {
        List<String> historial = new ArrayList<>(List.of("Aceite"));
        Vehiculo registrado = application.registrarVehiculo("ABC-123", "Toyota",
                "Disponible", historial);
        historial.add("Frenos");
        assertEquals(List.of("Aceite"), repository.ultimoHistorial);

        application.actualizarVehiculo(registrado.getId(), "ABC-123", "Toyota",
                "En taller", historial);
        historial.clear();
        assertEquals(List.of("Aceite", "Frenos"), repository.ultimoHistorial);
    }

    @Test
    void buscarIdInexistentePropagaExcepcionConDetalle() {
        assertFault(999, assertThrows(VehiculoNoEncontradoException.class,
                () -> application.buscarVehiculo(999)));
    }

    @Test
    void actualizarIdInexistentePropagaExcepcionSinRegistrarOtroVehiculo() {
        assertFault(999, assertThrows(VehiculoNoEncontradoException.class,
                () -> application.actualizarVehiculo(999, "ABC-123", "Toyota",
                        "En taller", null)));
        assertTrue(application.listarVehiculos().isEmpty());
    }

    @Test
    void eliminarIdInexistentePropagaExcepcion() {
        assertFault(999, assertThrows(VehiculoNoEncontradoException.class,
                () -> application.eliminarVehiculo(999)));
    }

    @Test
    void eliminarDevuelveTrueYSegundaEliminacionLanzaExcepcion() throws Exception {
        Vehiculo registrado = application.registrarVehiculo("ABC-123", "Toyota",
                "Disponible", List.of("Aceite"));

        assertTrue(application.eliminarVehiculo(registrado.getId()));
        assertTrue(application.listarVehiculos().isEmpty());
        assertFault(registrado.getId(), assertThrows(VehiculoNoEncontradoException.class,
                () -> application.eliminarVehiculo(registrado.getId())));
        assertFault(registrado.getId(), assertThrows(VehiculoNoEncontradoException.class,
                () -> application.buscarVehiculo(registrado.getId())));
    }

    @Test
    void falloDelRepositorioNoSeConvierteEnExitoNiEnAusencia() {
        IllegalStateException fallo = new IllegalStateException("Almacenamiento no disponible");
        application.repository = new RepositorioFalso() {
            @Override
            public Optional<Vehiculo> buscarPorId(int id) {
                throw fallo;
            }
        };

        assertSame(fallo, assertThrows(IllegalStateException.class,
                () -> application.buscarVehiculo(41)));
    }

    private static void assertFault(int id, VehiculoNoEncontradoException error) {
        assertEquals(id, error.getFaultInfo().getId());
        assertEquals(new VehiculoNoEncontradoException(id).getMessage(), error.getMessage());
        assertEquals(error.getMessage(), error.getFaultInfo().getMensaje());
    }

    private static void assertDatos(Vehiculo vehiculo, int id, String placa,
            String modelo, String estado, List<String> historial) {
        assertEquals(id, vehiculo.getId());
        assertEquals(placa, vehiculo.getPlaca());
        assertEquals(modelo, vehiculo.getModelo());
        assertEquals(estado, vehiculo.getEstado());
        assertEquals(historial, vehiculo.getMantenimientos());
    }

    /** Falso local: no usa la lista estatica ni la implementacion de A. */
    private static class RepositorioFalso implements VehiculoRepository {
        private final Map<Integer, Vehiculo> vehiculos = new LinkedHashMap<>();
        private int siguienteId = 41;
        private List<String> ultimoHistorial;

        @Override
        public List<Vehiculo> listar() {
            return new ArrayList<>(vehiculos.values());
        }

        @Override
        public Optional<Vehiculo> buscarPorId(int id) {
            return Optional.ofNullable(vehiculos.get(id));
        }

        @Override
        public Vehiculo insertar(String placa, String modelo, String estado,
                List<String> mantenimientos) {
            ultimoHistorial = mantenimientos;
            Vehiculo vehiculo = new Vehiculo(siguienteId++, placa, modelo, estado, mantenimientos);
            vehiculos.put(vehiculo.getId(), vehiculo);
            return vehiculo;
        }

        @Override
        public Optional<Vehiculo> actualizar(int id, String placa, String modelo,
                String estado, List<String> mantenimientos) {
            ultimoHistorial = mantenimientos;
            if (!vehiculos.containsKey(id)) {
                return Optional.empty();
            }
            Vehiculo vehiculo = new Vehiculo(id, placa, modelo, estado, mantenimientos);
            vehiculos.put(id, vehiculo);
            return Optional.of(vehiculo);
        }

        @Override
        public boolean eliminar(int id) {
            return vehiculos.remove(id) != null;
        }
    }
}
