package com.example.pmdm01_14_miniproyecto8repositoriocitas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pmdm01_14_miniproyecto8repositoriocitas.QuoteViewModel

/**
 * Pantalla principal del repositorio de citas.
 * Muestra la cita actual y permite obtener una nueva cita aleatoria.
 */
@Composable
fun QuoteScreen(vm: QuoteViewModel = viewModel()) {

    // Contenedor principal centrado vertical y horizontalmente
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Título de la pantalla
        Text(
            text = "Cita del día",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Mostramos la cita seleccionada por el ViewModel
        // Importante: vm.currentQuote.value es un objeto Quote, usamos .text para obtener el String
        Text(
            text = vm.currentQuote.value.text,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón para seleccionar una nueva cita aleatoria
        Button(
            onClick = { vm.loadRandomQuote() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nueva cita")
        }
    }
}
