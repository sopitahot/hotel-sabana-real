package com.hotel.modelo;

public class HuespedRegular extends Huesped {
    private final int noches;

    public HuespedRegular(String nombre, String documento, int edad, int noches) {
        super(nombre, documento, edad);
        this.noches = Validador.validarNoches(noches);
    }

    @Override
    public String obtenerInfo() {
        return "Huésped regular: " + getNombre() + " | Documento: " + getDocumento()
                + " | Edad: " + getEdad() + " | Noches: " + noches;
    }
}
