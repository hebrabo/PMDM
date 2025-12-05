package com.example.pmdm01_14_miniproyyecto9adivinanumero

import com.example.pmdm01_14_miniproyecto9adivinanumero.viewmodel.GuessNumberViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GuessNumberViewModelTest {

    private lateinit var viewModel: GuessNumberViewModel

    @Before
    fun setup() {
        viewModel = GuessNumberViewModel()
    }

    @Test
    fun guessLower_showsHigherMessage() {
        viewModel.secretNumber.value = 50

        viewModel.checkGuess(20)

        assertEquals("El número secreto es MAYOR", viewModel.message.value)
    }

    @Test
    fun guessHigher_showsLowerMessage() {
        viewModel.secretNumber.value = 50

        viewModel.checkGuess(80)

        assertEquals("El número secreto es MENOR", viewModel.message.value)
    }

    @Test
    fun guessCorrect_showsSuccessMessage() {
        viewModel.secretNumber.value = 30

        viewModel.checkGuess(30)

        assertEquals("¡Correcto! El número secreto era 30", viewModel.message.value)
    }

    @Test
    fun resetGame_changesSecretNumber_andClearsMessage() {
        viewModel.secretNumber.value = 50
        viewModel.message.value = "Cualquier texto"

        viewModel.resetGame()

        // comprobación del mensaje
        assertEquals("", viewModel.message.value)

        // el número secreto debe cambiar
        assertNotEquals(50, viewModel.secretNumber.value)
    }
}
