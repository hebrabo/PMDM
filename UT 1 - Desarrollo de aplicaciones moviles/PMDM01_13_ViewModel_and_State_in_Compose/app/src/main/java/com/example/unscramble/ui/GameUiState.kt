package com.example.unscramble.ui

// Data class que representa el estado de la UI del juego
data class GameUiState(
    val currentScrambledWord: String = "", // Palabra desordenada actual
    val isGuessedWordWrong: Boolean = false, // Indica si el intento del usuario es incorrecto
    val score: Int = 0, // Puntuación acumulada del jugador
    val currentWordCount: Int = 1, // Número de palabra actual en el juego
    val isGameOver: Boolean = false // Indica si se alcanzó el límite de palabras y el juego terminó
)
