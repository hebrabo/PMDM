package Nivel_Intermedio

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/*
Idea: Obtener temperatura, humedad y viento en paralelo.
Qué practicar:
- async: Para INICIAR tareas que devuelven un valor.
- await: Para ESPERAR y extraer ese valor.
- measureTimeMillis: Para demostrar que corren a la vez.
 */

fun main() {
    // 1. MEDICIÓN
    // Envolvemos todo en measureTimeMillis para cronometrar la ejecución.
    // Si fuera secuencial tardaría 3s (1+1+1).
    // Como es paralelo, esperamos que tarde aprox 1s (el máximo de las tres).
    val time = measureTimeMillis {

        runBlocking {
            println("--- Pronóstico del clima (Día de tormenta) ---")

            // 2. INICIO CONCURRENTE (async)
            // 'async' arranca la corrutina INMEDIATAMENTE en segundo plano.
            // A diferencia de 'launch', 'async' devuelve un 'Deferred<T>'.
            // 'Deferred' es un contenedor que tendrá el String en el futuro.

            val temperatura: Deferred<String> = async {
                getTemperatura()
            }

            // En este punto, 'Nivel_Intermedio.getTemperatura' ya está corriendo.
            // No esperamos a que termine; lanzamos inmediatamente la siguiente.
            val humedad: Deferred<String> = async {
                getHumedad()
            }

            // Ahora tenemos 2 corriendo. Lanzamos la tercera.
            val viento: Deferred<String> = async {
                getViento()
            }

            println("   ...Esperando resultados simultáneos...")

            // 3. SINCRONIZACIÓN Y EXTRACCIÓN (await)
            // .await() es el punto de encuentro.
            // Suspende la corrutina principal hasta que el resultado de 'temperatura' esté listo.
            // Como 'humedad' y 'viento' empezaron hace rato, sus resultados llegan casi a la vez.
            println("${temperatura.await()} ${humedad.await()} ${viento.await()}")

            println("¡Un fantástico día para quedarse en casa!")
        }
    }

    println("\n--- Estadísticas ---")
    println("Tiempo total de ejecución: ${time / 1000.0} segundos")
}

// ---------------- FUNCIONES SUSPENDIDAS ----------------

suspend fun getTemperatura(): String {
    delay(1000) // Simula tiempo de red
    return "Temperatura:7\u00b0C - "
}

suspend fun getHumedad(): String {
    delay(1000)
    return "Humedad:98% - "
}

suspend fun getViento(): String {
    delay(1000)
    return "Viento:65Km/h"
}