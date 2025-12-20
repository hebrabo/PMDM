package Nivel_Intermedio

import kotlinx.coroutines.*

/*
Idea: Enviar notificaciones periódicas mientras el usuario está activo.
Qué practicar:
- launch
- while (isActive)
- Job.cancel()
 */

fun main() = runBlocking {
    println("--- Sistema Iniciado ---")

    // 1. INICIO
    val servicioJob = launch {
        enviarNotificaciones()
    }

    // 2. USO (Simulación)
    simularActividadUsuario()

    // 3. CIERRE
    println("Logout: Usuario cerrando sesión...")
    servicioJob.cancelAndJoin() // Apagamos el servicio

    println("--- Sistema Apagado ---")
}

// ---------------- FUNCIONES ----------------

// Esto crea un entorno de corrutina dentro de la función.
// Ventaja: Nos da acceso directo a variables de control como 'isActive'.
suspend fun enviarNotificaciones() = coroutineScope {
    println("Servicio de notificaciones activado")

    // 'isActive' está conectado directamente al 'servicioJob' del Nivel_Básico.Nivel_Intermedio.Nivel_IntermedioAvanzado.main.
    // Si se llama a .cancel(), esta variable cambia automáticamente a 'false'
    // y el bucle se rompe.
    while (isActive) {
        println("Ping: Tienes una nueva notificación")
        delay(1000)
    }
}

suspend fun simularActividadUsuario() {
    println("Usuario leyendo noticias...")
    delay(3500) // Simula actividad bloqueando el flujo principal 3.5s
}