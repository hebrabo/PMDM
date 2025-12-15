/*
 * Copyright (C) 2023 The Android Open Source Project
 * ... (Licencia omitida para brevedad)
 */
package com.example.racetracker.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException

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
        try {
            // Bucle de la "carrera". Se ejecuta mientras no lleguemos a la meta.
            while (currentProgress < maxProgress) {

                /**
                 * PUNTO DE SUSPENSIÓN Y CANCELACIÓN COOPERATIVA:
                 * 'delay' no solo pausa la ejecución, también comprueba si la corrutina
                 * sigue activa (isActive).
                 * * Si el usuario pulsa "Reset" o gira la pantalla y la corrutina se cancela,
                 * la función 'delay' detecta esa cancelación e interrumpe la ejecución
                 * lanzando inmediatamente una 'CancellationException'.
                 */
                delay(progressDelayMillis)

                // Si no se ha cancelado, actualizamos el estado (la barra avanza).
                currentProgress += progressIncrement
            }
        } catch (e: CancellationException) {
            // Entramos aquí si la corrutina fue cancelada mientras estaba en el 'delay'.
            Log.e("RaceParticipant", "$name: ${e.message}")

            /**
             * ¡REGLA DE ORO DE LAS CORRUTINAS!
             * Siempre debemos volver a lanzar (re-throw) la CancellationException.
             * * ¿Por qué?
             * Porque la cancelación es un mecanismo especial de control de flujo.
             * Si te "comes" la excepción (la capturas y no haces nada), la corrutina padre
             * (el 'LaunchedEffect' o el 'scope') pensará que la tarea terminó con éxito
             * en lugar de saber que fue interrumpida.
             */
            throw e
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