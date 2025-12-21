package com.example.pmdm01_14_miniproyecto1contadoravanzadoconhistorial.ui

// Esta clase es inmutable (val). Solo almacena datos.
data class CounterUiState(
    val contador: Int = 0,
    val historial: List<String> = emptyList() // Una lista vacía al principio
)