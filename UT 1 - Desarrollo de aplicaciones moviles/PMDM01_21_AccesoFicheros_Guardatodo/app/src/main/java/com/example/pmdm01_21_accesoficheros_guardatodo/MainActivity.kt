package com.example.pmdm01_21_accesoficheros_guardatodo

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pmdm01_21_accesoficheros_guardatodo.ui.theme.PMDM01_21_AccesoFicheros_GuardatodoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PMDM01_21_AccesoFicheros_GuardatodoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GestorProgresoApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GestorProgresoApp(modifier: Modifier = Modifier) {
    // 1. OBTENCIÓN DEL CONTEXTO: Necesario en Compose para acceder a archivos internos
    val context = LocalContext.current
    val nombreArchivo = "progreso_juego.txt"

    // 2. ESTADOS: Variables reactivas. Si cambian, la UI se actualiza sola.
    // Usamos mutableIntStateOf para optimizar el manejo de números enteros
    var nivelActual by remember { mutableIntStateOf(1) }
    var mensajeEstado by remember { mutableStateOf("¡Bienvenido al sistema de progreso!") }

    // 3. LÓGICA DE CARGA (LECTURA): Se ejecuta automáticamente al abrir la App
    // LaunchedEffect(Unit) garantiza que esto solo ocurra una vez al inicio
    LaunchedEffect(Unit) {
        try {
            // openFileInput: Abre el flujo de lectura del almacenamiento PRIVADO
            context.openFileInput(nombreArchivo).use { input ->
                // Convertimos el flujo de bytes en texto legible
                val contenido = input.bufferedReader().readText()
                // Convertimos el String guardado ("5") de nuevo a un número (5)
                nivelActual = contenido.toInt()
                mensajeEstado = "Partida recuperada: Nivel $nivelActual"
            }
        } catch (e: Exception) {
            // Si el archivo no existe (primera vez), se captura el error y no pasa nada
            mensajeEstado = "No hay datos previos. Empiezas desde el Nivel 1."
        }
    }

    // 4. INTERFAZ VISUAL (UI)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "CONTROL DE PROGRESO ABN",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Tarjeta visual para mostrar el nivel
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            modifier = Modifier.size(180.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "NIVEL ACTUAL", fontSize = 14.sp)
                Text(
                    text = nivelActual.toString(),
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Controles de nivel (Modifican el ESTADO, pero no guardan aún en el archivo)
        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            Button(onClick = { if (nivelActual > 1) nivelActual-- }) {
                Text("Bajar")
            }
            Button(onClick = { if (nivelActual < 100) nivelActual++ }) {
                Text("Subir")
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        // 5. LÓGICA DE GUARDADO (ESCRITURA): Persistencia de datos
        Button(
            onClick = {
                try {
                    // openFileOutput: Crea/Abre el archivo. MODE_PRIVATE lo hace exclusivo de esta app.
                    context.openFileOutput(nombreArchivo, Context.MODE_PRIVATE).use { output ->
                        // Los archivos guardan BYTES, por eso convertimos Int -> String -> ByteArray
                        output.write(nivelActual.toString().toByteArray())
                    }
                    mensajeEstado = "✅ ¡Nivel $nivelActual guardado permanentemente!"
                } catch (e: Exception) {
                    mensajeEstado = "❌ Error: No se pudo escribir en la memoria."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("GUARDAR PROGRESO")
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Feedback textual para el usuario
        Text(text = mensajeEstado, textAlign = TextAlign.Center, fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.weight(1f))

        // 6. LÓGICA DE BORRADO: Limpiar datos del almacenamiento
        TextButton(onClick = {
            nivelActual = 1
            // deleteFile: Elimina físicamente el archivo del almacenamiento interno
            context.deleteFile(nombreArchivo)
            mensajeEstado = "Archivo borrado. Progreso reseteado."
        }) {
            Text("Borrar datos y reiniciar partida", color = Color.Red)
        }
    }
}