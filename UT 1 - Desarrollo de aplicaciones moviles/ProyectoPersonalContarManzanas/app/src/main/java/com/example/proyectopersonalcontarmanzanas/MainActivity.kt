package com.example.proyectopersonalcontarmanzanas

import android.os.Bundle
import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.proyectopersonalcontarmanzanas.ui.theme.ProyectoPersonalContarManzanasTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoPersonalContarManzanasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JuegoInteractivo()
                }
            }
        }
    }
}
@Composable
fun JuegoInteractivo() {
    // Mapear cada fondo con su cantidad de manzanas
    val fondosConCantidad = mapOf(
        R.drawable.fons1 to 1,
        R.drawable.fons2 to 2,
        R.drawable.fons3 to 3,
        R.drawable.fons4 to 4,
        R.drawable.fons5 to 5
    )

    var fondoActual by remember { mutableStateOf(R.drawable.fons0) }
    var cantidadManzanas by remember { mutableStateOf(1) }
    var mostrarFlecha by remember { mutableStateOf(false) }
    var mostrarDialogo by remember { mutableStateOf("Vamos a contar manzanas") }
    var estadoNumeros by remember { mutableStateOf(List(5) { "normal" }) } // "normal", "verde", "rojo"
    var iFallo by remember { mutableStateOf<Int?>(null) } // índice del fallo para restaurar

    // Iniciar juego: seleccionar fondo aleatorio y cantidad correcta
    LaunchedEffect(Unit) {
        val nuevoFondo = fondosConCantidad.keys.random()
        fondoActual = nuevoFondo
        cantidadManzanas = fondosConCantidad[nuevoFondo] ?: 1
        delay(1000)
        mostrarDialogo = "¿Cuántas manzanas hay?"
    }

    // Restaurar color de fallo tras 1 segundo
    LaunchedEffect(iFallo) {
        if (iFallo != null) {
            delay(1000)
            estadoNumeros = estadoNumeros.toMutableList().apply { this[iFallo!!] = "normal" }
            iFallo = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = fondoActual),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Personaje
        Image(
            painter = painterResource(id = R.drawable.personaje_hablar),
            contentDescription = "Personaje",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomStart)
                .padding(start = 16.dp)
        )

        // Burbuja de diálogo
        BurbujaDeDialogo(
            texto = mostrarDialogo,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 40.dp, y = (-180).dp)
        )

        // Números
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (i in 1..5) {
                val estado = estadoNumeros[i - 1]
                val id = when (estado) {
                    "verde" -> when (i) { 1 -> R.drawable.num1v; 2 -> R.drawable.num2v; 3 -> R.drawable.num3v; 4 -> R.drawable.num4v; else -> R.drawable.num5v }
                    "rojo" -> when (i) { 1 -> R.drawable.num1r; 2 -> R.drawable.num2r; 3 -> R.drawable.num3r; 4 -> R.drawable.num4r; else -> R.drawable.num5r }
                    else -> when (i) { 1 -> R.drawable.num1; 2 -> R.drawable.num2; 3 -> R.drawable.num3; 4 -> R.drawable.num4; else -> R.drawable.num5 }
                }
                Image(
                    painter = painterResource(id = id),
                    contentDescription = "Número $i",
                    modifier = Modifier
                        .size(60.dp)
                        .clickable(enabled = !mostrarFlecha) {
                            if (i == cantidadManzanas) {
                                // Acierto
                                estadoNumeros = estadoNumeros.toMutableList().apply { this[i - 1] = "verde" }
                                mostrarDialogo = "¡Muy bien!"
                                mostrarFlecha = true
                            } else {
                                // Fallo
                                estadoNumeros = estadoNumeros.toMutableList().apply { this[i - 1] = "rojo" }
                                mostrarDialogo = "Inténtalo otra vez"
                                iFallo = i - 1
                            }
                        }
                )
            }
        }

        // Flecha siguiente
        if (mostrarFlecha) {
            Image(
                painter = painterResource(id = R.drawable.flecha),
                contentDescription = "Siguiente",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(32.dp)
                    .size(80.dp)
                    .clickable {
                        mostrarFlecha = false
                        mostrarDialogo = "¿Cuántas manzanas hay?"
                        estadoNumeros = List(5) { "normal" }

                        // Nueva ronda con fondo y cantidad sincronizados
                        val nuevoFondo = fondosConCantidad.keys.random()
                        fondoActual = nuevoFondo
                        cantidadManzanas = fondosConCantidad[nuevoFondo] ?: 1
                    }
            )
        }
    }
}

@Composable
fun BurbujaDeDialogo(texto: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = texto, color = Color.Black)
    }
}



