package ejercicio11

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/*
Ejercicio 11: Buscador Reactivo (Ej: Buscador autocompletao tipo Google)
Idea:Simular búsquedas mientras el usuario escribe.

Qué practicar:
- StateFlow
- debounce
- collectLatest
*/

fun main() {
    runBlocking {
        println("--- BUSCADOR INTELIGENTE INICIADO ---")

        // 1. LA CAJA DE BÚSQUEDA (StateFlow)
        // ----------------------------------
        // MutableStateFlow es un "Hot Stream". Siempre está activo.
        // Nunca dice "he terminado", siempre espera datos futuros.
        val cajaBusqueda = MutableStateFlow("")

        // 2. EL MOTOR DE BÚSQUEDA (Consumidor)
        // ------------------------------------
        // Guardamos la referencia de esta corrutina en una variable llamada 'jobBuscador'.
        // ¿Por qué? Porque 'collectLatest' sobre un StateFlow es un bucle infinito.
        // Si no guardamos el control, runBlocking se quedará esperando eternamente a que termine.
        val jobBuscador = launch {
            cajaBusqueda
                // [DEBOUNCE] "El Cronómetro"
                // Solo deja pasar el dato si hay silencio durante 500ms.
                // Si el usuario teclea rápido (cada 100ms), esto frena las peticiones.
                .debounce(500)

                // [FILTER] Lógica de negocio
                .filter { texto -> texto.isNotBlank() }

                // [COLLECT LATEST]
                // Si llega una nueva búsqueda ("Java") mientras procesamos la vieja ("Kotlin"):
                // 1. Mata la vieja (lanza CancellationException en 'simularBusqueda').
                // 2. Empieza la nueva inmediatamente.
                .collectLatest { query ->
                    println("   [SISTEMA] Silencio detectado. Buscando: '$query'...")
                    simularBusquedaEnBaseDeDatos(query)
                }
        }

        // 3. EL USUARIO (Simulación de escritura)
        // ---------------------------------------
        launch {
            println("1. Usuario empieza a escribir 'KOTLIN' muy rápido...")
            val palabras = listOf("K", "Ko", "Kot", "Kotl", "Kotli", "Kotlin")

            for (letra in palabras) {
                cajaBusqueda.value = letra
                println("   (Usuario teclea: '$letra')")
                // 100ms < 500ms -> El debounce bloquea, no se busca nada.
                delay(100)
            }

            println("2. Usuario terminó de escribir (Pausa larga).")
            // 1000ms > 500ms -> El debounce deja pasar "Kotlin".
            delay(1000)

            println("3. Usuario cambia de opinión y escribe 'JAVA'...")
            // Esto provocará que collectLatest cancele la búsqueda de "Kotlin" si sigue viva.
            cajaBusqueda.value = "Java"

            // Esperamos para ver el resultado final
            delay(3000)

            println("--- Fin de la simulación ---")

            // Como el 'jobBuscador' es un flujo infinito, tenemos que matarlo manualmente
            // para que 'runBlocking' sepa que ya no queda trabajo pendiente y cierre el programa.
            println("   (Apagando el motor de búsqueda manualmente...)")
            jobBuscador.cancel()
        }
    }
}

// Función que simula una petición lenta (IO Operation)
suspend fun simularBusquedaEnBaseDeDatos(query: String) {
    try {
        // Simulamos latencia de 2 segundos
        delay(2000)
        println("   [RESULTADO] Encontrados 50 artículos sobre '$query'")

    } catch (e: CancellationException) {
        // [CANCELACIÓN]
        // Cuando collectLatest corta la ejecución, el código salta aquí.
        println("   [CANCELADO] Búsqueda de '$query' abortada (Llegó un dato nuevo).")
        throw e // Siempre relanzar la excepción
    }
}