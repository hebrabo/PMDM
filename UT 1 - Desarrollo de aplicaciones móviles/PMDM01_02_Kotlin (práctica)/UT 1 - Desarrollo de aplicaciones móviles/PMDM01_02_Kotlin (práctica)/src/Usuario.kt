class Usuario(
    val nombre: String,
    val lista: ListaTareas
) {
    fun saludar() {
        println("Hola, soy $nombre. Estas son mis tareas pendientes:")
    }
}
