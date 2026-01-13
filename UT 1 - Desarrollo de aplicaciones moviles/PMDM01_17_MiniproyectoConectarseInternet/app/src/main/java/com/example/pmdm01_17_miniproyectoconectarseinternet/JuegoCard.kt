package com.example.pmdm01_17_miniproyectoconectarseinternet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * JuegoCard
 * Representa visualmente un juego ABN. Este componente recibe datos que han sido
 * previamente obtenidos de internet de forma asíncrona.
 * * @param juego: Objeto con la información (título, imagen, puntuación).
 * @param onClick: Acción que se ejecuta al pulsar la tarjeta (abrir la URL).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuegoCard(
    juego: JuegoAbn,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Lógica visual: Colores pastel para ayudar a la categorización (UX para niños).
    val colorFondoSeccion = when (juego.categoria.lowercase()) {
        "contar" -> Color(0xFFE8F5E9)      // Verde pastel
        "numeracion" -> Color(0xFFE3F2FD)  // Azul pastel
        "operaciones" -> Color(0xFFFCE4EC) // Rosa pastel
        else -> Color(0xFFFFF8E1)          // Vainilla
    }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            /**
             * Cargar y mostrar imágenes.
             * AsyncImage (de la librería Coil) carga la imagen de forma asíncrona.
             * Esto es fundamental para cumplir la pauta de NO BLOQUEAR el hilo principal
             * (Main Thread) mientras se descarga el recurso visual.
             */
            AsyncImage(
                model = juego.imagenUrl,
                contentDescription = juego.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop // Ajusta la imagen sin deformarla
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorFondoSeccion)
                    .padding(16.dp)
            ) {
                Text(
                    text = juego.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF212121)
                )

                /**
                 * REPRESENTACIÓN DE DATOS: Fila de Estrellas (Puntuaciones).
                 * Muestra el progreso del niño obtenido del modelo de datos.
                 * El icono se elige dinámicamente según el valor de 'puntuacion'.
                 */
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = if (index < juego.puntuacion) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = null,
                            tint = if (index < juego.puntuacion) Color(0xFFFFD600) else Color.LightGray,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Text(
                    text = juego.categoria.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF757575),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Componente reutilizable para las etiquetas de los juegos.
                EtiquetaNaranja(texto = juego.etiqueta)
            }
        }
    }
}

/**
 * COMPONENTE REUTILIZABLE: EtiquetaNaranja
 * Refuerza la identidad visual de la app ABN.
 */
@Composable
fun EtiquetaNaranja(texto: String) {
    Surface(
        color = Color(0xFFFB8C00),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = texto,
            color = Color.White,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )
    }
}