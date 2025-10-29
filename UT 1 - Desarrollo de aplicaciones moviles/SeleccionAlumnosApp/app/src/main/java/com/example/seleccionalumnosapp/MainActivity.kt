package com.example.seleccionalumnosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.seleccionalumnosapp.ui.theme.SeleccionAlumnosAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeleccionAlumnosAppTheme(darkTheme = false, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SeleccionAlumnosApp()
                }
            }
        }
    }
}

@Composable
fun SeleccionAlumnosApp() {
    val alumnos = remember {
        mutableStateListOf(
            "Ana Martínez", "Luis González", "Carlos Pereira",
            "Marta López", "Sofía Martín", "Mireia Santos", "Pedro Ruiz",
            "Laura Fernández", "David Ortega", "Lucía Gómez", "Javier Ruiz"
        )
    }

    // Creamos un estado de scroll manual
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState) // Hacemos la columna desplazable
    ) {
        alumnos.forEach { alumno ->
            Text(
                text = alumno,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }
}






