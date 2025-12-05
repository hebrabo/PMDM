package com.example.pmdm01_14_miniproyecto5registrogastos.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pmdm01_14_miniproyecto5registrogastos.viewmodel.ExpenseViewModel

/*
 * Pantalla principal donde el usuario:
 *   ✔ introduce nombre y cantidad del gasto
 *   ✔ pulsa "Agregar gasto"
 *   ✔ ve la lista de gastos añadidos
 *   ✔ ve el total calculado automáticamente
 */

@Composable
fun ExpenseScreen(viewModel: ExpenseViewModel = viewModel()) {

    // Estados locales para los campos de texto
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Registro de gastos", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        // Campo para el nombre
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre del gasto") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("nameField") // útil para test UI
        )

        Spacer(Modifier.height(8.dp))

        // Campo para la cantidad
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Cantidad") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("amountField")
        )

        Spacer(Modifier.height(16.dp))

        // Botón para agregar un gasto
        Button(
            onClick = {
                // Convertir cantidad → Double
                amount.toDoubleOrNull()?.let { value ->
                    viewModel.addExpense(name, value)
                    name = ""      // limpiar campos
                    amount = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("addButton")
        ) {
            Text("Agregar gasto")
        }

        Spacer(Modifier.height(24.dp))

        // Lista de gastos
        LazyColumn {
            items(viewModel.expenses) { expense ->
                Text("${expense.name}: ${expense.amount}€")
            }
        }

        Spacer(Modifier.height(24.dp))

        // Total calculado por el ViewModel
        Text(
            "Total: ${viewModel.total.value}€",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
