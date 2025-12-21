package com.example.pmdm01_14_miniproyecto1contadorhistorial.viewmodel

import com.example.pmdm01_14_miniproyecto1contadoravanzadoconhistorial.ui.CounterViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CounterViewModelTest {

    private lateinit var viewModel: CounterViewModel

    @Before
    fun setup() {
        // Se crea un ViewModel nuevo antes de cada test
        viewModel = CounterViewModel()
    }

    @Test
    fun increment_increasesCount() {
        viewModel.increment()
        // Comprobamos que el contador subió
        assertEquals(1, viewModel.count.value)
        // El historial debe tener una operación registrada
        assertEquals(1, viewModel.history.size)
        assertEquals("Sumar 1", viewModel.history[0].description)
    }

    @Test
    fun decrement_decreasesCount() {
        viewModel.decrement()
        assertEquals(-1, viewModel.count.value)
        assertEquals(1, viewModel.history.size)
        assertEquals("Restar 1", viewModel.history[0].description)
    }

    @Test
    fun reset_setsCountToZero() {
        viewModel.increment()
        viewModel.increment()
        viewModel.reset()
        // Tras el reset, el contador debe ser 0
        assertEquals(0, viewModel.count.value)
        // El historial debe contener las 3 operaciones
        assertEquals(3, viewModel.history.size)
        assertEquals("Reiniciar contador", viewModel.history.last().description)
    }

    @Test
    fun multipleOperations_historyIsCorrect() {
        viewModel.increment()
        viewModel.decrement()
        viewModel.increment()
        viewModel.reset()

        val expectedDescriptions = listOf(
            "Sumar 1",
            "Restar 1",
            "Sumar 1",
            "Reiniciar contador"
        )

        // Comparamos que el historial coincide con lo esperado
        expectedDescriptions.forEachIndexed { index, description ->
            assertEquals(description, viewModel.history[index].description)
        }
    }
}
