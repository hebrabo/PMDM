import kotlinx.coroutines.*

/*
Idea: Una app que ejecute tareas largas y permita cancelarlas de forma segura.
Qué practicar:
- Job
- cancel() y isActive
- try/finally

 */

fun main() = runBlocking {
    println("--- Editor de Video Iniciado ---")

    // 1. INICIO DE LA TAREA
    // Guardamos el Job para tener el control.
    val renderizadoJob = launch {

        // BLOQUE TRY: Aquí va la tarea larga que podría ser cancelada.
        try {
            println("Iniciando renderizado del video...")

            // Simulamos 5 pasos de trabajo pesados
            for (i in 1..5) {
                println("   ... Procesando parte $i/5")
                // delay comprueba si el Job sigue activo.
                // Si llamamos a cancel(), delay lanza una 'CancellationException' AQUÍ MISMO.
                delay(1000)
            }
            println("¡Video exportado con éxito!") // Esto no llegará a imprimirse si cancelamos.

        } finally {
            // 2. BLOQUE FINALLY
            // Este código se ejecuta SIEMPRE:
            // - Si todo terminó bien.
            // - Si hubo un error.
            // - O SI FUE CANCELADO.
            println("(Limpieza) Cerrando archivos temporales y liberando memoria...")
        }
    }

    // 3. INTERACCIÓN DEL USUARIO
    // Dejamos que trabaje un poco (2.5 segundos, llegará a la parte 2 o 3)
    delay(2500)

    println("Usuario: ¡Cancelar! Me he equivocado de video.")

    // 4. CANCELACIÓN
    // Enviamos la señal y esperamos a que el 'finally' termine.
    renderizadoJob.cancelAndJoin()

    println("--- Editor Cerrado ---")
}