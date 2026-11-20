package com.hotel.app;

import com.hotel.excepciones.*;
import com.hotel.modelo.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Hotel hotel = new Hotel("Hotel Sabana Real");
        hotel.agregarEmpleado(new Recepcionista("Camila Prieto", "R001", "Recepción día"));
        hotel.agregarEmpleado(new Recepcionista("Andrés Mora", "R002", "Recepción noche"));
        hotel.agregarEmpleado(new Camarero("Luz Marina Díaz", "C001", "Housekeeping", "Piso 1"));
        ArrayList<Huesped> registrados = new ArrayList<>();
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nHotel Sabana Real\n1 Registrar huésped\n2 Listar registrados"
                        + "\n3 Alojar huésped\n4 Hacer reserva\n5 Ver huéspedes de habitación"
                        + "\n6 Ver reservas de recepcionista\n7 Check-out\n8 Buscar huésped alojado"
                        + "\n9 Reporte general\n10 Crear habitación\n0 Salir");
                int opcion = leerEntero(sc, "Opción:");
                try {
                    switch (opcion) {
                        case 0:
                            System.out.println("Hasta pronto.");
                            return;
                        case 1:
                            registrar(sc, registrados);
                            break;
                        case 2:
                            if (registrados.isEmpty()) {
                                System.out.println("No hay huéspedes registrados.");
                            }
                            for (Huesped h : registrados) {
                                System.out.println(h.obtenerInfo());
                            }
                            break;
                        case 3:
                            Habitacion destino = leerHabitacion(sc, hotel);
                            String documento = leerDocumento(sc);
                            Huesped h = buscarRegistrado(registrados, documento);
                            if (h == null) {
                                throw new HuespedNoEncontradoException(documento);
                            }
                            hotel.alojarHuesped(destino.getNumero(), h);
                            break;
                        case 4:
                            reservar(sc, hotel);
                            break;
                        case 5:
                            leerHabitacion(sc, hotel).listarHuespedes();
                            break;
                        case 6:
                            Recepcionista r = leerRecepcionista(sc, hotel);
                            ArrayList<Reserva> reservas = r.getReservas();
                            if (reservas.isEmpty()) {
                                System.out.println("No hay reservas para este recepcionista.");
                            }
                            for (Reserva reserva : reservas) {
                                System.out.println(reserva.getInfo());
                            }
                            break;
                        case 7:
                            leerHabitacion(sc, hotel).liberarHuesped(leerDocumento(sc));
                            break;
                        case 8:
                            System.out.println(hotel.buscarHuesped(leerDocumento(sc)).obtenerInfo());
                            break;
                        case 9:
                            hotel.generarReporteGeneral();
                            break;
                        case 10:
                            String numero = leerLinea(sc, "Número de habitación:");
                            hotel.agregarHabitacion(numero, leerEntero(sc, "Capacidad:"));
                            break;
                        default:
                            System.out.println("Opción inválida. Elija entre 0 y 10.");
                    }
                } catch (IllegalArgumentException | HabitacionLlenaException | HuespedNoEncontradoException
                        | EmpleadoNoEncontradoException | ReservaInvalidaException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Entrada finalizada. Cerrando el hotel.");
        }
    }

    private static String leerLinea(Scanner sc, String mensaje) {
        System.out.println(mensaje);
        return sc.nextLine().strip();
    }

    public static int leerEntero(Scanner sc, String mensaje) {
        while (true) {
            System.out.println(mensaje);
            if (!sc.hasNextLine()) {
                throw new NoSuchElementException();
            }
            // Examina una línea completa para rechazar también "2 texto" y líneas vacías.
            try (Scanner linea = new Scanner(sc.nextLine())) {
                if (!linea.hasNext("[+-]?[0-9]+")) {
                    throw new InputMismatchException();
                }
                int valor = linea.nextInt();
                if (linea.hasNext()) {
                    throw new InputMismatchException();
                }
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Escriba un solo número entero.");
            }
        }
    }

    private static String leerDocumento(Scanner sc) {
        String documento = leerLinea(sc, "Documento:");
        Validador.validarDocumento(documento);
        return documento;
    }

    private static Huesped buscarRegistrado(ArrayList<Huesped> registrados, String documento) {
        for (Huesped h : registrados) {
            if (h.getDocumento().equals(documento)) {
                return h;
            }
        }
        return null;
    }

    private static void registrar(Scanner sc, ArrayList<Huesped> registrados) {
        int tipo = leerEntero(sc, "Tipo (1 regular, 2 VIP):");
        if (tipo != 1 && tipo != 2) {
            System.out.println("Tipo inválido");
            return;
        }
        String nombre = leerLinea(sc, "Nombre:");
        Validador.validarNombre(nombre);
        String documento = leerDocumento(sc);
        if (buscarRegistrado(registrados, documento) != null) {
            throw new IllegalArgumentException("Ya existe un huésped con ese documento.");
        }
        int edad = leerEntero(sc, "Edad:");
        int noches = leerEntero(sc, "Noches:");
        Huesped nuevo;
        if (tipo == 1) {
            nuevo = new HuespedRegular(nombre, documento, edad, noches);
        } else {
            nuevo = new HuespedVIP(nombre, documento, edad, noches, leerEntero(sc, "Descuento (0 a 50):"));
        }
        registrados.add(nuevo);
        System.out.println("Huésped registrado.");
    }

    private static Habitacion leerHabitacion(Scanner sc, Hotel hotel) {
        Habitacion h = hotel.buscarHabitacion(leerLinea(sc, "Número de habitación:"));
        if (h == null) {
            throw new IllegalArgumentException("Habitación inexistente.");
        }
        return h;
    }

    private static Recepcionista leerRecepcionista(Scanner sc, Hotel hotel) throws EmpleadoNoEncontradoException {
        hotel.listarRecepcionistas();
        return hotel.buscarRecepcionista(leerLinea(sc, "ID de recepcionista:"));
    }

    private static void reservar(Scanner sc, Hotel hotel) throws EmpleadoNoEncontradoException,
            HuespedNoEncontradoException, ReservaInvalidaException {
        Recepcionista r = leerRecepcionista(sc, hotel);
        Huesped h = hotel.buscarHuesped(leerDocumento(sc));
        String fecha = leerLinea(sc, "Fecha (AAAA-MM-DD):");
        while (true) {
            String respuesta = leerLinea(sc, "¿Agregar servicio? (s/n)");
            if (respuesta.equalsIgnoreCase("s")) {
                r.hacerReserva(fecha, leerLinea(sc, "Servicio:"), h);
                return;
            }
            if (respuesta.equalsIgnoreCase("n")) {
                r.hacerReserva(fecha, h);
                return;
            }
            System.out.println("Responda s o n.");
        }
    }
}
