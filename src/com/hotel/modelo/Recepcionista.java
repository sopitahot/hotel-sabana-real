package com.hotel.modelo;

import com.hotel.excepciones.ReservaInvalidaException;
import java.util.ArrayList;

public class Recepcionista extends Empleado {
    private final ArrayList<Reserva> reservas = new ArrayList<>();

    public Recepcionista(String nombre, String id, String cargo) {
        super(nombre, id, cargo);
    }

    public void hacerReserva(String fecha, Huesped huesped) throws ReservaInvalidaException {
        reservas.add(new Reserva(fecha, "Alojamiento estándar", huesped, this));
        System.out.println("Reserva hecha para " + fecha + " a nombre de " + huesped.getNombre());
    }

    public void hacerReserva(String fecha, String servicio, Huesped huesped) throws ReservaInvalidaException {
        reservas.add(new Reserva(fecha, servicio, huesped, this));
        System.out.println("Reserva hecha para " + fecha + " con servicio " + servicio
                + " a nombre de " + huesped.getNombre());
    }

    public ArrayList<Reserva> getReservas() {
        return new ArrayList<>(reservas);
    }

    @Override
    public String generarReporte() {
        return "=== RECEPCIONISTA ===\nNombre: " + getNombre() + "\nID: " + getId()
                + "\nCargo: " + getCargo() + "\nReservas gestionadas: " + reservas.size();
    }
}
