package com.example.pmdm01_17_miniproyectoconectarseinternet

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException

/**
 * 1. DEFINICIÓN DE ESTADOS (UI STATE)
 * Siguiendo el patrón MVVM, definimos una interfaz sellada para representar
 * los diferentes estados de la pantalla según el flujo de la corrutina.
 */
sealed interface AbnUiState {
    data class Success(val juegos: List<JuegoAbn>) : AbnUiState
    object Error : AbnUiState
    object Loading : AbnUiState
}

class AbnViewModel : ViewModel() {

    /**
     * abnUiState: Estado reactivo que observa la interfaz.
     * Cambia de 'Loading' a 'Success' o 'Error' conforme avanza la corrutina.
     */
    var abnUiState: AbnUiState by mutableStateOf(AbnUiState.Loading)
        private set

    init {
        // Al iniciar el ViewModel, lanzamos la petición de datos.
        getJuegosAbn()
    }

    /**
     * Get data from the internet / Obtener datos de Internet.
     */
    fun getJuegosAbn() {
        /**
         * viewModelScope.launch: Iniciamos una corrutina en el hilo principal (Main Thread).
         * Esto permite que la app no se bloquee mientras espera
         * los datos, ya que la corrutina se ejecuta de forma asíncrona.
         */
        viewModelScope.launch {
            abnUiState = AbnUiState.Loading

            try {
                /**
                 * PUNTO DE SUSPENSIÓN: La ejecución se pausa aquí mientras Retrofit descarga el JSON.
                 * Mientras espera, el hilo principal queda libre para otras tareas (como animaciones).
                 */
                val listResult = RetrofitInstance.api.getJuegos()

                // Si la descarga tiene éxito, actualizamos el estado.
                Log.d("ABN_DEBUG", "¡Éxito! Juegos recibidos: ${listResult.size}")
                abnUiState = AbnUiState.Success(listResult)

            } catch (e: CancellationException) {
                /**
                 * Manejo de la cancelación.
                 * Si el usuario cierra la pantalla, la corrutina se cancela.
                 * Es obligatorio capturar y relanzar 'CancellationException'.
                 */
                Log.e("ABN_DEBUG", "Corrutina cancelada: ${e.message}")
                throw e

            } catch (e: Exception) {
                /**
                 * CAPTURA DE ERRORES: Manejamos fallos de red o errores en el JSON.
                 * Esto evita que la aplicación se cierre (crash) ante un fallo de conexión.
                 */
                Log.e("ABN_DEBUG", "Fallo en la descarga: ${e.message}", e)
                abnUiState = AbnUiState.Error
            }
        }
    }
}