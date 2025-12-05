package com.example.pmdm01_14_miniproyecto2listatareasfiltro.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pmdm01_14_miniproyecto2listatareasfiltro.viewmodel.TaskViewModel
import com.example.pmdm01_14_miniproyecto2listatareasfiltro.viewmodel.TaskFilter
import com.example.pmdm01_14_miniproyecto2listatareasfiltro.ui.components.TaskItem

/*
 * Pantalla principal de la app.
 * Responsable de dibujar:
 *  - Cuadro de texto + botón para añadir tareas
 *  - Filtros (Todas / Activas / Completadas)
 *  - Lista de tareas filtradas
 *
 * Esta pantalla NO contiene lógica de negocio.
 * Todo el estado viene del ViewModel (TaskViewModel).
 */

@Composable
fun TaskScreen(viewModel: TaskViewModel = viewModel()) {

    // Texto del input para añadir tareas
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            // Añadimos margen para evitar que el top bar tape contenido
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
    ) {

        // ---------- Sección: Añadir nueva tarea ----------
        Row {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Nueva tarea") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.width(8.dp))
            // Botón para añadir una nueva tarea al ViewModel
            Button(onClick = {
                viewModel.addTask(inputText)
                inputText = "" // vaciar campo tras añadir
            }) {
                Text("Añadir")
            }
        }

        Spacer(Modifier.height(16.dp))

        // ---------- Sección: Filtros ----------
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            // Filtro "Todas"
            FilterChip(
                selected = viewModel.filter == TaskFilter.ALL,
                onClick = { viewModel.changeFilter(TaskFilter.ALL) },
                label = { Text("Todas") }
            )
            // Filtro "Activas" (no completadas)
            FilterChip(
                selected = viewModel.filter == TaskFilter.ACTIVE,
                onClick = { viewModel.changeFilter(TaskFilter.ACTIVE) },
                label = { Text("Activas") }
            )
            // Filtro "Completadas"
            FilterChip(
                selected = viewModel.filter == TaskFilter.COMPLETED,
                onClick = { viewModel.changeFilter(TaskFilter.COMPLETED) },
                label = { Text("Completadas") }
            )
        }

        Spacer(Modifier.height(16.dp))

        // ---------- Sección: Lista de tareas filtradas ----------
        /*
         * filteredTasks depende del filtro seleccionado:
         *    - ALL: todas las tareas
         *    - ACTIVE: solo no completadas
         *    - COMPLETED: solo completadas
         *
         * Como la lista es observable, Compose redibuja la UI automáticamente.
         */

        viewModel.filteredTasks.forEach { task ->
            TaskItem(
                task = task,
                // El callback delega la edición al ViewModel
                onChecked = { viewModel.toggleTask(task.id) }
            )
        }
    }
}