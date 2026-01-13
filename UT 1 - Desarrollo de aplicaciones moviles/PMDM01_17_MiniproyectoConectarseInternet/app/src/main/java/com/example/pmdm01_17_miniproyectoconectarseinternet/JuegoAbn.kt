package com.example.pmdm01_17_miniproyectoconectarseinternet

data class JuegoAbn(
    val id: Int,
    val titulo: String,
    val categoria: String,
    val etiqueta: String,
    val descripcion: String,
    val imagenUrl: String,
    val juegoUrl: String? = "" // Usamos ? y = "" para máxima seguridad
)
