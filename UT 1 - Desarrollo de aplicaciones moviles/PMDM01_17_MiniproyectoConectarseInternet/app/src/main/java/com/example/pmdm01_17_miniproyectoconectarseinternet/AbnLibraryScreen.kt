package com.example.pmdm01_17_miniproyectoconectarseinternet

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Pantalla de Librería ABN.
 * Esta pantalla es reactiva: responde automáticamente al estado (uiState) que el ViewModel
 * gestiona mediante corrutinas para no bloquear el hilo principal.
 */
@Composable
fun AbnLibraryScreen(uiState: AbnUiState, onRetry: () -> Unit) {
    // LocalUriHandler permite a la app interactuar con el sistema para abrir URLs externas.
    val uriHandler = LocalUriHandler.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Usamos una máquina de estados para manejar la respuesta asíncrona de Internet.
        when (uiState) {

            /**
             * ESTADO: Loading (Cargando)
             * Mientras la corrutina en el ViewModel está suspendida esperando los datos del JSON,
             * el hilo principal (Main Thread) NO se bloquea  y permite mostrar este indicador visual.
             */
            is AbnUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            /**
             * ESTADO: Error
             * Si ocurre una excepción durante la descarga (ej. falta de conexión), el bloque 'catch'
             * del ViewModel actualiza el estado a Error.
             */
            is AbnUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("¡Ups! No hay internet", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    // El botón permite reiniciar el flujo de la corrutina (función onRetry).
                    Button(onClick = onRetry) { Text("Reintentar") }
                }
            }

            /**
             * ESTADO: Success (Éxito) - Obtener datos de Internet.
             * Una vez que la corrutina finaliza su tarea en segundo plano, recibimos la lista de juegos
             * y procedemos a dibujarlos en la interfaz.
             */
            is AbnUiState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Cargar y mostrar imágenes de Internet.
                    // 'items' itera sobre los datos obtenidos. Cada JuegoCard internamente
                    // usa Coil (AsyncImage) para cargar la imagen asíncronamente.
                    items(uiState.juegos) { juego ->
                        JuegoCard(
                            juego = juego,
                            onClick = {
                                val url = juego.juegoUrl

                                // LOGS DE CONTROL: Fundamentales para verificar el éxito del mapeo JSON en Logcat.
                                Log.d("ABN_DEBUG", "Click en: ${juego.titulo}")
                                Log.d("ABN_DEBUG", "URL intentada: '$url'")

                                if (!url.isNullOrEmpty()) {
                                    try {
                                        // Abre el navegador con la actividad interactiva seleccionada.
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