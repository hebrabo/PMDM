/*
 * Copyright (C) 2023 The Android Open Source Project
 * ... (Licencia omitida para brevedad)
 */
package com.example.racetracker.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

/**
 * Esta clase representa el ESTADO de un participante.
 * No es un Composable, es una clase de Kotlin pura que guarda datos.
 */
class RaceParticipant(
    val name: String,
    val maxProgress: Int = 100,
    val progressDelayMillis: Long = 500L,
    private val progressIncrement: Int = 1,
    private val initialProgress: Int = 0
) {
    init {
        // Validaciones básicas al crear el objeto
        require(maxProgress > 0) { "maxProgress=$maxProgress; must be > 0" }
        require(progressIncrement > 0) { "progressIncrement=$progressIncrement; must be > 0" }
    }

    /**
     * VARIABLE DE ESTADO:
     * Usamos 'mutableStateOf' para que Jetpack Compose pueda "observar" esta variable.
     * Cuando 'currentProgress' cambie, cualquier UI que lea este valor se volverá a dibujar (Recomposición).
     */
    var currentProgress by mutableStateOf(initialProgress)
        private set // El setter es privado para que solo esta clase pueda modificar el progreso.

    /**
     * LÓGICA DE CORRUTINA:
     * La palabra clave 'suspend' indica que esta función puede pausarse y reanudarse.
     * Es ideal para operaciones que toman tiempo (como una carrera) sin bloquear la pantalla (hilo principal).
     */
    suspend fun run() {
        while (currentProgress < maxProgress) {
            // delay() es una función de suspensión. Pausa la ejecución por X tiempo
            // pero NO congela la app.
            delay(progressDelayMillis)
            currentProgress += progressIncrement
        }
    }

    /**
     * Resetea el estado a 0. Al modificar 'currentProgress',
     * la UI se actualizará automáticamente.
     */
    fun reset() {
        currentProgress = 0
    }
}

/**
 * PROPIEDAD DE EXTENSIÓN:
 * La barra de progreso de Material Design espera un valor flotante entre 0.0 y 1.0.
 * Esta lógica convierte nuestro entero (ej. 50 de 100) a un factor (0.5).
 */
val RaceParticipant.progressFactor: Float
    get() = currentProgress / maxProgress.toFloat()