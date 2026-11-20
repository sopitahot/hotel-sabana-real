package com.hotel.modelo;

public final class Validador {
    private Validador() {}

    public static String texto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        return valor.strip();
    }

    public static void validarNombre(String nombre) {
        texto(nombre, "El nombre");
        if (nombre.codePoints().anyMatch(Character::isDigit)) {
            throw new IllegalArgumentException("El nombre no puede contener números: " + nombre);
        }
        if (!nombre.strip().matches("[\\p{L}][\\p{L}\\p{M} '\u2019-]*")) {
            throw new IllegalArgumentException("Escriba un nombre con letras.");
        }
    }

    public static void validarDocumento(String documento) {
        if (documento == null || documento.length() < 5 || documento.length() > 12) {
            throw new IllegalArgumentException("El documento debe tener entre 5 y 12 dígitos.");
        }
        if (!documento.matches("[0-9]+")) {
            throw new IllegalArgumentException("El documento solo puede contener números.");
        }
    }

    public static int validarNoches(int noches) {
        if (noches <= 0) {
            throw new IllegalArgumentException("Las noches deben ser mayores a 0.");
        }
        return noches;
    }
}
