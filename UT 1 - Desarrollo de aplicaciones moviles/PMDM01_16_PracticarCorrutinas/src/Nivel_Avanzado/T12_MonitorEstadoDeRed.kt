package ejercicio12

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/*
Ejercicio 12: Monitor de estado de red (SharedFlow). Ejemplo: Un estación de radio (Transmisión en vivo)
Idea: Emitir cambios de estado de red (conectado/desconectado).

Qué practicar:
- SharedFlow
- múltiples observadores
- hot vs cold streams

*/

fun main() = runBlocking {
    println("=== ESTACIÓN DE RADIO 'NETWORK NEWS' EN EL AIRE  ===")
    println()

    // 1. LA FRECUENCIA DE RADIO (SharedFlow)
    // --------------------------------------
    // Definimos el canal de comunicación.
    // - replay = 0: Es "EN VIVO". No guarda las noticias pasadas.
    // - extraBufferCapacity = 5: Si el locutor habla muy rápido, guardamos 5 palabras en cola.
    val radioFrecuencia = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 5)

    // 2. OYENTE 1: LA GRABADORA (Logs)
    // --------------------------------
    // Se suscribe (sintoniza) desde el principio.
    val jobGrabadora = launch {
        radioFrecuencia.collect { noticia ->
            println("  [GRABADORA]  >> Grabando: $noticia")
        }
    }

    // 3. OYENTE 2: EL CONDUCTOR (UI)
    // ------------------------------
    // Otro suscriptor independiente que también sintoniza desde el inicio.
    val jobConductor = launch {
        radioFrecuencia.collect { noticia ->
            println("  [CONDUCTOR]  >> Escuchando alerta: ¡$noticia!")
        }
    }

    // [SINCRONIZACIÓN]
    // Las corrutinas 'launch' tardan unos milisegundos en arrancar y llegar al 'collect'.
    // Si emitimos inmediatamente, el mensaje se perdería en el vacío (nadie estaría escuchando aún).
    // Damos 100ms para asegurar que las radios están encendidas.
    delay(100)

    // --- EL LOCUTOR COMIENZA EL PROGRAMA ---
    println("LOCUTOR: ¡Interrumpimos la programación! Noticia urgente...")

    // [EMISIÓN MULTICAST]
    // Al ejecutar esta ÚNICA línea, el mensaje viaja a la Grabadora Y al Conductor.
    radioFrecuencia.emit("URGENTE_RED_CAIDA")

    delay(1000)

    println("LOCUTOR: Seguimos informando...")
    radioFrecuencia.emit("INFORME_REINTENTANDO_CONEXION...")

    delay(1000)

    // --- LLEGA UN OYENTE TARDE ---
    println("\n[OYENTE DORMILÓN] Se despierta y enciende la radio (Llega tarde)...")

    // 4. OYENTE 3: EL DORMILÓN (Analytics)
    // -------------------------------------
    // [CONCEPTO CLAVE: REPLAY = 0]
    // Este oyente se conecta AHORA.
    // Como el SharedFlow no tiene memoria (replay=0), este oyente NO recibe
    // los mensajes anteriores ("RED_CAIDA" ni "REINTENTANDO").
    // Es como encender la radio a mitad de una canción: no escuchas el principio.
    val jobDormilon = launch {
        radioFrecuencia.collect { noticia ->
            println("                  [DORMILÓN]   >> Acabo de sintonizar: $noticia")
        }
    }

    // Tiempo técnico para que el dormilón sintonice
    delay(100)

    // --- NOTICIA FINAL ---
    println("\nLOCUTOR: ¡Última hora! Problema resuelto.")

    // [TODOS ESCUCHAN]
    // Ahora hay 3 suscriptores activos. Esta emisión llega a los tres.
    radioFrecuencia.emit("FLASH_CONEXION_RESTABLECIDA")

    delay(1000)
    println("\n=== FIN DE LA TRANSMISIÓN ===")

    // [LIMPIEZA]
    // A diferencia de un Flow normal que termina cuando se acaban los datos,
    // un SharedFlow se queda abierto esperando mensajes eternamente.
    // Debemos cancelar los trabajos manualmente para terminar el programa.
    jobGrabadora.cancel()
    jobConductor.cancel()
    jobDormilon.cancel()
}