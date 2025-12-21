package com.example.pmdm01_14_miniproyecto9adivinanumero.ui

data class GameUiState(
    val numeroSecreto : Int = 0,
    val intentoUsuario : String = "",
    val numeroIntentos : Int = 0,
    val mensajeEstado : String = "¡Empieza el juego!",
    val esJuegoTerminado : Boolean = false,
    val esError : Boolean = false
)
