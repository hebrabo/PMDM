package `Nivel_Básico`

import kotlinx.coroutines.*

/*
Idea: Crear un temporizador que cuente segundos sin bloquear el hilo principal.
Qué practicar:
- launch (crear corrutina)
- delay (suspensión no bloqueante)
- cancelación básica (control del ciclo de vida)
 */

// runBlocking: Habilita el uso de corrutinas (launch, delay) dentro del Nivel_Básico.Nivel_Intermedio.Nivel_IntermedioAvanzado.main
// y obliga al programa a ESPERAR a que todas terminen antes de cerrarse.
fun main() = runBlocking {
    println("--- Programa iniciado ---")

    // 1. INICIO (CONCURRENCIA)
    // 'launch': Inicia una nueva corrutina en "paralelo" (concurrentemente).
    // No bloquea el código que sigue debajo. El programa sigue avanzando.
    // Guardamos el resultado en 'timerJob' para poder cancelar esta corrutina más tarde.
    val timerJob = launch {
        iniciarContador()
    }

    // 2. EJECUCIÓN (HILO PRINCIPAL)
    // Mientras el 'timerJob' cuenta números en segundo plano,
    // el hilo principal ejecuta esta función inmediatamente.
    simularNavegacionUsuario()

    // 3. CIERRE (CANCELACIÓN)
    println("Main: Usuario salió. Deteniendo timer...")

    // 'cancelAndJoin': Es una combinación de dos acciones:
    // a) cancel(): Envía la señal de "apágate" al Job.
    // b) join(): Espera hasta que la corrutina confirma que ha terminado totalmente
    timerJob.cancelAndJoin()

    println("--- Programa finalizado ---")
}

// ---------------- FUNCIONES SUSPENDIDAS ----------------

// 'suspend': Indica que esta función puede pausarse y reanudarse.
// Solo puede llamarse desde otra función suspendida o una corrutina (como launch).
suspend fun iniciarContador() {
    var segundos = 0
    println("Timer arrancado")

    // while (true): En programación clásica esto bloquearía el programa para siempre.
    // En corrutinas es seguro PORQUE usamos 'delay'.
    while (true) {
        // delay(1000):
        // 1. Libera el hilo durante 1 seg (no lo congela).
        // 2. ES EL PUNTO DE CONTROL: Aquí es donde Kotlin revisa "¿Me han cancelado?".
        //    Si timerJob.cancel() fue llamado, delay lanza una excepción y rompe el bucle.
        delay(1000)

        segundos++
        println("   -> Timer: $segundos s")
    }
}

// Función auxiliar para simular trabajo
suspend fun simularNavegacionUsuario() {
    println("Main: Usuario navegando...")
    // Simulamos que el usuario está 3.5 segundos en la pantalla.
    delay(3500)
}