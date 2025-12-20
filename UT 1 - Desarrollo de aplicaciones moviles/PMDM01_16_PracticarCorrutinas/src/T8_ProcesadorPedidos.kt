import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

/*
Ejercicio 8: Procesador de pedidos
Idea: Simular un sistema que recibe pedidos y los procesa en paralelo, con límite de concurrencia.

Qué practicar:
- CoroutineScope
- Dispatcher
- Semaphore
*/

fun main() = runBlocking {
    println("--- Sistema de Pedidos Iniciado ---")

    // 1. SEMÁFORO: El "portero". Solo tiene 3 tickets (permisos).
    // Si llegan más de 3, los pone en fila de espera.
    val estacionesDeEmpaquetado = Semaphore(3)

    // Simulamos una lista de 10 pedidos
    val pedidos = (1..10).toList()

    // 2. COROUTINE SCOPE: Usamos un scope supervisor o simplemente runBlocking
    // para lanzar las corrutinas.
    // Usamos Dispatchers.Default para simular trabajo en hilos de background.
    pedidos.forEach { numeroPedido ->
        launch(Dispatchers.Default) {
            println("Pedido #$numeroPedido: Recibido y esperando turno...")

            // 3. USO DEL SEMÁFORO
            // withPermit adquiere el permiso, ejecuta el bloque y lo libera automáticamente (como try-finally).
            // La corrutina se SUSPENDE aquí si no hay permisos libres.
            estacionesDeEmpaquetado.withPermit {
                procesarPedido(numeroPedido)
            }
        }
    }
}

// Función suspendida que simula el trabajo pesado
suspend fun procesarPedido(id: Int) {
    println(">>> [PROCESANDO] Pedido #$id en ${Thread.currentThread().name}")
    delay(2000) // Tardan 2 segundos en procesarse
    println("<<< [FINALIZADO] Pedido #$id")
}