package com.example.pmdm01_14_miniproyecto6cronometro.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/*
 * ViewModel que contiene TODA la lógica del cronómetro.
 * Usa un StateFlow para emitir el tiempo actual.
 */

class TimerViewModel : ViewModel() {

    // Tiempo en segundos
    private val _time = MutableStateFlow(0)
    val time: StateFlow<Int> = _time

    // Corrutina que controla el temporizador
    private var timerJob: Job? = null

    // Iniciar el temporizador
    fun start() {
        // Evitar crear múltiples temporizadores
        if (timerJob != null) return

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)        // 1 segundo
                _time.value += 1   // Aumentar el tiempo
            }
        }
    }

    // Pausar (cancelar la corrutina)
    fun pause() {
        timerJob?.cancel()
        timerJob = null
    }

    // Reiniciar el temporizador
    fun reset() {
        pause()
        _time.value = 0
    }

    // Función auxiliar para mostrar el tiempo en formato mm:ss
    fun formatTime(seconds: Int): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return "%02d:%02d".format(mins, secs)
    }
}