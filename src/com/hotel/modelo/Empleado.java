package com.hotel.modelo;

public abstract class Empleado {
    private final String nombre;
    private final String id;
    private final String cargo;

    public Empleado(String nombre, String id, String cargo) {
        Validador.validarNombre(nombre);
        this.nombre = nombre.strip();
        this.id = Validador.texto(id, "El id");
        this.cargo = Validador.texto(cargo, "El cargo");
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public String getCargo() {
        return cargo;
    }

    public abstract String generarReporte();
}
