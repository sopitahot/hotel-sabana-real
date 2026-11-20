package com.hotel.modelo;

import com.hotel.excepciones.ReservaInvalidaException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Reserva {
    private final String fecha;
    private final String servicio;
    private final Huesped huesped;
    private final Recepcionista recepcionista;

    public Reserva(String fecha, String servicio, Huesped huesped, Recepcionista recepcionista)
            throws ReservaInvalidaException {
        if (fecha == null || fecha.isBlank()) {
            throw new ReservaInvalidaException("La fecha no puede estar vacía.");
        }
        if (huesped == null || recepcionista == null) {
            throw new ReservaInvalidaException("Debe indicarse " +
                    (huesped == null ? "un huésped válido." : "un recepcionista válido."));
        }
        if (servicio == null || servicio.isBlank()) {
            throw new ReservaInvalidaException("El servicio no puede estar vacío.");
        }
        this.fecha = fecha.strip();
        if (!this.fecha.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
            throw new ReservaInvalidaException("Use el formato AAAA-MM-DD.");
        }
        try {
            LocalDate.parse(this.fecha);
        } catch (DateTimeParseException e) {
            throw new ReservaInvalidaException("La fecha indicada no existe.");
        }
        this.servicio = servicio.strip();
        this.huesped = huesped;
        this.recepcionista = recepcionista;
    }

    public String getInfo() {
        return "Reserva: " + fecha + " | Servicio: " + servicio + " | Recepcionista: "
                + recepcionista.getNombre() + " | Huésped: " + huesped.getNombre();
    }

    public Huesped getHuesped() {
        return huesped;
    }

    public Recepcionista getRecepcionista() {
        return recepcionista;
    }

    public String getFecha() {
        return fecha;
    }
}
