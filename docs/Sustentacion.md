# Sustentación · Hotel Sabana Real

La parte individual vale el 15 % del curso. Practica con el programa abierto: haz una predicción, ejecuta la opción y señala el método responsable. Si puedes explicar esta secuencia sin leer, ya tienes cómo empezar la sustentación.

## Un recorrido de cinco minutos

| Paso | Qué teclear, en orden | Qué mostrar o explicar |
|---|---|---|
| 1 | Opción 1 → regular (1) → Ana María → 00123 → 22 → 2. Opción 2. | La persona ya existe como objeto. Su documento conserva los ceros. |
| 2 | Opción 4 → R001 → 00123. | Todavía no puede reservar. La búsqueda recorre las **habitaciones**, que siguen vacías. Se muestra el error y vuelve el menú. |
| 3 | Opción 10 → 301 → 1. Opción 3 → 301 → 00123. | El hotel creó la habitación. La habitación recibió el mismo objeto que ya estaba registrado. |
| 4 | Opción 1 → VIP (2) → Luis Díaz → 45678 → 30 → 3 → 20. Opción 3 → 301 → 45678. | El segundo alojamiento falla por falta de cupo. En la opción 9 la habitación sigue en 1/1. Puedes alojar a Luis en 102. |
| 5 | Opción 4 → R001 → 00123 → 2026-11-20 → n. Repite con s y Desayuno. Opción 6 → R001. | Mismo nombre de método, dos firmas. Ambas reservas guardan el huésped y la recepcionista reales. |
| 6 | Opción 7 → 301 → 00123. Prueba las opciones 5 (301), 2, 8 (00123) y 6 (R001). | La habitación quedó vacía, Ana sigue registrada, la búsqueda de alojados falla y sus dos reservas siguen en el historial. |

## Sigue la opción 4 en el código

1. En `App.reservar`, `leerRecepcionista` lista el personal de recepción y busca el ID. `C001` pertenece al camarero y falla en esta operación.
2. `hotel.buscarHuesped(documento)` pide a cada habitación que busque el documento. Si la primera habitación falla, el hotel sigue con la siguiente. Solo después de agotar la lista comunica el fallo a la aplicación.
3. La respuesta `s` o `n` determina qué llamada escribe el programa. Java sabe cuál de las dos firmas de `hacerReserva` corresponde a cada llamada por los parámetros que recibe.
4. Dentro de `Recepcionista`, la expresión `reservas.add(new Reserva(...))` evalúa primero el constructor. Si la fecha o el servicio fallan, nunca se agrega el objeto. `this` pasa la referencia al recepcionista que está atendiendo.
5. Si ocurre una excepción de negocio, el `catch` de `App.main` imprime el mensaje. Como el ciclo del menú rodea ese bloque, se puede elegir otra opción.

## Preguntas que debes poder responder mirando el código

