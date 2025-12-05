package com.example.pmdm01_14_miniproyyecto1contadorhistorial

/*
* Contador avanzado con historial
* Crea una app con un contador que permita sumar, restar y reiniciar.
* Debe mostrar un historial de operaciones. El ViewModel gestionará el estado y el historial.
 */

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pmdm01_14_miniproyyecto1contadorhistorial.ui.CounterScreen
import com.example.pmdm01_14_miniproyecto1contadoravanzadoconhistorial.ui.theme.PMDM01_14_Miniproyecto1ContadorAvanzadoConHistorialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // // Permite que la interfaz ocupe toda la pantalla
        setContent { // // setContent inicia Jetpack Compose
            PMDM01_14_Miniproyecto1ContadorAvanzadoConHistorialTheme { //  // Aplicamos el tema a toda la app
                CounterScreen() // // Llamamos a la pantalla principal de la app
            }
        }
    }
}
