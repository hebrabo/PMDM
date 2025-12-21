package com.example.pmdm01_14_miniproyyecto1contadorhistorial.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pmdm01_14_miniproyecto1contadoravanzadoconhistorial.ui.CounterViewModel


@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {

    // Pantalla principal organizada en una columna vertical
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Mostramos el valor actual del contador
        Text(
            text = "Contador: ${viewModel.count.value}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        // Fila con los tres botones
        Row {
            // Botón para sumar 1
            Button(onClick = { viewModel.increment() }, modifier = Modifier.weight(1f)) {
                Text("Sumar")
            }
            Spacer(Modifier.width(8.dp))
            // Botón para restar 1
            Button(onClick = { viewModel.decrement() }, modifier = Modifier.weight(1f)) {
                Text("Restar")
            }
            Spacer(Modifier.width(8.dp))
            // Botón para reiniciar a 0
            Button(
                onClick = { viewModel.reset() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Text("Reiniciar")
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            // Título del historial
            "Historial:",
            style = MaterialTheme.typography.titleMedium
        )

        // Lista del historial
        HistoryList(history = viewModel.history)
    }
}
