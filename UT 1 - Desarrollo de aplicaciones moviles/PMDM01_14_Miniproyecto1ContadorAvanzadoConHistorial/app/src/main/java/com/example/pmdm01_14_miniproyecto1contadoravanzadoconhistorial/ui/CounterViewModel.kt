package com.example.pmdm01_14_miniproyecto1contadoravanzadoconhistorial.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CounterViewModel : ViewModel() {

    // ------------------------------------------------------------------------
    // ESTADO
    // ------------------------------------------------------------------------
    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    init {
        reiniciarContador()
    }

    // ------------------------------------------------------------------------
    // FUNCIONES
    // ------------------------------------------------------------------------

    fun sumar() {
        _uiState.update { currentState ->

            // 1. CÁLCULOS PREVIOS (Variables temporales)
            // Calculamos aquí el valor limpio antes de meterlo en el estado
            val valorCalculado = currentState.contador + 1

            // Usamos el valor que acabamos de calcular para crear el mensaje
            val historialCalculado = listOf("Sumando (+1) -> $valorCalculado") + currentState.historial

            // 2. ACTUALIZACIÓN DEL ESTADO
            // Ahora asignamos las variables ya calculadas
            currentState.copy(
                contador = valorCalculado,
                historial = historialCalculado
            )
        }
    }

    fun restar() {
        _uiState.update { currentState ->

            // 1. CÁLCULOS PREVIOS (Variables temporales)
            // Calculamos aquí el valor limpio antes de meterlo en el estado
            val valorCalculado = currentState.contador - 1

            // Usamos el valor que acabamos de calcular para crear el mensaje
            val historialCalculado = listOf("Restando (-1) -> $valorCalculado") + currentState.historial

            // 2. ACTUALIZACIÓN DEL ESTADO
            // Ahora asignamos las variables ya calculadas
            currentState.copy(
                contador = valorCalculado,
                historial = historialCalculado
            )
        }
    }

    fun reiniciarContador() {
        _uiState.value = CounterUiState(
            contador = 0,
            historial = emptyList()
        )
    }
}