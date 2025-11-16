package com.example.unscramble.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    // Estado mutable de la UI del juego.
    // Este flujo privado se puede actualizar desde el ViewModel.
    private val _uiState = MutableStateFlow(GameUiState())

    // Estado de la UI de solo lectura expuesto a la UI.
    // La IU puede observar cambios, pero no puede modificar directamente el estado.
    // `asStateFlow()` convierte el MutableStateFlow en un StateFlow de solo lectura.
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
}