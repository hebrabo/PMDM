package com.example.pmdm01_14_miniproyecto7gestornotascortas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.pmdm01_14_miniproyecto7gestornotascortas.ui.NoteScreen

/*
 * Mini Proyecto 7: Gestor de notas cortas
 *
 * Esta actividad únicamente carga la interfaz con Jetpack Compose.
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                NoteScreen()   // Pantalla de la app
            }
        }
    }
}