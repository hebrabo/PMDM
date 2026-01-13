package com.example.pmdm01_17_miniproyectoconectarseinternet

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// 1. Definimos los estados posibles de la interfaz (UI)
sealed interface AbnUiState {
    data class Success(val juegos: List<JuegoAbn>) : AbnUiState
    object Error : AbnUiState
    object Loading : AbnUiState
}

class AbnViewModel : ViewModel() {

    // Estado que observa la pantalla de Compose
    var abnUiState: AbnUiState by mutableStateOf(AbnUiState.Loading)
        private set

    // Al crear el ViewModel, intentamos cargar los datos automáticamente
    init {
        getJuegosAbn()
    }

    fun getJuegosAbn() {
        viewModelScope.launch {
            // Ponemos la pantalla en modo "Cargando..."
            abnUiState = AbnUiState.Loading

            abnUiState = try {
                // Intentamos obtener la lista del servidor
                val listResult = RetrofitInstance.api.getJuegos()

                // Si llegamos aquí, la descarga fue un éxito
                Log.d("ABN_DEBUG", "¡Éxito! Juegos recibidos: ${listResult.size}")
                AbnUiState.Success(listResult)

            } catch (e: Exception) {
                // Capturamos CUALQUIER error (Red, JSON mal formado, URL incorrecta...)
                // El mensaje aparecerá en rojo en la pestaña Logcat
                Log.e("ABN_DEBUG", "Fallo en la descarga: ${e.message}", e)

                // Mostramos la pantalla de "Error / Reintentar"
                AbnUiState.Error
            }
        }
    }
}