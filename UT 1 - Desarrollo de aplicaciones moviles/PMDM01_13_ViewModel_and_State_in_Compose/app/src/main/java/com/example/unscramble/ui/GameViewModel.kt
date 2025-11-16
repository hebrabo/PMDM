package com.example.unscramble.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.unscramble.data.allWords
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.room.util.copy
import com.example.unscramble.data.SCORE_INCREASE
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    // Estado mutable de la UI del juego.
    // Este flujo privado se puede actualizar desde el ViewModel.
    private val _uiState = MutableStateFlow(GameUiState())

    // Estado de la UI de solo lectura expuesto a la UI.
    // La IU puede observar cambios, pero no puede modificar directamente el estado.
    // `asStateFlow()` convierte el MutableStateFlow en un StateFlow de solo lectura.
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    // Propiedad observable por Compose que almacena el intento actual del usuario
    var userGuess by mutableStateOf("")
        private set

    // Conjunto de palabras usadas en el juego
    private var usedWords: MutableSet<String> = mutableSetOf()

    // Propiedad para guardar la palabra desordenada actual.
    private lateinit var currentWord: String

    init {
        resetGame()
    }


    // Metodo auxiliar para elegir una palabra aleatoria de la lista y desordenarla.
    private fun pickRandomWordAndShuffle(): String {
        // Continue picking up a new random word until you get one that hasn't been used before
        currentWord = allWords.random()
        if (usedWords.contains(currentWord)) {
            return pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }

    // Metodo auxiliar para desordenar la palabra actual
    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        // Scramble the word
        tempWord.shuffle()
        while (String(tempWord).equals(word)) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    // Función que borra todas las palabras del conjunto usedWords, e inicializa el _uiState.
    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    // Función del ViewModel para actualizar el intento del usuario
    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    // ViewModel: función para verificar si el intento del usuario es correcto
    fun checkUserGuess() {

        if (userGuess.equals(currentWord, ignoreCase = true)) {
            // Si la palabra es correcta, incrementar la puntuación
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            // Preparar el juego para la siguiente ronda:
            // - Restablecer palabra incorrecta a false
            // - Elegir nueva palabra desordenada
            // - Actualizar puntuación
            // - Incrementar contador de palabras
            updateGameState(updatedScore)
        } else {
            // Si la palabra es incorrecta, se marca el intento como erróneo en el estado de la UI
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
        }
            // Reinicia el intento del usuario para permitir un nuevo intento
        updateUserGuess("")
        }
    }

    // Función que actualiza el estado de la UI para la próxima ronda
    private fun updateGameState(updatedScore: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                isGuessedWordWrong = false, // Reinicia el estado de error
                currentScrambledWord = pickRandomWordAndShuffle(), // Elige nueva palabra desordenada
                score = updatedScore, // Actualiza la puntuación
                currentWordCount = currentState.currentWordCount.inc(), // Incrementa contador de palabras
            )
        }
    }

    fun skipWord() {
        // Actualiza el juego sin modificar la puntuación.
        // Genera una nueva palabra, incrementa el contador y resetea estado de error.
        updateGameState(_uiState.value.score)
        // Limpia el texto ingresado por el usuario para la siguiente ronda.
        updateUserGuess("")
    }
}



