package com.example.pmdm01_14_miniproyecto1contadoravanzadoconhistorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.pmdm01_14_miniproyecto1contadoravanzadoconhistorial.ui.theme.PMDM01_14_Miniproyecto1ContadorAvanzadoConHistorialTheme
import com.example.pmdm01_14_miniproyecto1contadoravanzadoconhistorial.ui.CounterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita el modo pantalla completa

        setContent {
            PMDM01_14_Miniproyecto1ContadorAvanzadoConHistorialTheme {
                // 1. SURFACE: Pone el color de fondo (blanco/negro) correcto
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 2. COUNTER SCREEN con padding seguro
                    // Usamos safeDrawingPadding() para que el contenido no se tape con la barra de estado (la hora/batería)
                    // ya que enableEdgeToEdge() está activado.
                    Surface(modifier = Modifier.safeDrawingPadding()) {
                        CounterScreen()
                    }
                }
            }
        }
    }
}
