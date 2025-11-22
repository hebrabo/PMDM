package com.example.unscramble.data.ui.test

import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse

/* Test unitario de JUnit (Ruta de éxito) que verifica que el GameViewModel del juego Unscramble funciona correctamente cuando el usuario acierta una palabra.
- Simula que el usuario introduce la palabra correcta
- Verifica que el ViewModel actualiza bien el puntaje y el estado del juego
 */

class GameViewModelTest {
    // Creamos una instancia real del ViewModel.
    // Esto nos permite probar cómo cambia el estado del juego igual que en la app.
    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset()  {
        // Obtenemos el estado inicial del juego desde el ViewModel.
        // Aquí vemos cuál es la palabra mezclada que el usuario debería adivinar.
        var currentGameUiState = viewModel.uiState.value

        // Convertimos la palabra mezclada en su versión correcta.
        // En un escenario real esto sería equivalente a que el usuario ingrese la palabra correcta.
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        // Simulamos que el usuario escribe una palabra.
        viewModel.updateUserGuess(correctPlayerWord)
        // Simulamos que el usuario presiona “Submit”.
        viewModel.checkUserGuess()

        // Volvemos a leer el estado del juego después de la verificación.
        currentGameUiState = viewModel.uiState.value

        // Comprobamos que el ViewModel NO active la bandera de error,
        // ya que la palabra ingresada era correcta.
        assertFalse(currentGameUiState.isGuessedWordWrong)

        // Verificamos que el puntaje haya aumentado correctamente.
        // Como es la primera palabra acertada, el puntaje esperado es SCORE_INCREASE (20).
        assertEquals(20, currentGameUiState.score)

        // Esta verificación usa una constante para dejar claro el valor esperado semánticamente.
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)

    }
    companion object {
        // Representa el puntaje esperado después de acertar la primera palabra.
        // Usamos SCORE_INCREASE para mantener coherencia con la lógica del juego.
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }
}