"""Pruebas de integración. Ejecutar después de compilar las clases en out/."""
import os
from pathlib import Path
import subprocess

RAIZ = Path(__file__).resolve().parents[1]
JAVA = os.environ.get("HOTEL_JAVA", "java")
CASOS = []


def caso(nombre, entradas, esperados, prohibidos=(), salir=True):
    CASOS.append((nombre, entradas, esperados, prohibidos, salir))


def regular(doc="00123", nombre="Ana María", edad="22", noches="2"):
    return ["1", "1", nombre, doc, edad, noches]


def vip(doc="45678", edad="120", noches="1", descuento="50"):
    return ["1", "2", "Inés Díaz", doc, edad, noches, descuento]


alojar = ["3", "101", "00123"]
base = regular() + alojar
caso("menú e ingreso de enteros", ["", "abc", "2.5", "2 texto", "999999999999999", "-1", "11", "2"],
     ["Entrada inválida.", "Opción inválida.", "No hay huéspedes registrados."])
caso("tipo de huésped", ["1", "3", "2"], ["Tipo inválido", "No hay huéspedes registrados."])
for nombre in ["", "  ", "Ana2", "!!!"]:
    caso("nombre " + repr(nombre), ["1", "1", nombre, "2"], ["Error:", "No hay huéspedes registrados."])
for doc in ["1234", "1234567890123", "12A45", "+1234", ""]:
    caso("documento " + repr(doc), ["1", "1", "Ana", doc, "2"], ["Error:", "No hay huéspedes registrados."])
for edad in ["-1", "121"]:
    caso("edad " + edad, regular(edad=edad) + ["2"], ["Edad inválida", "No hay huéspedes registrados."])
caso("noches cero", regular(noches="0") + ["2"], ["Las noches deben ser mayores", "No hay huéspedes registrados."])
for descuento in ["-1", "51"]:
    caso("descuento " + descuento, vip(descuento=descuento) + ["2"], ["El descuento debe estar", "No hay huéspedes registrados."])
caso("límites válidos", regular(edad="0") + vip(doc="123456789012") + vip(doc="99999", descuento="0") + ["2"],
     ["Documento: 00123 | Edad: 0", "Documento: 123456789012 | Edad: 120", "Descuento: 50%", "Descuento: 0%"])
caso("documento duplicado", regular() + ["1", "1", "Otra Persona", "00123", "2"],
     ["Ya existe un huésped con ese documento.", "Huésped regular: Ana María"], ["Huésped regular: Otra Persona"])
caso("habitación desconocida", ["5", "000", "3", "000", "7", "000"], ["Habitación inexistente."])
caso("huésped sin registro", ["3", "101", "00123", "9"], ["no encontrado.", "Habitación: 101 | Ocupación: 0/2"])
caso("registrado sin alojar", regular() + ["4", "R001", "00123", "8", "00123", "6", "R001"],
     ["no encontrado.", "No hay reservas para este recepcionista."])
caso("dos habitaciones a la vez", base + ["3", "102", "00123", "9"],
     ["ya está alojado", "Habitación: 101 | Ocupación: 1/2", "Habitación: 102 | Ocupación: 0/3"])
caso("capacidad y cupo liberado", ["10", "301", "1"] + regular() + vip()
     + ["3", "301", "00123", "3", "301", "45678", "9", "7", "301", "00123", "3", "301", "45678", "5", "301"],
     ["sin cupo disponible.", "Habitación: 301 | Ocupación: 1/1", "Check-out realizado.", "Huésped VIP: Inés Díaz"])
caso("crear habitación inválida", ["10", "101", "5", "10", "301", "0", "10", "", "1", "9"],
     ["Ya existe esa habitación.", "Capacidad debe ser mayor a 0.", "no puede estar vacío.", "Habitación: 101 | Ocupación: 0/2"],
     ["Habitación: 301 |"])
caso("búsqueda después de primera habitación", regular() + ["3", "201", "00123", "8", "00123"], ["Huésped regular: Ana María"])
caso("recepcionista desconocido o camarero", ["4", "X", "6", "C001"], ["id X.", "id C001."])
caso("ambas sobrecargas y reporte", base + ["4", "r001", "00123", "2028-02-29", "n",
     "4", "R001", "00123", "2026-11-20", "quizá", "S", "Desayuno", "6", "R001", "9"],
     ["Responda s o n.", "Servicio: Alojamiento estándar", "Servicio: Desayuno", "Recepcionista: Camila Prieto | Huésped: Ana María",
      "Reservas gestionadas: 2", "Total de empleados: 3", "=== CAMARERO ==="])
for fecha in ["", "2026-02-29", "2026-04-31", "ayer", "20/11/2026"]:
    caso("fecha " + repr(fecha), base + ["4", "R001", "00123", fecha, "n", "6", "R001"],
         ["Error:", "No hay reservas para este recepcionista."])
caso("servicio vacío", base + ["4", "R001", "00123", "2026-11-20", "s", " ", "6", "R001"],
     ["El servicio no puede estar vacío.", "No hay reservas para este recepcionista."])
caso("historial y registro tras check-out", base + ["4", "R001", "00123", "2026-11-20", "n",
     "7", "101", "00123", "7", "101", "00123", "5", "101", "8", "00123", "2", "6", "R001", "9"],
     ["Check-out realizado.", "no encontrado.", "No hay huéspedes en esta habitación.", "Huésped regular: Ana María",
      "Servicio: Alojamiento estándar", "Habitación: 101 | Ocupación: 0/2"])
caso("entradas corregidas durante registro", ["1", "x", "1", "Ana", "00123", "dos", "22", "2.2", "1", "2"],
     ["Entrada inválida.", "Edad: 22 | Noches: 1"])
for entradas in [[], ["1", "1", "Ana"], ["10", "301"], base + ["4", "R001", "00123", "2026-11-20"]]:
    caso("fin de entrada " + str(len(entradas)), entradas, ["Entrada finalizada."], salir=False)


def ejecutar():
    for nombre, entradas, esperados, prohibidos, salir in CASOS:
        lineas = entradas + (["0"] if salir else [])
        datos = "".join(linea + "\n" for linea in lineas)
        p = subprocess.run(
            [JAVA, "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8", "-cp", "out", "com.hotel.app.App"],
            cwd=RAIZ, input=datos, encoding="utf-8", capture_output=True, timeout=12, check=True)
        for fragmento in list(esperados) + (["Hasta pronto."] if salir else []):
            if fragmento not in p.stdout:
                raise AssertionError(f"{nombre}: falta {fragmento!r}\n{p.stdout}")
        if p.stderr or any(fragmento in p.stdout for fragmento in prohibidos):
            raise AssertionError(f"{nombre}: salida inesperada\n{p.stderr}\n{p.stdout}")
    print(f"{len(CASOS)} escenarios de consola aprobados.")


if __name__ == "__main__":
    ejecutar()
