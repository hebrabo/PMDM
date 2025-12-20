package ejercicio10

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/*
Ejercicio 10: Sensor de datos en tiempo real (Ejemplo: Dron con mando a distancia)
Idea:Simular un sensor que emite datos cada X segundos.

Qué practicar:
- Flow
- emit
- collect
*/

fun main() = runBlocking {
    println("--- MISIÓN DE RECONOCIMIENTO AÉREO ---")

    // 1. EL DRON EN LA MOCHILA (Definición del Flow)
    // ----------------------------------------------
    // [CONCEPTO] Flow Frío (Cold Stream).
    // Aquí solo estamos definiendo las capacidades del dron.
    // IMPORTANTE: El dron está APAGADO.
    // - No vuela.
    // - No gasta batería.
    // - No envía datos.
    val dronExplorador = telemetriaDron()

    println("1. Dron preparado en el suelo. Motores fríos.")
    delay(1000)

    println("2. Encendiendo mando a distancia (collect)... ¡DESPEGUE!")
    println("-------------------------------------------------")

    // 2. CONEXIÓN CON EL MANDO (COLLECT / CONSUMIDOR)
    // -----------------------------------------------
    // [ACCIÓN] collect { ... }
    // Es el acto de conectar el mando y dar la orden de vuelo.
    // Al ejecutar esta línea:
    // a) El dron se enciende (se ejecuta el código de 'telemetriaDron').
    // b) Tu programa se queda aquí recibiendo datos en tiempo real.
    dronExplorador.collect { datos ->

        // ESTE BLOQUE ES LA PANTALLA DEL MANDO (DOWNSTREAM)
        // Se ejecuta cada vez que el dron manda un paquete de datos.
        println("   [MANDO] Recibido por radio: $datos")

        // Reacción ante los datos recibidos
        if (datos.contains("Batería Baja")) {
            println("      >>> [ALERTA] ¡Iniciando protocolo de retorno!")
        }
    }

    println("-------------------------------------------------")
    println("--- Misión cumplida. El dron ha aterrizado y se ha apagado solo. ---")
}

// 3. EL VUELO DEL DRON (PRODUCTOR / UPSTREAM)
// -------------------------------------------
// Esta función define el comportamiento del dron cuando está encendido.
fun telemetriaDron(): Flow<String> = flow {

    // [REACCIÓN]
    // Este código empieza a correr SOLO cuando el mando (Nivel_Básico.Nivel_Intermedio.Nivel_IntermedioAvanzado.main) llama a 'collect'.
    println("   [DRON] Recibida orden de despegue. Motores al 100%...")
    delay(500) // Tiempo físico de despegue

    var altura = 0

    // Simulamos un vuelo corto de 5 segundos
    for (segundo in 1..5) {
        delay(1000) // Pasa 1 segundo de vuelo real
        altura += 10 // El dron sube 10 metros

        // Lógica interna del dron (Sensor de batería)
        val estadoBateria = if (segundo >= 4) "Batería Baja" else "Batería OK"
        val reporte = "Altura: ${altura}m | Estado: $estadoBateria"

        // [ACCIÓN] EMIT = TRANSMISIÓN DE RADIO
        // El dron envía el paquete de datos al mando.
        // El dron espera (se suspende) brevemente asegurando que el mando recibió el dato.
        println("   [DRON] >>> Transmitiendo telemetría #$segundo...")
        emit(reporte)
    }

    // Cuando el bucle termina, el Flow se cierra automáticamente.
    println("   [DRON] Rutina de vuelo finalizada. Aterrizando suavemente...")
}