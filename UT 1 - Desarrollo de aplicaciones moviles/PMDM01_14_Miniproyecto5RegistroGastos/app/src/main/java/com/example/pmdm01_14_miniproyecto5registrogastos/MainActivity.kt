package com.example.pmdm01_14_miniproyecto5registrogastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.pmdm01_14_miniproyecto5registrogastos.ui.ExpenseScreen

/*
 * Proyecto: Registro básico de gastos
 * Pantalla principal de la app.
 * Aquí simplemente cargamos la interfaz ExpenseScreen envuelta en MaterialTheme.
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContent define el contenido Compose que aparece en pantalla.
        setContent {
            MaterialTheme {
                // Cargamos nuestra pantalla principal
                ExpenseScreen()
            }
        }
    }
}
