package com.example.pmdm01_14_miniproyecto2listatareasfiltro

/*
 * Lista de tareas con filtro
 * Implementa una lista de tareas donde el usuario pueda añadir, marcar como completadas y filtrar por “todas / activas / completadas”.
 * La lógica de tareas estará en un ViewModel.
 */

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pmdm01_14_miniproyecto2listatareasfiltro.ui.TaskScreen
import com.example.pmdm01_14_miniproyecto2listatareasfiltro.ui.theme.PMDM01_14_Miniproyecto2ListaTareasFiltroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PMDM01_14_Miniproyecto2ListaTareasFiltroTheme {
                TaskScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PMDM01_14_Miniproyecto2ListaTareasFiltroTheme {
        Greeting("Android")
    }
}