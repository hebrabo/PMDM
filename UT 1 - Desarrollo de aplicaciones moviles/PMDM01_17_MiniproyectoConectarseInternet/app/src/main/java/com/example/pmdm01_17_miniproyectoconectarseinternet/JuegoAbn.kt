package com.example.pmdm01_17_miniproyectoconectarseinternet

data class JuegoAbn(
    val id: Int,
    val titulo: String,
    val categoria: String, // "contar", "numeracion" o "operaciones"
    val etiqueta: String,  // "cuantificadores", "patrones", "recta numérica"...
    val descripcion: String,
    val imagenUrl: String  // La URL de la imagen que descargaremos con Coil
)
