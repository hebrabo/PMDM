package com.example.pmdm01_14_miniproyyecto3quizpreguntas

import com.example.pmdm01_14_miniproyecto3quizpreguntas.viewModel.QuizViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Tests unitarios para verificar la lógica del QuizViewModel.
 *
 * Estos tests NO dependen de Android ni de Compose,
 * por eso se ejecutan como pruebas locales (JUnit),
 * muy rápidas y perfectas para validar lógica.
 */
class QuizViewModelTest {

    // Un ViewModel nuevo antes de cada test
    private lateinit var viewModel: QuizViewModel

    @Before
    fun setup() {
        viewModel = QuizViewModel()
    }

    // -------------------------------------------------------------------------
    // 1. Comprobar que el quiz empieza correctamente
    // -------------------------------------------------------------------------
    @Test
    fun quiz_startsCorrectly() {
        assertEquals(0, viewModel.currentQuestionIndex)
        assertEquals(0, viewModel.score)
        assertFalse(viewModel.isGameOver)
    }

    // -------------------------------------------------------------------------
    // 2. Responder bien aumenta la puntuación
    // -------------------------------------------------------------------------
    @Test
    fun submitAnswer_correct_increasesScore() {
        val correctIndex = viewModel.currentQuestion.correctAnswerIndex

        viewModel.submitAnswer(correctIndex)

        assertEquals(1, viewModel.score)
    }

    // -------------------------------------------------------------------------
    // 3. Responder mal NO aumenta la puntuación
    // -------------------------------------------------------------------------
    @Test
    fun submitAnswer_wrong_doesNotIncreaseScore() {
        val wrongIndex =
            (0..3).first { it != viewModel.currentQuestion.correctAnswerIndex }

        viewModel.submitAnswer(wrongIndex)

        assertEquals(0, viewModel.score)
    }

    // -------------------------------------------------------------------------
    // 4. Después de responder, avanza a la siguiente pregunta
    // -------------------------------------------------------------------------
    @Test
    fun submitAnswer_movesToNextQuestion() {
        val firstIndex = viewModel.currentQuestionIndex

        viewModel.submitAnswer(viewModel.currentQuestion.correctAnswerIndex)

        assertEquals(firstIndex + 1, viewModel.currentQuestionIndex)
    }

    // -------------------------------------------------------------------------
    // 5. Cuando se llega a la última pregunta, el juego termina
    // -------------------------------------------------------------------------
    @Test
    fun quizEnds_afterLastQuestion() {
        // Responder todas las preguntas
        repeat(viewModel.totalQuestions) { i ->
            viewModel.submitAnswer(viewModel.currentQuestion.correctAnswerIndex)
        }

        assertTrue(viewModel.isGameOver)
    }

    // -------------------------------------------------------------------------
    // 6. Si el juego ha terminado, no debe aceptar más respuestas
    // -------------------------------------------------------------------------
    @Test
    fun cannotAnswer_afterGameOver() {
        // Avanzar hasta el final
        repeat(viewModel.totalQuestions) {
            viewModel.submitAnswer(viewModel.currentQuestion.correctAnswerIndex)
        }

        val scoreBefore = viewModel.score

        // Intentar responder después del final
        viewModel.submitAnswer(0)

        // El score no cambia
        assertEquals(scoreBefore, viewModel.score)
    }

    // -------------------------------------------------------------------------
    // 7. Resetear reinicia todo correctamente
    // -------------------------------------------------------------------------
    @Test
    fun resetQuiz_resetsState() {
        // Cambiar el estado primero
        viewModel.submitAnswer(viewModel.currentQuestion.correctAnswerIndex)
        viewModel.submitAnswer(viewModel.currentQuestion.correctAnswerIndex)

        viewModel.resetQuiz()

        assertEquals(0, viewModel.score)
        assertEquals(0, viewModel.currentQuestionIndex)
        assertFalse(viewModel.isGameOver)
    }
}
