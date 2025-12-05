package com.example.pmdm01_14_miniproyecto9adivinanumero.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class GuessNumberViewModel : ViewModel() {

    // Número secreto entre 1 y 100 (estado observable para Compose)
    var secretNumber = mutableStateOf(Random.nextInt(1, 101))
        private set

    // Mensaje que muestra la pista o acierto
    var message = mutableStateOf("")
        private set

    // Comprueba la suposición del usuario y actualiza el mensaje
    fun checkGuess(guess: Int) {
        message.value = when {
            guess < secretNumber.value -> "El número secreto es MAYOR"
            guess > secretNumber.value -> "El número secreto es MENOR"
            else -> "¡Correcto! El número secreto era " + secretNumber.value
        }
    }

    // Reinicia el juego generando un nuevo número y borrando el mensaje
    fun resetGame() {
        secretNumber.value = Random.nextInt(1, 101)
        message.value = ""
    }
}