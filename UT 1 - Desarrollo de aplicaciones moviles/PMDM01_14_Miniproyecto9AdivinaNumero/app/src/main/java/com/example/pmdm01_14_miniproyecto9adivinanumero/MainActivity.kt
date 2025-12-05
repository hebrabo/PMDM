package com.example.pmdm01_14_miniproyecto9adivinanumero

/*
 * Mini juego de “Adivina el número”
 * El usuario intenta adivinar un número secreto entre 1 y 100.
 * El ViewModel gestiona el número oculto y genera pistas (“mayor”, “menor”).
 */


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.pmdm01_14_miniproyecto9adivinanumero.ui.GuessNumberScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                GuessNumberScreen()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GuessNumberTheme {
        GuessNumberScreen()

    }
}

@Composable
fun GuessNumberTheme(content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}