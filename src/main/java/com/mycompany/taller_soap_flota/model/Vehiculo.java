package com.mycompany.taller_soap_flota.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Datos del vehiculo que se intercambian mediante JAXB. */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vehiculo",
        namespace = "http://service.taller_soap_flota.mycompany.com/",
        propOrder = {"id", "placa", "modelo", "estado", "mantenimientos"})
@XmlRootElement(name = "vehiculo",
        namespace = "http://service.taller_soap_flota.mycompany.com/")
public class Vehiculo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String placa;
    private String modelo;
    private String estado;
    private List<String> mantenimientos = new ArrayList<>();

    public Vehiculo() {
        // Constructor requerido por JAXB.
    }

    public Vehiculo(int id, String placa, String modelo, String estado,
            List<String> mantenimientos) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.estado = estado;
        setMantenimientos(mantenimientos);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<String> getMantenimientos() {
        return mantenimientos;
    }

    public void setMantenimientos(List<String> mantenimientos) {
        this.mantenimientos = mantenimientos == null
                ? new ArrayList<>() : new ArrayList<>(mantenimientos);
    }
}
