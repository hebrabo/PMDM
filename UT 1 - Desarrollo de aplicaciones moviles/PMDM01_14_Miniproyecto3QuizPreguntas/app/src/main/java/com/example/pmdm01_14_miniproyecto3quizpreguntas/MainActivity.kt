package com.example.pmdm01_14_miniproyecto3quizpreguntas

/*
* Quiz de preguntas
Haz un pequeño cuestionario de varias preguntas de opción múltiple.
El ViewModel debe controlar la pregunta actual, las respuestas y la puntuación final.
 */
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.material3.Surface
import com.example.pmdm01_14_miniproyecto3quizpreguntas.ui.QuizScreen

// -----------------------------------------------------------------------------
// Actividad principal de la app. Solo sirve para arrancar la UI.
// Toda la lógica del quiz está en el ViewModel (arquitectura MVVM).
// -----------------------------------------------------------------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    QuizScreen()
                }
            }
        }
    }
}

