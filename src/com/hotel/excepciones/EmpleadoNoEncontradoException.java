package com.hotel.excepciones;

public class EmpleadoNoEncontradoException extends Exception {
    private static final long serialVersionUID = 1L;

    public EmpleadoNoEncontradoException(String id) {
        super("No existe empleado registrado con id " + id + ".");
    }
}
