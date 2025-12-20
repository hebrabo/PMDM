package ejercicio9

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

/*
Ejercicio 9: Pipeline de Datos
Idea: Leer datos → procesarlos → guardarlos, cada paso en una corrutina distinta.

Qué practicar:
- canales (Channel)
- comunicación entre corrutinas
- backpressure básico
*/

data class NotaPedido(val id: Int, val estado: String)

fun main() {
    // runBlocking: Crea un mundo de corrutinas que bloquea el hilo principal hasta terminar todo.
    runBlocking {
        println("--- RESTAURANTE COROUTINES ABIERTO ---")

        // 1. DEFINICIÓN DE CANALES
        // -------------------------------------------------
        // [DEFINICIÓN] Channel(capacity = 2):
        // Es un buffer acotado. Solo caben 2 elementos en memoria.
        // Actúa como semáforo: Si está lleno, el que envía se detiene (suspend).
        // Si está vacío, el que recibe se detiene.
        val ventanillaCocina = Channel<NotaPedido>(capacity = 2)
        val mesaCliente = Channel<NotaPedido>(capacity = 2)


        // 2. PRIMERA CORRUTINA: EL PRODUCTOR (MESERO)
        // -------------------------------------------
        launch {
            for (i in 1..5) {
                println("1. [MESERO] Tomé nota del pedido #$i (¡Rápido!)")
                delay(100) // Simula que el mesero es muy veloz tomando notas.

                println("   >> [MESERO] Voy a dejar la nota #$i en la ventanilla...")

                val horaLlegada = System.currentTimeMillis()

                // [BACKPRESSURE / PUNTO DE SUSPENSIÓN]
                // La función .send() suspende la corrutina si el canal está lleno.
                // Estado del sistema:
                // - Si hay 0 o 1 notas: La mete y sigue.
                // - Si hay 2 notas: El Mesero se "congela" en esta línea exacta.
                //   No gasta CPU, solo espera a que se libere un hueco.
                ventanillaCocina.send(NotaPedido(i, "Pendiente"))

                // Si llegamos aquí, es que logramos poner la nota.
                val horaSalida = System.currentTimeMillis()
                val tiempoEspera = horaSalida - horaLlegada

                // Diagnóstico para ver si hubo bloqueo
                if (tiempoEspera > 50) {
                    println("   !! [MESERO MOLESTO] ¡Ventanilla llena! Tuve que esperar ${tiempoEspera}ms parado con la nota #$i en la mano.")
                } else {
                    println("   >> [MESERO] Dejé la nota #$i y me fui rápido.")
                }
            }

            // [ACCIÓN] Cerrar el canal es VITAL.
            // Envía una señal especial de "Fin de transmisión".
            // Sin esto, el Cocinero se quedaría esperando eternamente una nota que nunca llegará.
            ventanillaCocina.close()
            println("--- Mesero terminó su turno ---")
        }


        // 3. SEGUNDA CORRUTINA: EL CONSUMIDOR/TRANSFORMADOR (COCINERO)
        // ------------------------------------------------------------
        launch {
            // [ACCIÓN] Bucle 'for' sobre un canal:
            // Este bucle llama automáticamente a .receive().
            // Se queda esperando si la ventanilla está vacía.
            // Se rompe el bucle automáticamente cuando el canal se cierra (close).
            for (nota in ventanillaCocina) {

                // Al entrar aquí, significa que hemos SACADO una nota de 'ventanillaCocina'.
                // ¡Esto libera un hueco (baja de 2 a 1)!
                // Si el mesero estaba bloqueado arriba, AHORA MISMO se despierta.
                println("      2. [COCINERO] Agarré la nota #${nota.id} de la ventanilla (Hueco libre). Cocinando...")

                // [CUELLO DE BOTELLA]
                // Aquí simulamos lentitud. Esto es lo que causa que la ventanilla se llene.
                delay(500)

                // Transformamos el dato y lo pasamos al siguiente canal
                mesaCliente.send(nota.copy(estado = "Cocinada"))
                println("      2. [COCINERO] Pedido #${nota.id} listo. ¡Oído cocina!")
            }
            // Cerramos el siguiente tramo del pipeline
            mesaCliente.close()
            println("--- Cocina cerrada ---")
        }


        // 4. TERCERA CORRUTINA: CONSUMIDOR FINAL (CLIENTE)
        // ------------------------------------------------
        launch {
            for (plato in mesaCliente) {
                println("         3. [CLIENTE] Comiendo plato #${plato.id}")
                delay(100)
            }
            println("--- Todos comieron ---")
        }
    }
}