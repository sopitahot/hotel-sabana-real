package com.hotel.modelo;

public final class HuespedVIP extends Huesped {
    private final int noches;
    private final int descuento;

    public HuespedVIP(String nombre, String documento, int edad, int noches, int descuento) {
        super(nombre, documento, edad);
        this.noches = Validador.validarNoches(noches);
        if (descuento < 0 || descuento > 50) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 50.");
        }
        this.descuento = descuento;
    }

    public int getDescuento() {
        return descuento;
    }

    @Override
    public String obtenerInfo() {
        return "Huésped VIP: " + getNombre() + " | Documento: " + getDocumento()
                + " | Edad: " + getEdad() + " | Noches: " + noches + " | Descuento: " + descuento + "%";
    }
}
