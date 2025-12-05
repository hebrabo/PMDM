package com.example.pmdm01_14_miniproyecto7gestornotascortas

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/*
 * Tests del ViewModel de notas.
 *
 * Estos tests NO usan Android: funcionan como Unit Tests normales,
 * ejecutándose en la JVM.
 *
 * El objetivo es comprobar que la lógica del ViewModel funciona:
 *  - Se añaden notas correctamente.
 *  - No se añaden notas vacías.
 */

class NoteViewModelTest {

    @Test
    fun addNote_addsValidNote() {
        // Creamos un nuevo ViewModel para empezar limpio
        val vm = NoteViewModel()

        // Añadimos una nota válida
        vm.addNote("Hola mundo")

        // Comprobamos que ahora la lista tiene exactamente 1 elemento
        assertEquals(1, vm.notes.size)

        // Comprobamos que el contenido de la nota añadida es el esperado
        assertEquals("Hola mundo", vm.notes[0].text)
    }

    @Test
    fun addNote_doesNotAddEmptyNote() {
        // Nuevo ViewModel para el test
        val vm = NoteViewModel()

        // Intentamos añadir notas vacías o con espacios
        vm.addNote("")
        vm.addNote("   ")

        // Como son notas inválidas, la lista debe seguir vacía
        assertTrue(vm.notes.isEmpty())
    }
}