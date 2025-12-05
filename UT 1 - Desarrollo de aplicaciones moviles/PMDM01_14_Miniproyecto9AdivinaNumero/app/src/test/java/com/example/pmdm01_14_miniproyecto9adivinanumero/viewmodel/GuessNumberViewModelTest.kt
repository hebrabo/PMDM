package com.example.pmdm01_14_miniproyecto9adivinanumero.viewmodel

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
    fun guessLowerThanSecret_showsHigherMessage() {
        // Fijamos el número secreto a 50 para test controlado
        viewModel.secretNumber.value = 50
        viewModel.checkGuess(30)
        assertEquals("El número secreto es MAYOR", viewModel.message.value)
    }

    @Test
    fun guessHigherThanSecret_showsLowerMessage() {
        viewModel.secretNumber.value = 50
        viewModel.checkGuess(70)
        assertEquals("El número secreto es MENOR", viewModel.message.value)
    }

    @Test
    fun guessEqualToSecret_showsCorrectMessage() {
        viewModel.secretNumber.value = 50
        viewModel.checkGuess(50)
        assertEquals("¡Correcto! El número secreto era 50", viewModel.message.value)
    }

    @Test
    fun resetGame_clearsMessageAndChangesSecretNumber() {
        val oldNumber = viewModel.secretNumber.value
        viewModel.message.value = "Test"
        viewModel.resetGame()
        // Verificamos que el mensaje se ha limpiado
        assertEquals("", viewModel.message.value)
        // Verificamos que el número secreto ha cambiado (puede fallar rara vez si Random da mismo valor)
        assertNotEquals(oldNumber, viewModel.secretNumber.value)
    }
}
