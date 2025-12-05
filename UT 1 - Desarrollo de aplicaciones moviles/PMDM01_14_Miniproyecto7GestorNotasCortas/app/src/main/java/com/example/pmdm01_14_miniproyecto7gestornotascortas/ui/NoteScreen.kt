package com.example.pmdm01_14_miniproyecto7gestornotascortas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pmdm01_14_miniproyecto7gestornotascortas.NoteViewModel

/*
 * Pantalla principal:
 *  ✔ Campo para escribir una nota
 *  ✔ Botón "Añadir"
 *  ✔ Lista de notas debajo
 */

@Composable
fun NoteScreen(viewModel: NoteViewModel = viewModel()) {

    var noteInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            "Gestor de notas",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = noteInput,
            onValueChange = { noteInput = it },
            label = { Text("Escribe una nota...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.addNote(noteInput)
                noteInput = ""     // Limpia el campo
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir nota")
        }

        Spacer(Modifier.height(24.dp))

        // Lista de notas
        LazyColumn {
            items(viewModel.notes) { note ->
                Text(
                    text = "• ${note.text}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}