package com.example.pmdm01_14_miniproyecto2listatareasfiltro.viewmodel

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/*
 * Conjunto de tests para validar el TaskViewModel.
 * Se comprueba que:
 *   - Se puedan añadir tareas
 *   - Se puedan marcar como completadas o activas
 *   - Los filtros funcionen correctamente
 *   - No se añadan tareas vacías
 */

class TaskViewModelTest {

    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        // Antes de cada test se crea un nuevo ViewModel
        viewModel = TaskViewModel()
    }

    @Test
    fun addTask_increasesTaskList() {
        viewModel.addTask("Tarea 1")
        viewModel.addTask("Tarea 2")

        assertEquals(2, viewModel.tasks.size)
        assertEquals("Tarea 1", viewModel.tasks[0].text)
        assertEquals("Tarea 2", viewModel.tasks[1].text)
    }

    @Test
    fun toggleTask_changesIsDone() {
        viewModel.addTask("Tarea 1")
        val id = viewModel.tasks[0].id

        // Antes de alternar, debe estar sin completar
        assertFalse(viewModel.tasks[0].isDone)

        // Primera vez → se marca como completada
        viewModel.toggleTask(id)
        assertTrue(viewModel.tasks[0].isDone)

        // Segunda vez → vuelve a estado activo
        viewModel.toggleTask(id)
        assertFalse(viewModel.tasks[0].isDone)
    }

    @Test
    fun changeFilter_updatesFilteredTasks() {
        viewModel.addTask("Tarea 1") // activa
        viewModel.addTask("Tarea 2") // activa
        viewModel.toggleTask(viewModel.tasks[1].id) // segunda → completada

        // ALL debe mostrar ambas
        viewModel.changeFilter(TaskFilter.ALL)
        assertEquals(2, viewModel.filteredTasks.size)

        // ACTIVE solo la primera
        viewModel.changeFilter(TaskFilter.ACTIVE)
        assertEquals(1, viewModel.filteredTasks.size)
        assertEquals("Tarea 1", viewModel.filteredTasks[0].text)

        // COMPLETED solo la segunda
        viewModel.changeFilter(TaskFilter.COMPLETED)
        assertEquals(1, viewModel.filteredTasks.size)
        assertEquals("Tarea 2", viewModel.filteredTasks[0].text)
    }

    @Test
    fun addTask_withBlankText_doesNothing() {
        viewModel.addTask("   ") // texto vacío
        assertTrue(viewModel.tasks.isEmpty())
    }

}
