package com.example.pmdm01_14_miniproyecto1contadorhistorial.model

// Modelo simple que representa una operación del historial.
// Guarda una descripción ("Sumar 1") y el valor del contador después de esa acción.
data class Operation(
    val description: String,
    val newValue: Int
)
