package com.example.pmdm01_14_miniproyecto9adivinanumero.ui

/**
 * GameUiState: Representa el estado de la pantalla en un momento específico.
 * * En la arquitectura MVVM con Compose:
 * 1. Es INMUTABLE (val): No se pueden cambiar los valores individualmente.
 * 2. La UI solo muestra lo que hay aquí.
 * Si no está en esta clase, no existe en la pantalla.
 */
data class GameUiState(
    // El número que el usuario debe adivinar (Lógica interna)
    // Aunque no se muestra directamente, es parte del estado del juego actual.
    val numeroSecreto: Int = 0,

    // El texto que guía al usuario (ej: "Mayor", "Menor", "Ganaste")
    val mensajeEstado: String = "",

    // Contador de intentos realizados
    val numeroIntentos: Int = 0,

    // Lo que el usuario está escribiendo en el TextField.
    // Es String porque los campos de texto manejan cadenas (y para manejar vacío "").
    val intentoUsuario: String = "",

    // Bandera para cambiar la UI cuando se gana (ej: mostrar botón "Jugar otra vez")
    val esJuegoTerminado: Boolean = false,

    // Bandera para activar el color rojo en el TextField si escriben letras o nada
    val esError: Boolean = false
)
