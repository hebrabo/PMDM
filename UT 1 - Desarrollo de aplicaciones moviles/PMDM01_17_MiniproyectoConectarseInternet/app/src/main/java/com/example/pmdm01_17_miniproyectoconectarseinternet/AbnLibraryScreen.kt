package com.example.pmdm01_17_miniproyectoconectarseinternet

import android.util.Log // Importante para ver los fallos en Logcat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AbnLibraryScreen(uiState: AbnUiState, onRetry: () -> Unit) {
    val uriHandler = LocalUriHandler.current

    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is AbnUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is AbnUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("¡Ups! No hay internet", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onRetry) { Text("Reintentar") }
                }
            }
            is AbnUiState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.juegos) { juego ->
                        JuegoCard(
                            juego = juego,
                            onClick = {
                                val url = juego.juegoUrl

                                // LOG DE CONTROL: Verás esto en la pestaña Logcat
                                Log.d("ABN_DEBUG", "Click en: ${juego.titulo}")
                                Log.d("ABN_DEBUG", "URL intentada: '$url'")

                                if (!url.isNullOrEmpty()) {
                                    try {
                                        uriHandler.openUri(url)
                                    } catch (e: Exception) {
                                        Log.e("ABN_DEBUG", "Error abriendo URL: ${e.message}")
                                    }
                                } else {
                                    Log.w("ABN_DEBUG", "¡Atención! La URL para este juego está VACÍA")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, name = "Librería ABN Completa")
@Composable
fun AbnLibraryScreenPreview() {
    val juegosDePrueba = listOf(
        JuegoAbn(1, "Cuantificadores en la clase", "contar", "cuantificadores", "", "", "https://www.penyagolosaeduca.com/cuantificadores-en-la-clase/"),
        JuegoAbn(2, "El doble de palillos (1-50)", "numeracion", "dobles", "", "", "https://www.penyagolosaeduca.com/el-doble-de-palillos-1-50/"),
        JuegoAbn(3, "¿Cuántas abejas hay? (1-10)", "operaciones", "sumar", "", "", "https://www.penyagolosaeduca.com/cuantas-abejas-hay-en-las-dos-colmenas1-10/"),
        JuegoAbn(4, "Reparto en bandejas", "operaciones", "reparto", "", "", "https://www.penyagolosaeduca.com")
    )

    val estadoExito = AbnUiState.Success(juegos = juegosDePrueba)

    MaterialTheme {
        AbnLibraryScreen(
            uiState = estadoExito,
            onRetry = { }
        )
    }
}