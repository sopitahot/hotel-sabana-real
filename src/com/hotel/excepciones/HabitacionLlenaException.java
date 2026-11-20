package com.hotel.excepciones;

public class HabitacionLlenaException extends Exception {
    private static final long serialVersionUID = 1L;

    public HabitacionLlenaException(String mensaje) {
        super(mensaje);
    }
}
