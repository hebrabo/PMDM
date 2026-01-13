package com.example.pmdm01_17_miniproyectoconectarseinternet

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun JuegoCard(juego: JuegoAbn, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Imagen de internet usando COIL
            AsyncImage(
                model = juego.imagenUrl,
                contentDescription = juego.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = juego.titulo, style = MaterialTheme.typography.titleLarge)
                Text(text = "Categoría: ${juego.categoria}", style = MaterialTheme.typography.labelMedium)
                // Aquí mostramos tu campo de etiqueta que añadimos antes
                SuggestionChip(
                    onClick = { },
                    label = { Text(juego.etiqueta) }
                )
            }
        }
    }
}