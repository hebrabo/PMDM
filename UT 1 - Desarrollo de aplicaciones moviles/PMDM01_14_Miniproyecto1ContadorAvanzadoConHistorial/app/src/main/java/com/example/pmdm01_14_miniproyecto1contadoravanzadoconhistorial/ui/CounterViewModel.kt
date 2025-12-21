package com.example.pmdm01_14_miniproyecto1contadoravanzadoconhistorial.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pmdm01_14_miniproyecto1contadoravanzadoconhistorial.ui.CounterUiState

class CounterViewModel : ViewModel() {

    // Estado observable del contador
    // Compose actualiza la pantalla automáticamente si cambia este valor
    var count = mutableStateOf(0)
        private set

    // Lista observable donde guardamos el historial de operaciones
    var history = mutableStateListOf<CounterUiState>()
        private set

    // Función para sumar 1
    fun increment() {
        count.value++
        history.add(CounterUiState("Sumar 1", count.value))
    }

    // Función para restar 1
    fun decrement() {
        count.value--
        history.add(CounterUiState("Restar 1", count.value))
    }

    // Función para reiniciar el contador a cero
    fun reset() {
        count.value = 0
        history.add(CounterUiState("Reiniciar contador", 0))
    }
}