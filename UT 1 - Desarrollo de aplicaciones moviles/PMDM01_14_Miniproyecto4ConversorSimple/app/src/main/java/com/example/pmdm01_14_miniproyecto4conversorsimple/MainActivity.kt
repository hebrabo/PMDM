package com.example.pmdm01_14_miniproyecto4conversorsimple
/*
 * PROYECTO 4: Conversor simple (temperatura, divisas…)
 *
 * Objetivo:
 *  - Crear un conversor sencillo (por ejemplo, Celsius ↔ Fahrenheit).
 *  - La interfaz se realiza con Jetpack Compose.
 *  - El ViewModel es el encargado de guardar el valor introducido por el usuario
 *    y calcular el resultado convertido.
 *
 * Este archivo contiene únicamente la Activity principal, responsable de:
 *  1. Inicializar Compose.
 *  2. Aplicar un tema.
 *  3. Mostrar la pantalla principal (ConverterScreen).
 */


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pmdm01_14_miniproyecto4conversorsimple.ui.ConverterScreen
import com.example.pmdm01_14_miniproyecto4conversorsimple.ui.theme.PMDM01_14_Miniproyecto4ConversorSimpleTheme

class MainActivity : ComponentActivity() {
    /*
     * onCreate() es el punto de entrada de la Activity.
     * Aquí configuramos:
     *   - el modo "edge-to-edge" (para aprovechar toda la pantalla)
     *   - Compose como sistema de UI
     *   - el tema de la app
     *   - la pantalla principal (ConverterScreen)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PMDM01_14_Miniproyecto4ConversorSimpleTheme {
                Scaffold { innerPadding ->
                    ConverterScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PMDM01_14_Miniproyecto4ConversorSimpleTheme {
        ConverterScreen()
    }
}