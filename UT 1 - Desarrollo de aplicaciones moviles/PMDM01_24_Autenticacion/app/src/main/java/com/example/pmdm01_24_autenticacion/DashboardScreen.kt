package com.example.pmdm01_24_autenticacion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla Principal (Dashboard)
 * * Esta pantalla es el destino tras una autenticación exitosa.
 * Sirve como centro de control para el tutor de niños de 3 a 5 años.
 * * @param userName Nombre recuperado de Firebase Auth (Google, Microsoft o Email).
 * @param onLogout Función que gestiona el cierre de sesión y vuelve a la pantalla de registro.
 */
@Composable
fun DashboardScreen(userName: String, onLogout: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6A9B74)), // FondoVerde: Mantenemos la identidad visual ABN.
        contentAlignment = Alignment.Center
    ) {
        Card(
            // FondoTarjeta: Color crema suave.
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEBE3D5)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp) // Añadimos elevación para dar profundidad.
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // PERSONALIZACIÓN: Saludamos al usuario por su nombre real.
                // Esto confirma que la persistencia de datos de Firebase ha funcionado.
                Text(
                    text = "¡Bienvenida, $userName!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A614D), // TextoVerdeOscuro
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "a la app Cálculo ABN infantil",
                    fontSize = 18.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Este espacio está reservado para la cuadrícula
                // que mostrará los juegos y actividades de cálculo.
                Text(
                    text = "Desde aquí podrás gestionar los logros y juegos de tus alumnos de 3 a 5 años.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                // BOTÓN DE CIERRE DE SESIÓN:
                // Al pulsar aquí, el Activity ejecutará 'auth.signOut()', rompiendo la persistencia
                // y obligando a pasar de nuevo por el login por seguridad.
                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF29900)), // BotonNaranja
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Cerrar Sesión",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}