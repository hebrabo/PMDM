package com.example.pmdm01_14_miniproyecto10checklistcompras.ViewModel

/*
 * El ViewModel almacena el estado de la lista y expone funciones
 * para modificarla.
 *
 * Usamos MutableStateListOf porque Compose observará automáticamente
 * los cambios y actualizará la UI.
 */

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.pmdm01_14_miniproyecto10checklistcompras.model.ShoppingItem

class ShoppingListViewModel : ViewModel() {

    // Lista observable de items
    var items = mutableStateListOf<ShoppingItem>()
        private set

    // Añade un nuevo producto
    fun addItem(name: String) {
        if (name.isNotBlank()) {
            items.add(ShoppingItem(name = name))
        }
    }

    // Marca o desmarca un producto
    fun toggleItem(index: Int) {
        val item = items[index]
        items[index] = item.copy(checked = !item.checked)
    }

    // Elimina un producto
    fun removeItem(index: Int) {
        items.removeAt(index)
    }
}