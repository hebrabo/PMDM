package com.example.pmdm01_17_miniproyectoconectarseinternet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AbnLibraryScreen(uiState: AbnUiState, onRetry: () -> Unit) {
    when (uiState) {
        is AbnUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is AbnUiState.Error -> {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("¡Ups! No hay internet")
                Button(onClick = onRetry) { Text("Reintentar") }
            }
        }
        is AbnUiState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 columnas, ideal para niños
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.juegos) { juego ->
                    JuegoCard(juego = juego)
                }
            }
        }
    }
}