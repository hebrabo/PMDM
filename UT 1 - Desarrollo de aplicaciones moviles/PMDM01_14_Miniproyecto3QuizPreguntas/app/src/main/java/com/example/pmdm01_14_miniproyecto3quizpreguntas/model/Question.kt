package com.example.pmdm01_14_miniproyecto3quizpreguntas.model

// -----------------------------------------------------------------------------
// Modelo que representa una pregunta del quiz.
//
// text → enunciado
// options → lista de respuestas posibles
// correctAnswerIndex → índice de la respuesta correcta
//
// Es una data class para que sea inmutable y fácil de manejar.
// -----------------------------------------------------------------------------
data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)