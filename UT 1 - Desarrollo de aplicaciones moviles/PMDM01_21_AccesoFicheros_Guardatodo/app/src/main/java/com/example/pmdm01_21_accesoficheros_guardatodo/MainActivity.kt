package com.example.pmdm01_21_accesoficheros_guardatodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pmdm01_21_accesoficheros_guardatodo.ui.theme.PMDM01_21_AccesoFicheros_GuardatodoTheme
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

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
    val context = LocalContext.current

    // --- REFERENCIA AL FICHERO (Alineado con los enlaces de la profesora) ---
    // context.filesDir: Accede al almacenamiento interno privado de la app.
    // File: Clase de java.io para gestionar la ruta del archivo físico.
    val archivoProgreso = File(context.filesDir, "progreso_abn.txt")

    // --- ESTADOS (Memoria reactiva de Compose) ---
    var nivelActual by remember { mutableIntStateOf(1) }
    var mensajeEstado by remember { mutableStateOf("Sistema de archivos listo.") }

    // --- LECTURA AUTOMÁTICA AL INICIAR ---
    LaunchedEffect(Unit) {
        if (archivoProgreso.exists()) {
            try {
                // FileInputStream: Abre el flujo de entrada para leer el fichero.
                FileInputStream(archivoProgreso).use { input ->
                    val contenido = input.bufferedReader().readText()
                    nivelActual = contenido.toInt()
                    mensajeEstado = "📂 Progreso recuperado del almacenamiento interno."
                }
            } catch (e: Exception) {
                mensajeEstado = "⚠️ Error al cargar el archivo de progreso."
            }
        } else {
            mensajeEstado = "🆕 Sin datos previos. Nivel inicial: 1."
        }
    }

    // --- DISEÑO DE LA INTERFAZ ---
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "GESTIÓN DE PROGRESO ABN",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(30.dp))

        // --- TARJETA VISUAL DEL NIVEL (Corregida con mejor espaciado) ---
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.size(180.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "NIVEL",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = nivelActual.toString(),
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Controles para cambiar el nivel en memoria RAM
        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            Button(onClick = { if (nivelActual > 1) nivelActual-- }) { Text("Bajar") }
            Button(onClick = { if (nivelActual < 100) nivelActual++ }) { Text("Subir") }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- BOTÓN GUARDAR (Uso de FileOutputStream según Enlace 2) ---
        Button(
            onClick = {
                try {
                    // FileOutputStream: Crea el flujo para escribir bytes en el disco.
                    FileOutputStream(archivoProgreso).use { output ->
                        output.write(nivelActual.toString().toByteArray())
                    }
                    mensajeEstado = "💾 Nivel $nivelActual guardado permanentemente."
                } catch (e: Exception) {
                    mensajeEstado = "❌ Error: No se pudo escribir el archivo."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
        ) {
            Text("GUARDAR PROGRESO", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Texto informativo para el usuario
        Text(text = mensajeEstado, textAlign = TextAlign.Center, fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.weight(1f))

        // --- BOTÓN REINICIAR (Gestión de eliminación de archivos) ---
        TextButton(onClick = {
            if (archivoProgreso.exists()) {
                // archivoProgreso.delete(): Elimina físicamente el archivo del dispositivo.
                archivoProgreso.delete()
                nivelActual = 1
                mensajeEstado = "🗑️ Archivo borrado. Datos reseteados."
            }
        }) {
            Text("Borrar datos guardados", color = Color.Red)
        }
    }
}