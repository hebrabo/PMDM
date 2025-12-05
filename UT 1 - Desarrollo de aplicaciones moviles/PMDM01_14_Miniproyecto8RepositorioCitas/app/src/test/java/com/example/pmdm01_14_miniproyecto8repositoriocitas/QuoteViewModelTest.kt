package com.example.pmdm01_14_miniproyecto8repositoriocitas

import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/*
 * Tests del ViewModel de citas.
 * Verificamos:
 *  1) Que siempre haya una cita inicial válida.
 *  2) Que al pedir una nueva, la cita cambie.
 */

class QuoteViewModelTest {

    @Test
    fun initialQuote_isValid() {
        val vm = QuoteViewModel()

        // Comprobamos que la cita inicial tiene texto no vacío
        assertTrue(vm.currentQuote.value.text.isNotBlank())
    }

    @Test
    fun loadRandomQuote_changesQuote() {
        val vm = QuoteViewModel()
        val oldQuote = vm.currentQuote.value

        vm.loadRandomQuote()
        val newQuote = vm.currentQuote.value

        // Es posible (aunque improbable) que salga la misma,
        // pero para este ejercicio asumimos que debe cambiar.
        assertNotEquals(oldQuote, newQuote)
    }
}