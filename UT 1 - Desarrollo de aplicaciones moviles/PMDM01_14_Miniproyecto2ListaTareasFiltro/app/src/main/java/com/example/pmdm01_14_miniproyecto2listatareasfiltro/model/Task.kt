package com.example.pmdm01_14_miniproyecto2listatareasfiltro.model

// -----------------------------------------------------------------------------
// Data class que representa una tarea.
//
// id      → identificador único (lo asigna el ViewModel)
// text    → texto descriptivo de la tarea
// isDone  → indica si la tarea está completada o no
//
// Es inmutable por diseño: si algo cambia, se crea una copia con copy().
// Esto evita errores y funciona muy bien con Jetpack Compose.
// -----------------------------------------------------------------------------
data class Task(
    val id: Int,
    val text: String,
    val isDone: Boolean = false
)
