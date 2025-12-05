package com.example.pmdm01_14_miniproyecto10checklistcompras

/*
 * Tests unitarios del ViewModel de la lista de la compra.
 * Aquí comprobamos:
 *   ✔ Que añadir un ítem funciona
 *   ✔ Que marcar/desmarcar un ítem funciona
 *   ✔ Que eliminar un ítem funciona
 *
 * NO probamos la UI, solo la lógica del ViewModel.
 */

import com.example.pmdm01_14_miniproyecto10checklistcompras.ViewModel.ShoppingListViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ShoppingListViewModelTest {

    // ViewModel a probar
    private lateinit var viewModel: ShoppingListViewModel

    @Before
    fun setup() {
        // Se crea uno nuevo antes de cada test
        viewModel = ShoppingListViewModel()
    }

    // ------------------------------------------------------
    // TEST 1: Comprobar que añadir un producto funciona
    // ------------------------------------------------------
    @Test
    fun `addItem inserts a new item into the list`() {
        viewModel.addItem("Manzanas")

        // Debe haber un ítem en la lista
        assertEquals(1, viewModel.items.size)

        // Y su nombre debe coincidir
        assertEquals("Manzanas", viewModel.items[0].name)
    }

    // ------------------------------------------------------
    // TEST 2: No debe añadir items vacíos
    // ------------------------------------------------------
    @Test
    fun `addItem should not add empty names`() {
        viewModel.addItem("   ")   // Cadena vacía o con espacios

        assertEquals(
            0,
            viewModel.items.size
        )
    }

    // ------------------------------------------------------
    // TEST 3: Marcar/desmarcar un producto (toggle)
    // ------------------------------------------------------
    @Test
    fun `toggleItem switches the checked state`() {
        viewModel.addItem("Pan")

        // Estado inicial = false
        assertFalse(viewModel.items[0].checked)

        viewModel.toggleItem(0)

        // Debe pasar a true
        assertTrue(viewModel.items[0].checked)

        viewModel.toggleItem(0)

        // Y volver a false
        assertFalse(viewModel.items[0].checked)
    }

    // ------------------------------------------------------
    // TEST 4: Eliminar un producto por índice
    // ------------------------------------------------------
    @Test
    fun `removeItem deletes the correct element`() {
        viewModel.addItem("Leche")
        viewModel.addItem("Huevos")

        // Lista inicial: [Leche, Huevos]
        viewModel.removeItem(0)

        // Lista debe quedar: [Huevos]
        assertEquals(1, viewModel.items.size)
        assertEquals("Huevos", viewModel.items[0].name)
    }

    // ------------------------------------------------------
    // TEST 5: Eliminar en orden sigue funcionando
    // ------------------------------------------------------
    @Test
    fun `removeItem works even when multiple items exist`() {
        viewModel.addItem("Arroz")
        viewModel.addItem("Pasta")
        viewModel.addItem("Tomates")

        // Eliminamos el del medio
        viewModel.removeItem(1)

        val expectedNames = listOf("Arroz", "Tomates")
        val actualNames = viewModel.items.map { it.name }

        assertEquals(expectedNames, actualNames)
    }
}