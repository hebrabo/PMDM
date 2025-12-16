import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/*
Idea: Obtener temperatura, humedad y viento en paralelo.

Qué practicar:
- async / await
- concurrencia real
- medición de tiempo (measureTimeMillis)


 */
fun main() {
    val time = measureTimeMillis {
        runBlocking {
            println("--- Pronóstico del clima (Día de tormenta) ---")

            val temperatura: Deferred<String> = async {
            getTemperatura()
            }

            val humedad: Deferred<String> = async {
            getHumedad()
            }

            val viento: Deferred<String> = async {
                getViento()
            }
            println("${temperatura.await()} ${humedad.await()} ${viento.await()}")

            println("¡Un fantástico día para quedarse en casa!")
        }
    }

    println("\n--- Estadísticas ---")
    println("Tiempo total de ejecución: ${time / 1000.0} segundos")
}

suspend fun getTemperatura(): String {
    delay(1000)
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