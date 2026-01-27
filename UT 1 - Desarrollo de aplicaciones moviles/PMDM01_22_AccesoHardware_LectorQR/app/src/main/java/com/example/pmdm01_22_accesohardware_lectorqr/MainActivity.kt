package com.example.pmdm01_22_accesohardware_lectorqr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.*
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Iniciamos el flujo de la aplicación con la gestión de permisos
                    CameraPermissionWrapper()
                }
            }
        }
    }
}

/**
 * GESTIÓN DE PERMISOS:
 * Los permisos de hardware como la Cámara deben pedirse
 * en tiempo de ejecución, no solo declararlos en el Manifest.
 * Usamos la librería Accompanist para integrar este flujo de forma reactiva en Compose.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionWrapper() {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    if (cameraPermissionState.status.isGranted) {
        // Si el usuario ya aceptó el permiso, mostramos la pantalla de escaneo
        ScannerScreen()
    } else {
        // Interfaz de solicitud de permiso: Obligatoria por seguridad y privacidad
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Se requiere acceso a la cámara para el Lector QR", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Conceder Permiso")
            }
        }
    }
}

/**
 * PANTALLA PRINCIPAL DEL ESCÁNER:
 * Aquí combinamos tres tecnologías clave:
 * 1. CameraX (Hardware): Para acceder a la lente y previsualizar la imagen.
 * 2. AndroidView: Un "puente" para usar componentes clásicos (PreviewView) dentro de Compose.
 * 3. ML Kit (IA): Para procesar los fotogramas y reconocer el código QR.
 */
@OptIn(ExperimentalGetImage::class)
@Composable
fun ScannerScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current // Vincula la cámara al ciclo de vida de la app
    val uriHandler = LocalUriHandler.current // Permite abrir enlaces en el navegador

    var qrResult by remember { mutableStateOf("Enfoca un código QR") }

    // Componentes técnicos de CameraX
    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    Box(modifier = Modifier.fillMaxSize()) {
        // --- HARDWARE: LA CÁMARA ---
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        // --- INTERFAZ: TARJETA DE RESULTADO INTERACTIVA ---
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth(),
            // Acción: Si es un enlace, al tocar la tarjeta se abre el navegador
            onClick = {
                if (qrResult.startsWith("http")) {
                    try { uriHandler.openUri(qrResult) } catch (e: Exception) { /* Error de URI */ }
                }
            },
            colors = CardDefaults.cardColors(
                containerColor = if (qrResult.startsWith("http")) Color(0xFFE3F2FD) else Color.White.copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = if (qrResult.startsWith("http")) "🔗 Enlace detectado (Toca para abrir):" else "Contenido:",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = qrResult,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (qrResult.startsWith("http")) Color(0xFF1976D2) else Color.Black
                )
            }
        }
    }

    // --- LÓGICA DE INICIALIZACIÓN DE HARDWARE E INTELIGENCIA ARTIFICIAL ---
    LaunchedEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()

        // Configuración de la Previsualización (Hardware)
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        // Configuración del Análisis de Imagen (Inteligencia Artificial)
        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST) // Solo analiza el frame más reciente para no saturar
            .build()

        val scanner = BarcodeScanning.getClient() // Motor de ML Kit

        // El Analyzer procesa cada fotograma que llega de la cámara
        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                // Convertimos el frame de la cámara al formato que entiende ML Kit
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            // Cuando se detecta un QR, actualizamos el estado de la UI
                            qrResult = barcode.rawValue ?: "QR detectado"
                        }
                    }
                    .addOnCompleteListener {
                        // IMPORTANTE: Liberamos el frame actual para que pueda entrar el siguiente
                        imageProxy.close()
                    }
            }
        }

        try {
            // Unificamos todo: Previsualización + Análisis de IA
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA, // Usamos la cámara trasera por defecto
                preview,
                imageAnalysis
            )
        } catch (e: Exception) {
            qrResult = "Error al iniciar cámara: ${e.message}"
        }
    }
}