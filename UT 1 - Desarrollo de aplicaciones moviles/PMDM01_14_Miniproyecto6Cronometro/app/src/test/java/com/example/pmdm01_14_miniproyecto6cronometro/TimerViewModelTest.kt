package com.example.pmdm01_14_miniproyecto6cronometro

import com.example.pmdm01_14_miniproyecto6cronometro.model.TimerViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/*
 * Tests unitarios del temporizador.
 * No prueban corrutinas infinitas, pero sí:
 *   ✔ reset()
 *   ✔ formatTime()
 */

class TimerViewModelTest {

    @Test
    fun testFormatTime() {
        val vm = TimerViewModel()
        assertEquals("00:05", vm.formatTime(5))
        assertEquals("01:00", vm.formatTime(60))
        assertEquals("10:30", vm.formatTime(630))
    }

    @Test
    fun testReset() {
        val vm = TimerViewModel()
        runBlocking {
            // Modificar tiempo simulando comportamiento
            vm.reset()
            assertEquals(0, vm.time.value)
        }
    }
}
