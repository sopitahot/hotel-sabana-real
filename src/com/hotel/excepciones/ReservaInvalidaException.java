package com.hotel.excepciones;

public class ReservaInvalidaException extends Exception {
    private static final long serialVersionUID = 1L;

    public ReservaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
