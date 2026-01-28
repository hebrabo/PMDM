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

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        enableEdgeToEdge()

        setContent {
            // --- PERSISTENCIA DE SESIÓN ---
            // Al arrancar, Firebase comprueba si hay un "token" guardado en el móvil.
            // Si existe, auth.currentUser no será nulo y entraremos directo.
            var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }
            var userName by remember { mutableStateOf(auth.currentUser?.displayName ?: "Tutor ABN") }

            // --- RECUPERACIÓN DE LOGIN PENDIENTE ---
            // Maneja el caso en que el usuario vuelve del navegador de Microsoft
            LaunchedEffect(Unit) {
                auth.pendingAuthResult?.addOnSuccessListener { authResult ->
                    userName = authResult.user?.displayName ?: "Tutor ABN"
                    isLoggedIn = true
                    Log.d("AUTH_PENDING", "Sesión recuperada tras retorno del navegador")
                }?.addOnFailureListener { e ->
                    Log.e("AUTH_PENDING", "Error al recuperar sesión: ${e.message}")
                }
            }

            if (!isLoggedIn) {
                RegisterScreen(
                    onLoginSuccess = { nombre ->
                        userName = nombre
                        isLoggedIn = true
                    },
                    onMicrosoftClick = {
                        ejecutarLoginMicrosoft { nombre ->
                            userName = nombre
                            isLoggedIn = true
                        }
                    }
                )
            } else {
                // Aquí se gestionará el acceso a los 100 juegos
                DashboardScreen(
                    userName = userName,
                    onLogout = {
                        auth.signOut() // Borra el token local para que pida login la próxima vez
                        isLoggedIn = false
                    }
                )
            }
        }
    }

    private fun ejecutarLoginMicrosoft(onSuccess: (String) -> Unit) {
        val provider = OAuthProvider.newBuilder("microsoft.com")
        provider.addCustomParameter("prompt", "select_account")

        auth.startActivityForSignInWithProvider(this, provider.build())
            .addOnSuccessListener { authResult ->
                val nombre = authResult.user?.displayName ?: "Usuario ABN"
                onSuccess(nombre)
            }
            .addOnFailureListener { e ->
                Log.e("AUTH_ERROR", "Error Microsoft: ${e.message}")
                Toast.makeText(this, "No se pudo conectar con Microsoft", Toast.LENGTH_LONG).show()
            }
    }
}