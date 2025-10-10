class Tareas(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    var completada: Boolean = false
) {
    fun marcarComoCompletada() {
        completada = true
    }

    fun mostrarInfo()  {
        val estado = if (completada) "[✔]" else "[ ]"
        println("$estado $id - $titulo ($descripcion)")
    }
}