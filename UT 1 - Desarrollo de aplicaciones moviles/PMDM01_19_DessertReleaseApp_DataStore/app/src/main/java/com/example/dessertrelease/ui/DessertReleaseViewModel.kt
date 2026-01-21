/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.dessertrelease.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dessertrelease.DessertReleaseApplication
import com.example.dessertrelease.R
import com.example.dessertrelease.data.local.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * [APUNTE: VIEWMODEL]
 * Esta clase actúa como el "traductor" entre los datos (el Repositorio) y la pantalla (la UI).
 * 1) Recibe el 'userPreferencesRepository' por el constructor (Inyección de Dependencias)
 *      para no tener que preocuparse de cómo se guardan los datos.
 * 2) Al heredar de 'ViewModel()', asegura que el estado de la interfaz (como la elección
 *      de diseño) no se pierda si el usuario gira el teléfono o cambia el tamaño de la ventana.
 */
class DessertReleaseViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DessertReleaseUiState())

// ----- CÓMO LEE LA PREFERENCIA DE DISEÑO -----
    // Este es el "Estado de la Pantalla" que Compose observará
    val uiState: StateFlow<DessertReleaseUiState> =
        userPreferencesRepository.isLinearLayout // 1. Escuchamos al repositorio
            .map { isLinearLayout ->
                DessertReleaseUiState(isLinearLayout) // 2. Convertimos el Boolean en un objeto UI
            }
            .stateIn(
                scope = viewModelScope, // 3. Vinculamos al ciclo de vida del ViewModel
                // 4. Optimización para rotaciones de pantalla
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DessertReleaseUiState() // 5. Valor de seguridad al arrancar
            )

/// ----- CÓMO ALMACENAR LA PREFERENCIA DE DISEÑO -----
    /**
     * Esta función es llamada por la UI cuando el usuario pulsa el botón de diseño.
     * Usa una corrutina (viewModelScope.launch) para realizar la escritura en el
     * disco de forma asíncrona, evitando que la interfaz se congele.
     */
    fun selectLayout(isLinearLayout: Boolean) {
        viewModelScope.launch {
            // Le pedimos al repositorio que guarde el valor permanentemente
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
        }
    }

// ----- PROPORCIONA EL RESPOSITORIO AL VIEWMODEL ---
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // 1. Usamos la llave 'APPLICATION_KEY' para obtener la instancia de la app
                val application = (this[APPLICATION_KEY] as DessertReleaseApplication)

                // 2. Creamos el ViewModel pasándole el repositorio que vive en la Application
                DessertReleaseViewModel(application.userPreferencesRepository)
            }
        }
    }
}

/*
 * Data class containing various UI States for Dessert Release screens
 */
data class DessertReleaseUiState(
    val isLinearLayout: Boolean = true,
    val toggleContentDescription: Int =
        if (isLinearLayout) R.string.grid_layout_toggle else R.string.linear_layout_toggle,
    val toggleIcon: Int =
        if (isLinearLayout) R.drawable.ic_grid_layout else R.drawable.ic_linear_layout
)
