package com.example.pmdm01_14_miniproyecto6cronometro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.pmdm01_14_miniproyecto6cronometro.ui.TimerScreen

/*
 * Mini proyecto 6:
 * Temporizador simple (cronómetro)
 *
 * Esta actividad solo inicia la UI usando Jetpack Compose.
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                TimerScreen()  // Cargamos la pantalla del cronómetro
            }
        }
    }
}