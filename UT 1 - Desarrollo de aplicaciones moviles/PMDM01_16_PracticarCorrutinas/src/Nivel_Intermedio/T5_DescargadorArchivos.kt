package Nivel_Intermedio

import kotlinx.coroutines.*

/*
Idea: Simular la descarga de varios archivos al mismo tiempo.
Qué practicar:
- múltiples launch (concurrencia)
- join (sincronización manual)
- bucles con delay (simular progreso)
 */

fun main() = runBlocking {
    println("--- Gestor de Descargas Iniciado ---")

    // 1. LANZAMIENTO
    // Iniciamos 3 descargas simultáneas.
    // Cada una corre en su propia "línea de tiempo" (corrutina).
    val jobFoto = launch {
        descargarArchivo("vacaciones.jpg", 200) // Tarda poco
    }

    val jobPeli = launch {
        descargarArchivo("pelicula_4k.mkv", 500) // Tarda más
    }

    val jobDoc = launch {
        descargarArchivo("trabajo.pdf", 100) // Muy rápido
    }

    println("Main: Las descargas están corriendo en segundo plano...")

    // 2. SINCRONIZACIÓN (El uso de JOIN)
    // El código principal NO se bloquea por los launch.
    // Si queremos esperar a que terminen antes de cerrar, debemos usar join().

    // Esperamos explícitamente a cada uno:
    jobFoto.join()
    jobPeli.join()
    jobDoc.join()

    println("Main: ¡Todos los archivos se han descargado correctamente!")
}

// ---------------- FUNCIÓN DE DESCARGA ----------------

suspend fun descargarArchivo(nombre: String, tiempoPorPaquete: Long) {
    println("Iniciando descarga de: $nombre")

    // Simulamos el progreso de 0 a 100%
    for (progreso in 20..100 step 20) {
        delay(tiempoPorPaquete) // Simula la velocidad de red
        println("   ... $nombre: $progreso%")
    }

    println("$nombre GUARDADO.")
}