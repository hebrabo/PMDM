package com.example.pmdm01_14_miniproyecto3quizpreguntas.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pmdm01_14_miniproyecto3quizpreguntas.model.Question

// -----------------------------------------------------------------------------
// ViewModel con toda la lógica del juego:
//  - Lista de preguntas
//  - Respuestas correctas
//  - Avance entre preguntas
//  - Puntuación
//  - Control de fin del juego
// -----------------------------------------------------------------------------
class QuizViewModel : ViewModel() {

    // -------------------------------------------------------------------------
    // Lista fija de preguntas (en una app real sería un repositorio o API)
    // -------------------------------------------------------------------------
    private val questions = listOf(
        Question(
            text = "¿Qué palabra clave se usa para definir una constante en Kotlin?",
            options = listOf("var", "let", "val", "const"),
            correctAnswerIndex = 2 // "val"
        ),
        Question(
            text = "¿Qué anotación se usa en Compose para crear una UI?",
            options = listOf("@Composable", "@UI", "@AndroidView", "@Compose"),
            correctAnswerIndex = 0 // "@Composable"
        ),
        Question(
            text = "¿Cuál es el componente raíz para la navegación en Android moderno?",
            options = listOf("Intent", "NavController", "NavHost", "Activity"),
            correctAnswerIndex = 2 // "NavHost"
        )
    )

    // Índice de la pregunta actual
    var currentQuestionIndex by mutableStateOf(0)
        private set

    // Puntuación
    var score by mutableStateOf(0)
        private set

    // Indica si ya no quedan preguntas
    var isGameOver by mutableStateOf(false)
        private set

    // Pregunta actual (cómodo para la UI)
    val currentQuestion: Question
        get() = questions[currentQuestionIndex]

    // Total de preguntas
    val totalQuestions = questions.size

    // -------------------------------------------------------------------------
    // Llamado cuando el usuario selecciona una opción.
    // Actualiza puntuación, avanza de pregunta o termina el juego.
    // -------------------------------------------------------------------------
    fun submitAnswer(selectedIndex: Int) {
        if (isGameOver) return

        // Si la respuesta es correcta, sumar un punto
        if (selectedIndex == currentQuestion.correctAnswerIndex) {
            score++
        }

        // Pasar a la siguiente pregunta
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
        } else {
            // Si no hay más preguntas → fin del juego
            isGameOver = true
        }
    }

    // Reiniciar el quiz
    fun resetQuiz() {
        score = 0
        currentQuestionIndex = 0
        isGameOver = false
    }
}