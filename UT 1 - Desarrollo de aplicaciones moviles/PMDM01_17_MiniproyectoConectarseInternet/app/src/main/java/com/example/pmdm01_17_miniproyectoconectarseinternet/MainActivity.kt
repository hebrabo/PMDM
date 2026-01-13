package com.example.pmdm01_17_miniproyectoconectarseinternet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * MAIN ACTIVITY: El punto de entrada de la aplicación.
 * Aquí se configura el entorno de Compose y se vincula la lógica de negocio (ViewModel)
 * con la interfaz de usuario (Screen).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContent define el contenido visual de la actividad usando Jetpack Compose.
        setContent {
            // Aplicamos el tema de diseño de Material3.
            MaterialTheme {

                /**
                 * ARQUITECTURA MVVM: Obtenemos el ViewModel.
                 * Al usar 'viewModel()', Android se encarga de mantener viva la lógica y los
                 * datos de los juegos de la app aunque el niño gire la pantalla.
                 */
                val viewModel: AbnViewModel = viewModel()

                /**
                 * Obtener datos de Internet.
                 * Llamamos a la pantalla de la librería pasando el 'uiState'.
                 * Este estado es reactivo: cuando la corrutina en el ViewModel termina su
                 * punto de suspensión (descarga del JSON), esta pantalla se redibuja sola.
                 */
                AbnLibraryScreen(
                    uiState = viewModel.abnUiState,
                    /**
                     * GESTIÓN DE ERRORES: Función de reintento.
                     * Si la corrutina falló por falta de red (capturado en el catch), pasamos
                     * la función para que el usuario pueda reiniciar el flujo de datos.
                     */
                    onRetry = { viewModel.getJuegosAbn() }
                )
            }
        }
    }
}