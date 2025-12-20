package `Nivel_Básico`/*
* Simula un proceso con varias tareas (por ejemplo: login → cargar perfil → cargar preferencias).
* Qué practicar:
* - runBlocking
* - funciones suspend
* - delay
* - ejecución secuencial
 */

import kotlin.system.*
import kotlinx.coroutines.*

fun main() {
    println("--- Iniciando App ---")

    // Medimos el tiempo total de ejecución
    val time = measureTimeMillis {

        // runBlocking: bloquea el hilo principal
        // hasta que todas las corrutinas dentro terminen.
        runBlocking {
            loginUsuario()      // Paso 1
            cargarPerfil()      // Paso 2
            cargarPreferencias() // Paso 3
            println("¡Bienvenido! El sistema está listo.")
        }
    }

    println("\n--- Estadísticas ---")
    println("Tiempo total de ejecución: ${time / 1000.0} segundos")
}

// ---------------- FUNCIONES SUSPENDIDAS ----------------

// Simula una petición al servidor para loguear
suspend fun loginUsuario() {
    print("Autenticando usuario... ")
    delay(1000) // Simula una espera de 1 segundo
    println("OK")
}

// Simula la descarga de datos del usuario
suspend fun cargarPerfil() {
    print("Descargando perfil... ")
    delay(1000)
    println("Datos recibidos")
}

// Simula la configuración de la UI según preferencias
suspend fun cargarPreferencias() {
    print("Aplicando preferencias... ")
    delay(1000)
    println("Configuración aplicada")
}
