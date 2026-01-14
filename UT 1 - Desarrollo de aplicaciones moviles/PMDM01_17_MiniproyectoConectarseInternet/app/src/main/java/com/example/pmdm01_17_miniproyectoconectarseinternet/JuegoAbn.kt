package com.example.pmdm01_17_miniproyectoconectarseinternet

// REQUISITO: Importar Serializable para que la biblioteca funcione
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * DATA CLASS: El Modelo de Datos.
 * @Serializable: Esta etiqueta es OBLIGATORIA para usar kotlinx.serialization.
 * Indica que esta clase puede convertirse desde una cadena JSON a objetos Kotlin.
 */
@Serializable
data class JuegoAbn(
    // ID único para identificar cada actividad dentro de los juegos de la app.
    val id: Int,

    // Información textual que se mostrará en las tarjetas de la interfaz.
    val titulo: String,
    val categoria: String,
    val etiqueta: String,
    val descripcion: String,

    /**
     * @SerialName: Mapea la clave exacta del JSON con tu variable.
     * Es útil si el JSON usa nombres distintos o para asegurar el mapeo correcto.
     */
    @SerialName(value = "imagenUrl")
    val imagenUrl: String,

    @SerialName(value = "juegoUrl")
    val juegoUrl: String? = "",

    /**
     * PUNTUACIÓN: Representa el progreso del niño (estrellas).
     * Este dato se carga junto con el resto del objeto en los
     * puntos de suspensión de la corrutina.
     */
    val puntuacion: Int = 0
)