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
    // Estado inicial
    var fondoActual by remember { mutableStateOf(R.drawable.fons1) }
    var mostrarDialogo by remember { mutableStateOf("Vamos a contar manzanas") }

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

        // Números de ejemplo
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (i in 1..5) {
                val id = when(i) {
                    1 -> R.drawable.num1
                    2 -> R.drawable.num2
                    3 -> R.drawable.num3
                    4 -> R.drawable.num4
                    else -> R.drawable.num5
                }
                Image(
                    painter = painterResource(id = id),
                    contentDescription = "Número $i",
                    modifier = Modifier
                        .size(60.dp)
                        .clickable { /* interacción aún por implementar */ }
                )
            }
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
