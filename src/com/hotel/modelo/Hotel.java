package com.hotel.modelo;

import com.hotel.excepciones.EmpleadoNoEncontradoException;
import com.hotel.excepciones.HabitacionLlenaException;
import com.hotel.excepciones.HuespedNoEncontradoException;
import java.util.ArrayList;

public class Hotel {
    private final String nombre;
    private final ArrayList<Habitacion> habitaciones = new ArrayList<>();
    private final ArrayList<Empleado> empleados = new ArrayList<>();

    public Hotel(String nombre) {
        this.nombre = Validador.texto(nombre, "El nombre del hotel");
        habitaciones.add(new Habitacion("101", 2));
        habitaciones.add(new Habitacion("102", 3));
        habitaciones.add(new Habitacion("201", 4));
    }

    public void agregarHabitacion(String numero, int capacidad) {
        numero = Validador.texto(numero, "El número de habitación");
        if (buscarHabitacion(numero) != null) {
            throw new IllegalArgumentException("Ya existe esa habitación.");
        }
        habitaciones.add(new Habitacion(numero, capacidad));
        System.out.println("Habitación '" + numero + "' creada con capacidad " + capacidad);
    }

    public Habitacion buscarHabitacion(String numero) {
        for (Habitacion h : habitaciones) {
            if (h.getNumero().equalsIgnoreCase(numero)) {
                return h;
            }
        }
        return null;
    }

    public void alojarHuesped(String numero, Huesped huesped) throws HabitacionLlenaException {
        Habitacion destino = buscarHabitacion(numero);
        if (destino == null) {
            throw new IllegalArgumentException("Habitación inexistente.");
        }
        if (huesped == null) {
            throw new IllegalArgumentException("Debe indicarse un huésped válido.");
        }
        try {
            buscarHuesped(huesped.getDocumento());
            throw new IllegalArgumentException("El huésped ya está alojado en el hotel.");
        } catch (HuespedNoEncontradoException e) {
            destino.alojarHuesped(huesped);
        }
    }

    public Huesped buscarHuesped(String documento) throws HuespedNoEncontradoException {
        for (Habitacion h : habitaciones) {
            try {
                return h.buscarHuesped(documento);
            } catch (HuespedNoEncontradoException e) {
                // El documento aún puede estar en otra habitación.
            }
        }
        throw new HuespedNoEncontradoException(documento);
    }

    public void agregarEmpleado(Empleado e) {
        if (e == null) {
            throw new IllegalArgumentException("Debe indicarse un empleado válido.");
        }
        for (Empleado actual : empleados) {
            if (actual.getId().equalsIgnoreCase(e.getId())) {
                throw new IllegalArgumentException("Ya existe un empleado con ese ID.");
            }
        }
        empleados.add(e);
        System.out.println(e.getNombre() + " registrado en el hotel.");
    }

    public Recepcionista buscarRecepcionista(String id) throws EmpleadoNoEncontradoException {
        for (Empleado e : empleados) {
            if (e instanceof Recepcionista && e.getId().equalsIgnoreCase(id)) {
                return (Recepcionista) e;
            }
        }
        throw new EmpleadoNoEncontradoException(id);
    }

    public void listarRecepcionistas() {
        for (Empleado e : empleados) {
            if (e instanceof Recepcionista) {
                System.out.println(e.getId() + " - " + e.getNombre() + " (" + e.getCargo() + ")");
            }
        }
    }

    public void generarReporteGeneral() {
        System.out.println("Reporte de " + nombre);
        for (Habitacion h : habitaciones) {
            System.out.println("Habitación: " + h.getNumero() + " | Ocupación: "
                    + h.getHuespedes().size() + "/" + h.getCapacidad());
        }
        System.out.println("Total de empleados: " + empleados.size());
        for (Empleado e : empleados) {
            System.out.println(e.generarReporte());
        }
    }

    public ArrayList<Habitacion> getHabitaciones() {
        return new ArrayList<>(habitaciones);
    }

    public ArrayList<Empleado> getEmpleados() {
        return new ArrayList<>(empleados);
    }
}
