package com.example.unscramble.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.unscramble.data.allWords

class GameViewModel : ViewModel() {

    // Estado mutable de la UI del juego.
    // Este flujo privado se puede actualizar desde el ViewModel.
    private val _uiState = MutableStateFlow(GameUiState())

    // Estado de la UI de solo lectura expuesto a la UI.
    // La IU puede observar cambios, pero no puede modificar directamente el estado.
    // `asStateFlow()` convierte el MutableStateFlow en un StateFlow de solo lectura.
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

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
}

