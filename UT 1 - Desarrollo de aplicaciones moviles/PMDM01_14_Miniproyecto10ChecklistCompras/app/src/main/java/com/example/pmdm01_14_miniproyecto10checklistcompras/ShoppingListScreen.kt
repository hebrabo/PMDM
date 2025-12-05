package com.example.pmdm01_14_miniproyecto10checklistcompras
/*
 * Esta pantalla es la UI completa del checklist.
 *
 * - Un TextField para escribir productos nuevos.
 * - Un botón "Añadir".
 * - Una lista donde cada elemento tiene:
 *      ✔ Checkbox para marcar como comprado
 *      🗑 Botón para eliminar
 *
 * Todo se conecta al ViewModel, que gestiona el estado real.
 */

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pmdm01_14_miniproyecto10checklistcompras.ViewModel.ShoppingListViewModel

@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel = viewModel()) {

    // Estado del campo de texto, solo para esta pantalla
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // ------------------------------
        // Entrada de texto + botón añadir
        // ------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Nuevo producto") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            Button(
                onClick = {
                    viewModel.addItem(text)
                    text = ""  // limpia el campo
                }
            ) {
                Text("Añadir")
            }
        }

        Divider()

        // ------------------------------
        // Lista de productos
        // ------------------------------
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(viewModel.items) { index, item ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // Checkbox + nombre
                    Row {
                        Checkbox(
                            checked = item.checked,
                            onCheckedChange = { viewModel.toggleItem(index) }
                        )
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    // Botón eliminar
                    Button(
                        onClick = { viewModel.removeItem(index) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}
