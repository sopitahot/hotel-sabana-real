package com.hotel.modelo;

import com.hotel.excepciones.HabitacionLlenaException;
import com.hotel.excepciones.HuespedNoEncontradoException;
import java.util.ArrayList;

public class Habitacion {
    private final String numero;
    private final int capacidad;
    private final ArrayList<Huesped> huespedes = new ArrayList<>();

    public Habitacion(String numero, int capacidad) {
        this.numero = Validador.texto(numero, "El número de habitación");
        if (capacidad <= 0) {
            throw new IllegalArgumentException("Capacidad debe ser mayor a 0.");
        }
        this.capacidad = capacidad;
    }

    public void alojarHuesped(Huesped h) throws HabitacionLlenaException {
        if (h == null) {
            throw new IllegalArgumentException("Debe indicarse un huésped válido.");
        }
        if (huespedes.size() >= capacidad) {
            throw new HabitacionLlenaException("Habitación " + numero + " sin cupo disponible.");
        }
        for (Huesped actual : huespedes) {
            if (actual.getDocumento().equals(h.getDocumento())) {
                throw new IllegalArgumentException("El huésped ya está alojado en esta habitación.");
            }
        }
        huespedes.add(h);
        System.out.println("Huésped " + h.getNombre() + " alojado en habitación " + numero);
    }

    public void liberarHuesped(String documento) throws HuespedNoEncontradoException {
        Huesped h = buscarHuesped(documento);
        huespedes.remove(h);
        System.out.println("Check-out realizado.");
    }

    public Huesped buscarHuesped(String documento) throws HuespedNoEncontradoException {
        for (Huesped h : huespedes) {
            if (h.getDocumento().equals(documento)) {
                return h;
            }
        }
        throw new HuespedNoEncontradoException(documento);
    }

    public void listarHuespedes() {
        if (huespedes.isEmpty()) {
            System.out.println("No hay huéspedes en esta habitación.");
        }
        for (Huesped h : huespedes) {
            System.out.println(h.obtenerInfo());
        }
    }

    public String getNumero() {
        return numero;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public ArrayList<Huesped> getHuespedes() {
        return new ArrayList<>(huespedes);
    }
}
