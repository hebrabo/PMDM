class ListaTareas {
    private val tareas: MutableList<Tareas> = mutableListOf()

    fun agregarTarea(tarea: Tareas){
        tareas.add(tarea)
        println("Tarea agregada: ${tarea.titulo}")
    }

    fun mostrarTareas(){
        if (tareas.isEmpty()){
            println("No hay tareas registradas")
        } else {
            println ("Lista de tareas:")
            tareas.forEach { tarea -> tarea.mostrarInfo() }
        }
    }

    fun buscarTarea(id: Int): Tareas? {
    return tareas.find { tarea -> tarea.id == id }
    }


    fun eliminarTarea(id: Int){
        val tarea = buscarTarea(id)
        if (tarea != null){
            tareas.remove(tarea)
            println("Tarea eliminada: ${tarea.titulo}")
        } else {
            println("No se encontró la tarea con ID $id")
        }
    }

    fun tareasCompletadas() {
        val completadas = tareas.filter { tarea -> tarea.completada }
        if (completadas.isEmpty()) {
            println("No hay tareas completadas")
        } else {
            println("Tareas completadas:")
            completadas.forEach { tarea -> tarea.mostrarInfo() }
        }
    }

}
