package com.example.unscramble.ui

// Data class que representa el estado de la UI del juego
data class GameUiState(
    val currentScrambledWord: String = "", // Palabra desordenada actual
    val isGuessedWordWrong: Boolean = false, // Indica si el intento del usuario es incorrecto
)
