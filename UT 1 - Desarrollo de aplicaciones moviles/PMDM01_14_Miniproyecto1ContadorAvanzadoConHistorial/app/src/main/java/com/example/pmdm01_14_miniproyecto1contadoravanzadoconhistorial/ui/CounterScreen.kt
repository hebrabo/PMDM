package com.example.pmdm01_14_miniproyecto1contadoravanzadoconhistorial.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// -------------------------------------------------------------------------
// 1. STATEFUL - Conecta ViewModel con la Pantalla
// -------------------------------------------------------------------------
@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
    // 1. Leemos el estado del ViewModel (el número y la lista)
    val state by viewModel.uiState.collectAsState()

    // 2. Llamamos al diseño (Layout) pasándole los datos y las funciones
    CounterLayout(
        contador = state.contador,
        historial = state.historial,
        onSumar = { viewModel.sumar() },
        onRestar = { viewModel.restar() },
        onReiniciar = { viewModel.reiniciarContador() }
    )
}

// -------------------------------------------------------------------------
// 2. STATELESS - Solo pinta lo que le dicen
// -------------------------------------------------------------------------
@Composable
fun CounterLayout(
    contador: Int,
    historial: List<String>,
    onSumar: () -> Unit,
    onRestar: () -> Unit,
    onReiniciar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TÍTULO
        Text(
            text = "Contador Avanzado",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // NÚMERO GRANDE
        Text(
            text = "$contador",
            fontSize = 90.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(32.dp))

        // BOTONERA
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Botón Restar
            Button(onClick = onRestar) {
                Text("-1", fontSize = 20.sp)
            }

            // Botón Reset (Rojo)
            Button(
                onClick = onReiniciar,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reiniciar")
            }

            // Botón Sumar
            Button(onClick = onSumar) {
                Text("+1", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        Text("Historial de operaciones", style = MaterialTheme.typography.labelLarge)

        // LISTA DE HISTORIAL
        // Usamos LazyColumn para que sea eficiente si la lista es muy larga
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Ocupa todo el espacio restante hacia abajo
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(historial) { item ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
