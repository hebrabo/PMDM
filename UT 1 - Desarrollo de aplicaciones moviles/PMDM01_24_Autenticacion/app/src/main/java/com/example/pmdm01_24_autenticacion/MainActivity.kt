package com.example.pmdm01_24_autenticacion

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

/**
 * MainActivity: Punto de entrada principal de la aplicación.
 * Aquí gestionamos el ciclo de vida de la autenticación y la navegación inicial.
 */
class MainActivity : ComponentActivity() {
    // Instancia de FirebaseAuth: El corazón de la seguridad en nuestro proyecto.
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos Firebase Auth para que esté listo antes de dibujar la interfaz.
        auth = FirebaseAuth.getInstance()

        // Ajusta el diseño para que use toda la pantalla (incluyendo la barra de estado).
        enableEdgeToEdge()

        setContent {
            // --- GESTIÓN DE LA PERSISTENCIA DE SESIÓN ---
            // 'auth.currentUser' es la clave: Si Firebase tiene un token guardado, no es nulo.
            // Esto permite que el tutor no tenga que loguearse cada vez que abre la app.
            var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }

            // Recuperamos el nombre del tutor guardado en el perfil de Firebase.
            var userName by remember { mutableStateOf(auth.currentUser?.displayName ?: "Tutor ABN") }

            // --- RECUPERACIÓN DE LOGIN PENDIENTE (Flujos OAuth) ---
            // Especialmente crítico para Microsoft: Si el navegador externo cierra la app al volver,
            // 'pendingAuthResult' recupera la sesión que quedó "en el aire".
            LaunchedEffect(Unit) {
                auth.pendingAuthResult?.addOnSuccessListener { authResult ->
                    userName = authResult.user?.displayName ?: "Tutor ABN"
                    isLoggedIn = true
                    Log.d("AUTH_PENDING", "Sesión recuperada con éxito")
                }?.addOnFailureListener { e ->
                    Log.e("AUTH_PENDING", "Fallo al recuperar sesión: ${e.message}")
                }
            }

            // --- NAVEGACIÓN CONDICIONAL ---
            // Si no hay sesión, mostramos la pantalla de Registro/Login.
            if (!isLoggedIn) {
                RegisterScreen(
                    onLoginSuccess = { nombre ->
                        userName = nombre
                        isLoggedIn = true // Cambiamos el estado para saltar al Dashboard.
                    },
                    onMicrosoftClick = {
                        // Lanzamos el flujo específico de Azure/Microsoft.
                        ejecutarLoginMicrosoft { nombre ->
                            userName = nombre
                            isLoggedIn = true
                        }
                    }
                )
            } else {
                // Si la sesión es válida, entramos al centro de mando de los 100 juegos.
                DashboardScreen(
                    userName = userName,
                    onLogout = {
                        // El proceso de Logout rompe la persistencia borrando el token local.
                        auth.signOut()
                        isLoggedIn = false // Volvemos automáticamente a la pantalla de registro.
                    }
                )
            }
        }
    }

    /**
     * Lógica de Autenticación con Microsoft (Azure):
     * Abre un navegador seguro para que el tutor valide su cuenta institucional.
     */
    private fun ejecutarLoginMicrosoft(onSuccess: (String) -> Unit) {
        // Configuramos el proveedor para Microsoft.
        val provider = OAuthProvider.newBuilder("microsoft.com")

        // Forzamos al sistema a pedir la cuenta cada vez por seguridad en entornos escolares.
        provider.addCustomParameter("prompt", "select_account")

        // Iniciamos la actividad de inicio de sesión gestionada por Firebase.
        auth.startActivityForSignInWithProvider(this, provider.build())
            .addOnSuccessListener { authResult ->
                // Tras el éxito, Firebase ya ha guardado la sesión automáticamente.
                val nombre = authResult.user?.displayName ?: "Usuario ABN"
                onSuccess(nombre)
            }
            .addOnFailureListener { e ->
                Log.e("AUTH_ERROR", "Fallo en el flujo de Microsoft: ${e.message}")
                Toast.makeText(this, "Error de conexión con Microsoft", Toast.LENGTH_LONG).show()
            }
    }
}