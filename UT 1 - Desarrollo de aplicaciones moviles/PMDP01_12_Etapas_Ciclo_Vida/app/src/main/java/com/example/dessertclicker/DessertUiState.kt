package com.example.dessertclicker

import androidx.annotation.DrawableRes

data class DessertUiState(
    val dessertsSold: Int = 0, // Cantidad de postres vendidos
    val revenue: Int = 0, // Ingreso total
    val currentDessertIndex: Int = 0, // Índice del postre actual
    val currentDessertPrice: Int = 0, // Precio del postre actual
    @DrawableRes val currentDessertImageId: Int = 0 // Imagen del postre actual
)
