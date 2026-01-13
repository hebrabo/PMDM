package com.example.pmdm01_17_miniproyectoconectarseinternet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuegoCard(
    juego: JuegoAbn,
    onClick: () -> Unit, // <--- Parámetro necesario para la navegación
    modifier: Modifier = Modifier
) {
    val colorFondoSeccion = when (juego.categoria.lowercase()) {
        "contar" -> Color(0xFFE8F5E9)      // Verde pastel
        "numeracion" -> Color(0xFFE3F2FD)  // Azul pastel
        "operaciones" -> Color(0xFFFCE4EC) // Rosa pastel
        else -> Color(0xFFFFF8E1)          // Vainilla
    }

    Card(
        onClick = onClick, // <--- La tarjeta ahora es clickable
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = juego.imagenUrl,
                contentDescription = juego.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorFondoSeccion)
                    .padding(20.dp)
            ) {
                Text(
                    text = juego.titulo,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF212121)
                )

                Text(
                    text = juego.categoria.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF757575),
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                EtiquetaNaranja(texto = juego.etiqueta)
            }
        }
    }
}

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

@Preview(showBackground = true, name = "Vista Previa Tarjeta")
@Composable
fun JuegoCardPreview() {
    val juegoPrueba = JuegoAbn(
        id = 1,
        titulo = "Juego de Prueba",
        categoria = "contar",
        etiqueta = "conteo",
        descripcion = "",
        imagenUrl = "",
        juegoUrl = ""
    )

    Box(modifier = Modifier.padding(16.dp)) {
        JuegoCard(juego = juegoPrueba, onClick = {})
    }
}