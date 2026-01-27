package com.example.pmdm01_23_workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class CleanupWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // RECIBIMOS LA SALIDA DE LA TAREA ANTERIOR
        val mensajeAnterior = inputData.getString("MENSAJE_RESULTADO")

        Log.d("ABN_Cloud", "Encadenamiento: $mensajeAnterior. Ahora limpiando temporales...")
        // Simulamos una limpieza rápida
        return Result.success()
    }
}