package com.example.pmdm01_23_workmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WorkManagerScreen()
                }
            }
        }
    }
}

@Composable
fun WorkManagerScreen() {
    val context = androidx.compose.ui.platform.LocalContext.current
    var statusText by remember { mutableStateOf("Sistema listo para sincronizar") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Gestor de Respaldo ABN", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                statusText = "Encadenando tareas: Sincronización + Limpieza..."

                // 1. RESTRICCIONES DE HARDWARE
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresCharging(true)
                    .build()

                // 2. PARÁMETROS DE ENTRADA (Input Data)
                // Pasamos el nombre del archivo de la Tarea 1 como dato
                val datosEntrada = workDataOf("ARCHIVO_A_SUBIR" to "progreso_abn.txt")

                // 3. PETICIÓN 1: Sincronización (Con datos y restricciones)
                val syncRequest = OneTimeWorkRequestBuilder<SyncProgressWorker>()
                    .setConstraints(constraints)
                    .setInputData(datosEntrada)
                    .build()

                // 4. PETICIÓN 2: Limpieza (Tarea encadenada)
                val cleanupRequest = OneTimeWorkRequestBuilder<CleanupWorker>().build()

                // 5. ENCADENAMIENTO (Chaining)
                // "Primero sincroniza (con sus reglas) y luego limpia"
                WorkManager.getInstance(context)
                    .beginWith(syncRequest)
                    .then(cleanupRequest)
                    .enqueue()
            }
        ) {
            Text("Iniciar Flujo de Trabajo")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = statusText, color = MaterialTheme.colorScheme.secondary)
    }
}