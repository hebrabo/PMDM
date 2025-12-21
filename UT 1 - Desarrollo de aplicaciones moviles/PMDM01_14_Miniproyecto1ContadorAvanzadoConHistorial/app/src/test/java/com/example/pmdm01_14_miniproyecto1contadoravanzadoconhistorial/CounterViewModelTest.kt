package com.example.pmdm01_14_miniproyecto1contadoravanzadoconhistorial

import com.example.pmdm01_14_miniproyecto1contadoravanzadoconhistorial.ui.CounterViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.DefaultAsserter.assertTrue

class CounterViewModelTest {

    private lateinit var viewModel: CounterViewModel

    @Before
    fun setup() {
        // Antes de cada test, creamos un ViewModel nuevo y limpio
        viewModel = CounterViewModel()
    }

    @Test
    fun `al iniciar, el contador debe ser 0 y el historial no vacio`() {
        // Verificamos el estado inicial
        val estado = viewModel.uiState.value

        assertEquals("El contador debería empezar en 0", 0, estado.contador)
        // Como en el init llamamos a reiniciar(), debería haber al menos un mensaje
        assertTrue("El historial no debería estar vacío", estado.historial.isNotEmpty())
    }

    @Test
    fun `sumar aumenta el contador y añade al historial`() {
        // 1. Ejecutamos la acción
        viewModel.sumar()

        // 2. Verificamos el resultado
        val estado = viewModel.uiState.value

        assertEquals("El contador debería ser 1", 1, estado.contador)
        assertTrue("El historial debe crecer", estado.historial.size > 1)
        assertTrue("El último mensaje debe indicar suma", estado.historial[0].contains("Sumando"))
    }

    @Test
    fun `restar disminuye el contador y añade al historial`() {
        // 1. Ejecutamos la acción
        viewModel.restar()

        // 2. Verificamos
        val estado = viewModel.uiState.value

        assertEquals("El contador debería ser -1", -1, estado.contador)
        assertTrue("El último mensaje debe indicar resta", estado.historial[0].contains("Restando"))
    }

    @Test
    fun `flujo completo sumar y reiniciar`() {
        // 1. Hacemos varias operaciones
        viewModel.sumar() // 1
        viewModel.sumar() // 2
        viewModel.restar() // 1

        // Comprobamos estado intermedio
        assertEquals(1, viewModel.uiState.value.contador)

        // 2. Reiniciamos
        viewModel.reiniciarContador()

        // 3. Verificamos el reinicio
        val estadoFinal = viewModel.uiState.value
        assertEquals("Debe volver a 0", 0, estadoFinal.contador)
        assertTrue("El mensaje de reinicio debe estar primero", estadoFinal.historial[0].contains("Reiniciado"))
    }
}