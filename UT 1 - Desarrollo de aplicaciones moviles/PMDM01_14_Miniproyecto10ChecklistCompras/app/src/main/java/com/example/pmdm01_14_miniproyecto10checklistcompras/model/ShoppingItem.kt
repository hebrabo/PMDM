package com.example.pmdm01_14_miniproyecto10checklistcompras.model

/*
 * Esta data class representa un elemento de la lista de la compra.
 * - name: nombre del producto.
 * - checked: indica si está “comprado”.
 */

data class ShoppingItem(
    val name: String,
    val checked: Boolean = false
)