package com.example.pmdm01_14_miniproyyecto1contadorhistorial.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pmdm01_14_miniproyecto1contadoravanzadoconhistorial.ui.CounterUiState


@Composable
fun HistoryList(history: List<CounterUiState>) {
    // LazyColumn permite listas largas con scroll eficiente
    LazyColumn(
        contentPadding = PaddingValues(top = 8.dp)
    ) {
        // items recorre la lista de operaciones
        items(history) { op ->
            // Un bloque por cada operación realizada
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                // Un bloque por cada operación realizada
                Text(text = op.description)
                // Valor resultante después de realizar la operación
                Text(
                    text = "Valor: ${op.newValue}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
