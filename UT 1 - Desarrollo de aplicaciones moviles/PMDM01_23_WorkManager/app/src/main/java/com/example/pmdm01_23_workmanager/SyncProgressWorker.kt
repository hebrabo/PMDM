package com.example.pmdm01_23_workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

class SyncProgressWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // PARÁMETROS DE ENTRADA: Recibimos el nombre del archivo desde la UI
        val archivo = inputData.getString("ARCHIVO_A_SUBIR") ?: "desconocido.txt"

        Log.d("ABN_Cloud", "Iniciando respaldo de: $archivo")
        delay(1500)

        return try {
            for (i in 1..4) {
                delay(800)
                Log.d("ABN_Cloud", "Subiendo $archivo... ${i * 25}%")
            }

            // PARÁMETROS DE SALIDA: Enviamos un mensaje de éxito a la siguiente tarea
            val datosSalida = workDataOf("MENSAJE_RESULTADO" to "Copia de $archivo finalizada")

            Log.d("ABN_Cloud", "Sincronización completada.")
            Result.success(datosSalida)
        } catch (e: Exception) {
            Log.e("ABN_Cloud", "Error en hardware/red: ${e.message}")
            Result.retry()
        }
    }
}