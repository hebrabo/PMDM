package com.example.pmdm01_14_miniproyecto2listatareasfiltro.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.pmdm01_14_miniproyecto2listatareasfiltro.model.Task

// -----------------------------------------------------------------------------
// Enum que define los posibles filtros que puede elegir el usuario:
//  ALL → muestra todas las tareas
//  ACTIVE → muestra solo tareas sin completar
//  COMPLETED → muestra solo tareas completadas
// -----------------------------------------------------------------------------

enum class TaskFilter { ALL, ACTIVE, COMPLETED }

// -----------------------------------------------------------------------------
// ViewModel que contiene toda la lógica de la aplicación:
//  - lista de tareas
//  - filtro actual
//  - funciones para modificar el estado
//  - lógica para obtener las tareas filtradas
//
// La UI NO debe contener lógica; solo llama a estas funciones.
// -----------------------------------------------------------------------------

class TaskViewModel : ViewModel() {

    // Contador para asignar IDs únicos a las tareas nuevas.
    private var nextId = 0

    // Lista de tareas guardada como estado observable.
    // mutableStateOf hace que Compose redibuje la interfaz cuando cambia.
    var tasks by mutableStateOf(listOf<Task>())
        private set

    // Filtro actual (ALL, ACTIVE o COMPLETED), también observable.
    var filter by mutableStateOf(TaskFilter.ALL)
        private set

    // -------------------------------------------------------------------------
    // Lista calculada: depende del filtro.
    // No se almacena directamente, sino que se calcula cada vez que se consulta.
    // Esto mantiene siempre la UI sincronizada con los datos.
    // -------------------------------------------------------------------------
    val filteredTasks: List<Task>
        get() = when (filter) {
            TaskFilter.ALL -> tasks
            TaskFilter.ACTIVE -> tasks.filter { !it.isDone }
            TaskFilter.COMPLETED -> tasks.filter { it.isDone }
        }

    // -------------------------------------------------------------------------
    // Añade una tarea nueva siempre que el texto no esté vacío.
    // Se usa `+` porque las listas en Kotlin son inmutables → genera una nueva.
    // Esto es bueno para Compose: evita inconsistencias en el estado.
    // -------------------------------------------------------------------------
    fun addTask(text: String) {
        if (text.isNotBlank()) {
            tasks = tasks + Task(nextId++, text)
        }
    }

    // -------------------------------------------------------------------------
    // Alterna el estado de una tarea (completada ↔ activa).
    // .map() recorre toda la lista, sustituyendo solo la tarea cuyo id coincide.
    // `copy()` se usa porque la clase Task es inmutable (data class).
    // -------------------------------------------------------------------------
    fun toggleTask(id: Int) {
        tasks = tasks.map { t ->
            if (t.id == id) t.copy(isDone = !t.isDone) else t
        }
    }

    // -------------------------------------------------------------------------
    // Cambia el filtro actual.
    // Como es un mutableStateOf, la UI se redibuja automáticamente.
    // -------------------------------------------------------------------------
    fun changeFilter(newFilter: TaskFilter) {
        filter = newFilter
    }
}