| Pregunta | Respuesta que puedes dar en tus palabras |
|---|---|
| ¿Dónde hay composición? | En `Hotel`. Su constructor crea tres habitaciones con `new Habitacion(...)`; `agregarHabitacion` también crea la nueva habitación dentro del hotel. En el modelo cada habitación pertenece a ese hotel. |
| ¿Dónde hay agregación? | En `Habitacion.alojarHuesped`. Recibe un huésped que ya creó la opción 1 y guarda su referencia. Al hacer check-out la persona sigue existiendo en el registro. |
| ¿Dónde hay asociación? | `Reserva` tiene dos atributos de tipo objeto, `huesped` y `recepcionista`. Vincula personas que ya existían antes de crear la reserva. |
| ¿Para qué sirve una clase abstracta? | `Huesped` reúne nombre, documento, edad y validaciones. Exige que cada subtipo implemente `obtenerInfo()`. Para crear una persona concreta usamos `new HuespedRegular(...)` o `new HuespedVIP(...)`. |
| ¿Qué hace `super(...)`? | En el constructor VIP ejecuta el constructor de `Huesped` para validar e inicializar los datos comunes. Después el constructor VIP valida noches y descuento. |
| ¿Dónde se ve el encapsulamiento? | El documento es `private`; las subclases lo consultan con `getDocumento()`. Los getters de listas devuelven una copia de la lista: vaciar esa copia no vacía la habitación. Los objetos contenidos siguen siendo los mismos. |
| ¿Qué significa `final`? | En `final class HuespedVIP`, impide crear una subclase de VIP. En un atributo como `final ArrayList<Reserva>`, impide reasignar la referencia. La lista sí admite nuevas reservas. |
| ¿Sobrecarga y sobrescritura? | Sobrecarga: en `Recepcionista` hay dos firmas de `hacerReserva`, con y sin servicio. Sobrescritura: VIP y regular implementan la misma firma `obtenerInfo()` de la clase padre. |
| ¿Dónde ves polimorfismo dinámico? | En `Habitacion.listarHuespedes`, la variable `h` tiene tipo declarado `Huesped`. Cuando se llama `h.obtenerInfo()`, se ejecuta la versión del objeto real. El VIP muestra su descuento. Lo mismo ocurre en `Hotel.generarReporteGeneral` con los empleados. |
| ¿Por qué `ArrayList<Huesped>`? | Permite alojar ambos subtipos y cambiar el tamaño de la lista. La capacidad del cuarto es una regla del hotel: `size() >= capacidad` impide superar el cupo. |
| ¿Por qué el documento es `String`? | Identifica a la persona y puede empezar con cero. Se compara el contenido con `.equals()`. Con objetos, `==` compara referencias. |
| ¿Por qué se usa `instanceof`? | La lista de empleados contiene camareros y recepcionistas. La búsqueda verifica el tipo antes del cast, porque solo un recepcionista gestiona reservas. |
| ¿Qué diferencia hay entre `throw` y `throws`? | `throw new HabitacionLlenaException(...)` lanza el error en ese punto. `throws HabitacionLlenaException` avisa en la firma que la operación puede propagarlo. |
| ¿Qué pasa si escribo letras donde va la edad? | `leerEntero` consume la línea con `sc.nextLine()` y la analiza aparte. Si el dato es inválido, captura `InputMismatchException` e insiste en el mismo campo. Si escribo 121, sí es un entero: el constructor rechaza el rango y vuelvo al menú sin registrar a nadie. |
| ¿Por qué el número 2 no basta para reservar? | La opción 2 solo muestra los registrados. Para reservar, `Hotel.buscarHuesped` debe encontrar a la persona alojada. Esta decisión está en el README. |
| ¿Puede una habitación destruir un objeto al hacer check-out? | `remove` quita una referencia de esa lista. El registro y las reservas mantienen sus propias referencias al huésped. |
| ¿Qué pasa al cerrar el programa? | Todos los datos son de la sesión. La siguiente ejecución empieza con las habitaciones y empleados iniciales. |

## Las cuatro excepciones del dominio

| Excepción | Cómo provocarla desde el menú | Resultado esperado |
|---|---|---|
| `HabitacionLlenaException` | Aloja a una segunda persona en una habitación de capacidad 1. | El cupo permanece en 1/1. |
| `HuespedNoEncontradoException` | Busca con la opción 8 un documento válido que no esté alojado. | Aparece el documento en el mensaje y vuelve el menú. |
| `EmpleadoNoEncontradoException` | Consulta reservas en la opción 6 con C001. | No encuentra un **recepcionista** elegible con ese ID. |
| `ReservaInvalidaException` | Intenta reservar el 2026-02-29 para un huésped alojado. | La fecha no existe y el contador de reservas no aumenta. |

**Ensayo sin mirar respuestas.** Registra un VIP con documento 00125, edad 0, una noche y descuento 50. Predice qué pasa si cambias el descuento a 51, si escribes `1.5` en noches o si intentas alojarlo dos veces. En cada caso, señala la clase que detecta el problema, la excepción y el estado que conserva el hotel. Luego cambia de rol con un compañero: uno usa el menú y el otro explica el resultado.
