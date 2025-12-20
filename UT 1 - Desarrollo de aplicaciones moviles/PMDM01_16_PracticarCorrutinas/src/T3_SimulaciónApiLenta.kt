import kotlinx.coroutines.*

/*
Idea:
Una función suspendida que simule una llamada a una API (con delay) y devuelva datos ficticios.

Qué practicar:
- suspend fun
- manejo de resultados
- separación de funciones
*/

fun main() = runBlocking {
    println("--- App Iniciada ---")

    println("Pidiendo datos al servidor...")

    // 1. LLAMADA: Llamamos a la función suspendida.
    // Como es secuencial, el programa se PAUSA aquí hasta que 'obtenerNombreUsuario' termine.
    // El resultado (el String) se guarda en la variable 'usuario'.
    val usuario = obtenerNombreUsuario()

    // 2. USO DE DATOS: Esta línea no se ejecuta hasta que la anterior termina.
    println("Datos recibidos con éxito.")
    println("Hola, $usuario. Bienvenida al sistema.")
}

// ---------------- FUNCIONES ----------------

// Esta función devuelve un String (como una función normal),
// pero tiene el poder de suspenderse.
suspend fun obtenerNombreUsuario(): String {
    // Simulamos que la red tarda 2 segundos en responder
    delay(2000)

    // Devolvemos el dato ficticio
    return "Helena"
}