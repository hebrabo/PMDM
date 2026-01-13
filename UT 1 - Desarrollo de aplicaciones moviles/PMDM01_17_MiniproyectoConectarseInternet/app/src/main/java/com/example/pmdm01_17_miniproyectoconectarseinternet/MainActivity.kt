package com.example.pmdm01_17_miniproyectoconectarseinternet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // Obtenemos el ViewModel
                val viewModel: AbnViewModel = viewModel()

                // Llamamos a la pantalla pasando el estado actual
                AbnLibraryScreen(
                    uiState = viewModel.abnUiState,
                    onRetry = { viewModel.getJuegosAbn() }
                )
            }
        }
    }
}