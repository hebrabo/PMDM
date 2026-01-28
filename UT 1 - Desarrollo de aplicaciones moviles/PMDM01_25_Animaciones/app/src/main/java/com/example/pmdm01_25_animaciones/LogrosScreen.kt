package com.example.pmdm01_25_animaciones

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarOutline // Necesita la librería material-icons-extended
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- IDENTIDAD VISUAL ABN ---
// Colores seleccionados para ser amigables para niños de 3 a 5 años y sus tutores.
val FondoVerde = Color(0xFF6A9B74)
val TextoVerdeOscuro = Color(0xFF4A614D)
val FondoTarjeta = Color(0xFFEBE3D5)
val EstrellasColor = Color(0xFFF29900)

/**
 * Modelo de datos para los logros.
 * Representa el progreso individual en cada una de las 100 actividades del proyecto.
 */
data class Logro(
    val titulo: String,
    val descCorta: String,
    val descLarga: String,
    val estrellas: Int,
    val esCompletado: Boolean
)

@Composable
fun LogrosScreen() {
    // --- DATOS SIMULADOS ---
    // En una fase posterior, estos datos vendrán de una base de datos vinculada a los 100 juegos.
    val logrosContar = remember {
        listOf(
            Logro("Distingue Muchos/Pocos", "Comparación visual básica.", "Hito fundamental: el niño identifica cantidades sin contar, base del sentido numérico ABN.", 3, true),
            Logro("Los Amigos del 10", "Parejas que suman diez.", "El niño visualiza descomposiciones del 10 usando materiales manipulativos.", 1, false),
            Logro("Subitización", "Reconocimiento rápido de puntos.", "Capacidad de decir cuántos elementos hay (hasta 5) de un solo vistazo.", 0, false),
            Logro("Recta Numérica", "Orden de los números 1-10.", "El niño entiende la posición de cada número y que el siguiente siempre es 'uno más'.", 2, true)
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Cabecera: Muestra el perfil del alumno (en este caso, Carmen).
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(FondoVerde)
                .padding(vertical = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Logros de Carmen", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }

        // LazyColumn: Lista eficiente que solo renderiza lo que se ve en pantalla.
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(logrosContar) { logro ->
                ItemLogro(logro)
            }
        }
    }
}

@Composable
fun ItemLogro(logro: Logro) {
    // --- ESTADO DE LA ANIMACIÓN ---
    // 'isExpanded' rastrea si esta tarjeta específica está abierta o cerrada.
    // Usamos 'remember' para que el estado persista durante las recomposiciones.
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            // --- NÚCLEO DE LA TAREA 5: ANIMACIÓN DE TAMAÑO ---
            // 'animateContentSize' detecta automáticamente el cambio de tamaño cuando aparece la descLarga.
            // Usamos 'spring' (muelle) para dar un efecto elástico muy adecuado para apps infantiles.
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .clickable { isExpanded = !isExpanded }, // Permite expandir clicando en cualquier parte de la tarjeta.
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // --- SISTEMA DE PUNTUACIÓN VISUAL ---
                // Dibuja 3 estrellas: se rellenan según el progreso del niño en la actividad.
                Row {
                    repeat(3) { i ->
                        Icon(
                            imageVector = if (i < logro.estrellas) Icons.Default.Star else Icons.Outlined.StarOutline,
                            contentDescription = null,
                            tint = if (i < logro.estrellas) EstrellasColor else Color.Gray.copy(alpha = 0.4f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(logro.titulo, fontWeight = FontWeight.Bold, color = TextoVerdeOscuro)
                    Text(logro.descCorta, fontSize = 12.sp, color = Color.Gray)
                }

                // Icono dinámico: Cambia su forma según el estado de expansión de la tarjeta.
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.Info,
                    contentDescription = null,
                    tint = TextoVerdeOscuro
                )
            }

            // --- CONTENIDO EXPANDIBLE ---
            // Solo se incluye en el árbol de la UI si 'isExpanded' es true.
            // Al activarse, 'animateContentSize' suaviza la aparición de este bloque.
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = logro.descLarga, // Explicación pedagógica para el tutor.
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = TextoVerdeOscuro
                )
            }
        }
    }
}