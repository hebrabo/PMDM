package com.example.pmdm01_14_miniproyecto9adivinanumero

import com.example.pmdm01_14_miniproyecto9adivinanumero.ui.GameViewModel
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameViewModelTest {

    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_Initialization_FirstStateIsCorrect() {
        val uiState = viewModel.uiState.value

        // Verifica estado inicial
        assertFalse(uiState.esJuegoTerminado)
        assertEquals(0, uiState.numeroIntentos)
        assertTrue(uiState.intentoUsuario.isEmpty())
        // Verifica que el mensaje inicial es correcto
        assertEquals("Adivina el número entre 1 y 100", uiState.mensajeEstado)
    }

    @Test
    fun gameViewModel_CorrectGuess_UpdatesStateToWon() {
        // 1. Obtenemos el número secreto que se generó aleatoriamente
        val secreto = viewModel.uiState.value.numeroSecreto

        // 2. Simulamos que el usuario escribe ese número
        viewModel.actualizarIntentoUsuario(secreto.toString())

        // 3. Comprobamos el intento
        viewModel.comprobarIntento()

        // 4. Verificamos que el juego terminó
        val uiState = viewModel.uiState.value
        assertTrue("El juego debería haber terminado", uiState.esJuegoTerminado)
        assertEquals("¡Correcto! El número era $secreto", uiState.mensajeEstado)
    }

    @Test
    fun gameViewModel_IncorrectGuess_UpdatesMessageAndAttempts() {
        // 1. Obtenemos el secreto
        val secreto = viewModel.uiState.value.numeroSecreto

        // 2. Calculamos un número incorrecto (asegurando que no nos salimos de rango lógico)
        val intentoFallido = if (secreto < 100) secreto + 1 else secreto - 1

        viewModel.actualizarIntentoUsuario(intentoFallido.toString())
        viewModel.comprobarIntento()

        val uiState = viewModel.uiState.value

        // Verifica que NO ha terminado
        assertFalse(uiState.esJuegoTerminado)
        // Verifica que aumentaron los intentos
        assertEquals(1, uiState.numeroIntentos)
        // Verifica que el input se limpió
        assertTrue(uiState.intentoUsuario.isEmpty())
    }

    @Test
    fun gameViewModel_InvalidInput_ShowsError() {
        // Test de validación (input no numérico)
        viewModel.actualizarIntentoUsuario("no es un numero")
        viewModel.comprobarIntento()

        val uiState = viewModel.uiState.value
        assertTrue("Debería marcar error", uiState.esError)
        assertEquals(0, uiState.numeroIntentos) // No debe contar como intento
    }

    @Test
    fun gameViewModel_BoundaryCase_LowerLimit() {
        // Test de caso límite inferior
        // Aunque el número secreto es random, probamos la lógica de input con el límite inferior

        viewModel.actualizarIntentoUsuario("1")
        // Solo verificamos que el sistema lo acepta y procesa, no si gana o pierde (porque depende del random)
        viewModel.comprobarIntento()

        val uiState = viewModel.uiState.value
        assertFalse("No debería dar error de formato con el 1", uiState.esError)
        assertEquals(1, uiState.numeroIntentos)
    }
}