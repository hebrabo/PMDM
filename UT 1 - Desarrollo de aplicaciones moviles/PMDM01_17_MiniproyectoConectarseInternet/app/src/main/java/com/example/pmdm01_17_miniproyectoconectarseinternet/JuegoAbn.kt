package com.example.pmdm01_17_miniproyectoconectarseinternet

/**
 * DATA CLASS: El Modelo de Datos.
 * Esta clase es el "molde" que usa Retrofit y Gson para convertir el texto JSON
 * que viene de Internet en objetos que Kotlin puede entender.
 */
data class JuegoAbn(
    // ID único para identificar cada actividad dentro de los juegos de la app.
    val id: Int,

    // Información textual que se mostrará en las tarjetas de la interfaz.
    val titulo: String,
    val categoria: String,
    val etiqueta: String,
    val descripcion: String,

    /**
     * Cargar y mostrar imágenes de Internet.
     * imagenUrl: Contiene la dirección web de la imagen.
     * Esta URL es procesada por la librería Coil para descargar la imagen
     * de forma asíncrona sin bloquear el hilo principal.
     */
    val imagenUrl: String,

    /**
     * Obtener datos de Internet.
     * juegoUrl: Dirección web de la actividad interactiva.
     * Al ser un String opcional (String?), evitamos que la app falle si el
     * dato llega vacío desde el servidor.
     */
    val juegoUrl: String? = "",

    /**
     * PUNTUACIÓN: Representa el progreso del niño (estrellas).
     * Este dato se carga junto con el resto del objeto en los
     * puntos de suspensión de la corrutina.
     */
    val puntuacion: Int = 0
)
