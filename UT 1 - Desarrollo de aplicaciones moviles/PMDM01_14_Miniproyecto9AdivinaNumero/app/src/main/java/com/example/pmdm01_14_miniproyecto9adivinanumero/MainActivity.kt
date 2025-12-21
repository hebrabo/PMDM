package com.example.pmdm01_14_miniproyecto9adivinanumero

/*
 * Mini juego de “Adivina el número”
 * El usuario intenta adivinar un número secreto entre 1 y 100.
 * El ViewModel gestiona el número oculto y genera pistas (“mayor”, “menor”).
 */


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.pmdm01_14_miniproyecto9adivinanumero.ui.GameScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Envolvemos la app en el Tema de diseño
            MaterialTheme {
                // Surface es el lienzo base estándar de Material Design
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ------------------------------------------------------------------
                    // PUNTO DE ENTRADA ÚNICO
                    // ------------------------------------------------------------------
                    // Ya no instanciamos el ViewModel aquí, ni observamos el estado.
                    // Hemos delegado esa responsabilidad a 'GameScreen' para que
                    // la Activity quede totalmente limpia y desacoplada.
                    //
                    // Simplemente "pintamos" la pantalla del juego.
                    // ------------------------------------------------------------------
                    GameScreen()
                }
            }
        }
    }
}