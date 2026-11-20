package com.hotel.excepciones;

public class HuespedNoEncontradoException extends Exception {
    private static final long serialVersionUID = 1L;

    public HuespedNoEncontradoException(String documento) {
        super("Huésped con documento " + documento + " no encontrado.");
    }
}
