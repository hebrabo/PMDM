fun main() {
    var nombre: String?

    do {
        print("Por favor, ingresa tu nombre: ")
        nombre = readLine()
        if (nombre.isNullOrBlank()) {
            println("Debes ingresar un nombre válido.")
        }
    } while (nombre.isNullOrBlank())

    val usuario = Usuario(nombre, ListaTareas())

    var opcion: Int
    var contadorId = 1

    usuario.saludar()

    do {
        println(
            """
            
            ==== MENÚ DE TAREAS ====
            1. Agregar tarea
            2. Mostrar tareas
            3. Marcar tarea como completada
            4. Eliminar tarea
            5. Ver solo tareas completadas
            6. Salir
            =========================
            """.trimIndent()
        )
        print("Selecciona una opción: ")
        opcion = readLine()?.toIntOrNull() ?: 0

        when (opcion) {
            1 -> {
                print("Título de la tarea: ")
                val titulo = readLine() ?: ""
                print("Descripción: ")
                val descripcion = readLine() ?: ""
                val tarea = Tareas(contadorId++, titulo, descripcion)
                usuario.lista.agregarTarea(tarea)
            }
            2 -> usuario.lista.mostrarTareas()
            3 -> {
                print("Ingresa el ID de la tarea a marcar como completada: ")
                val id = readLine()?.toIntOrNull()
                if (id != null) {
                    val tarea = usuario.lista.buscarTarea(id)
                    if (tarea != null) {
                        tarea.marcarComoCompletada()
                        println("Tarea marcada como completada.")
                    } else {
                        println("No se encontró la tarea con ID $id.")
                    }
                }
            }
            4 -> {
                print("Ingresa el ID de la tarea a eliminar: ")
                val id = readLine()?.toIntOrNull()
                if (id != null) usuario.lista.eliminarTarea(id)
            }
            5 -> usuario.lista.tareasCompletadas()
            6 -> println("¡Hasta luego, $nombre!")
            else -> println("Opción no válida. Intenta de nuevo.")
        }
    } while (opcion != 6)
}