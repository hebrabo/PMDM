package com.example.pmdm01_14_miniproyecto6cronometro.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pmdm01_14_miniproyecto6cronometro.model.TimerViewModel

/*
 * Pantalla del temporizador.
 * Muestra:
 *   ✔ el tiempo actual formateado
 *   ✔ botones de iniciar, pausar y reiniciar
 */

@Composable
fun TimerScreen(vm: TimerViewModel = viewModel()) {

    // Observar el tiempo del ViewModel
    val time by vm.time.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Tiempo grande centrado
        Text(
            text = vm.formatTime(time),
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botones de control del temporizador
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

            Button(onClick = { vm.start() }) {
                Text("Iniciar")
            }

            Button(onClick = { vm.pause() }) {
                Text("Pausar")
            }

            Button(onClick = { vm.reset() }) {
                Text("Reiniciar")
            }
        }
    }
}