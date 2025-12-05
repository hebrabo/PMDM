package com.example.pmdm01_14_miniproyecto7gestornotascortas

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.pmdm01_14_miniproyecto7gestornotascortas.model.Note

/*
 * ViewModel que guarda el estado de la lista de notas.
 * Usamos mutableStateListOf para que la UI se actualice automáticamente.
 */

class NoteViewModel : ViewModel() {

    // Lista observable de notas
    private val _notes = mutableStateListOf<Note>()
    val notes: List<Note> = _notes

    // Añadir una nueva nota si no está vacía
    fun addNote(text: String) {
        if (text.isBlank()) return
        _notes.add(Note(text))
    }
}
