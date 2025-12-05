package com.example.pmdm01_14_miniproyecto2listatareasfiltro.ui.components

/*
 * Composable que dibuja una sola tarea.
 * Muestra el texto y una casilla de verificación para marcarla como completada.
 * No modifica el estado directamente: se apoya en un callback (onChecked)
 * perteneciente al ViewModel para aplicar la lógica.
 */

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pmdm01_14_miniproyecto2listatareasfiltro.model.Task

@Composable
fun TaskItem(task: Task, onChecked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Texto de la tarea
        Text(task.text)
        // Checkbox para marcarla como completada
        Checkbox(
            checked = task.isDone,
            onCheckedChange = { onChecked() } // Notificamos al ViewModel para actualizar el estado
        )
    }
}