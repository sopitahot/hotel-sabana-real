package com.hotel.modelo;

public class Camarero extends Empleado {
    private final String zona;

    public Camarero(String nombre, String id, String cargo, String zona) {
        super(nombre, id, cargo);
        this.zona = Validador.texto(zona, "La zona");
    }

    @Override
    public String generarReporte() {
        return "=== CAMARERO ===\nNombre: " + getNombre() + "\nID: " + getId()
                + "\nCargo: " + getCargo() + "\nZona: " + zona;
    }
}
