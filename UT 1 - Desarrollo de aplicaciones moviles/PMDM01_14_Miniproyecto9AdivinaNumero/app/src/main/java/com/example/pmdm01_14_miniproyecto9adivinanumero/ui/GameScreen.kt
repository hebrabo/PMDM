package com.example.pmdm01_14_miniproyecto9adivinanumero.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pmdm01_14_miniproyecto9adivinanumero.ui.GameViewModel

// -------------------------------------------------------------------------
// 1. GameScreen (STATEFUL - CON ESTADO)
// -------------------------------------------------------------------------
/**
 * Este es el "Container" o pantalla principal.
 *
 * RESPONSABILIDADES:
 * 1. Poseer el ViewModel .
 * 2. Observar el flujo de datos (StateFlow) y convertirlo a Estado de Compose.
 * 3. Implementar el patrón "State Hoisting" (Elevación de estado):
 * - Pasa datos hacia abajo a los hijos (GameLayout, GameStatus).
 * - Recibe eventos desde abajo (lambdas) y se los pasa al ViewModel.
 */
@Composable
fun GameScreen(
    gameViewModel: GameViewModel = viewModel()
) {
    // Convertimos el StateFlow del ViewModel en un estado observable por Compose.
    // 'by' nos permite usar 'gameUiState' directamente como objeto, sin .value
    val gameUiState by gameViewModel.uiState.collectAsState()

    // Estructura visual de la pantalla completa
    Column(
        modifier = Modifier
            .statusBarsPadding() // Respeta la barra de estado del sistema
            .verticalScroll(rememberScrollState()) // Permite scroll si la pantalla es pequeña
            .safeDrawingPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Adivina el Número",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // LLAMADA AL COMPONENTE A (Layout del Juego)
        // Le pasamos solo los datos que necesita y funciones para responder a eventos.
        GameLayout(
            mensajePista = gameUiState.mensajeEstado,
            intentoUsuario = gameUiState.intentoUsuario,
            esError = gameUiState.esError,
            // Eventos: Conectamos la UI con la Lógica del ViewModel
            onUserGuessChanged = { gameViewModel.actualizarIntentoUsuario(it) },
            onKeyboardDone = { gameViewModel.comprobarIntento() },
            onComprobar = { gameViewModel.comprobarIntento() },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 20.dp)
        )

        // LLAMADA AL COMPONENTE B (Estatus)
        GameStatus(
            intentos = gameUiState.numeroIntentos,
            modifier = Modifier.padding(20.dp)
        )

        // LÓGICA DE NAVEGACIÓN / DIÁLOGOS
        // Si el estado dice que el juego terminó, mostramos el Dialog sobre la UI.
        if (gameUiState.esJuegoTerminado) {
            FinalScoreDialog(
                intentos = gameUiState.numeroIntentos,
                numeroSecreto = gameUiState.numeroSecreto,
                onPlayAgain = { gameViewModel.reiniciarJuego() }
            )
        }
    }
}
// -------------------------------------------------------------------------
// 2. GameLayout (SIN ESTADO)
// -------------------------------------------------------------------------
/**
 * Solo sabe "Pintar datos" y "Avisar cuando tocan botones".
*/
@Composable
fun GameLayout(
    // Datos
    mensajePista: String,
    intentoUsuario: String,
    esError: Boolean,

    // Eventos
    onUserGuessChanged: (String) -> Unit, // Callback: El usuario escribió algo
    onKeyboardDone: () -> Unit, // Callback: El usuario dio a "Enter" en teclado
    onComprobar: () -> Unit, // Callback: El usuario pulsó el botón
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Etiqueta decorativa "PISTA" con fondo tintado
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                text = "PISTA",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )

            // El mensaje principal (Mayor, Menor, etc.)
            Text(
                text = mensajePista,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            // INPUT TEXTO (Campo de escritura)
            OutlinedTextField(
                // 1. Estado - Lo que se VE
                // En Compose, la caja no "almacena" el texto por sí sola.
                // Simplemente refleja el valor de esta variable. Si la variable está vacía, la caja se ve vacía.
                value = intentoUsuario,

                // 2. Evento - Lo que se HACE
                // Se ejecuta CADA VEZ que pulsamos una tecla.
                // Coge el nuevo texto y avisa al padre (ViewModel) para que actualice la variable 'intentoUsuario'.
                // Sin esto, podríamos aporrear el teclado y no aparecería nada.
                onValueChange = onUserGuessChanged,

                // 3. ETIQUETA INTELIGENTE
                // Es el texto flotante que explica qué escribir.
                // Usamos un 'if' para cambiar el mensaje dinámicamente:
                // - Si hay error: Muestra mensaje de advertencia.
                // - Si no: Muestra "Tu número".
                label = {
                    if (esError) {
                        Text("¡Escribe un número válido!")
                    } else {
                        Text("Tu número")
                    }
                },

                // 4. Indicador de Error
                // Si esta variable es 'true', el borde de la caja se pinta de ROJO automáticamente
                // para indicar visualmente al usuario que algo va mal.
                isError = esError,

                // 5. CONFIGURACIÓN DEL TECLADO
                keyboardOptions = KeyboardOptions.Default.copy(
                    // Obliga a que el móvil muestre solo el teclado NUMÉRICO (evita errores de letras).
                    keyboardType = KeyboardType.Number,
                    // Cambia la tecla "Enter" (esquina inferior derecha) por un icono de "Hecho" (Check/Done).
                    imeAction = ImeAction.Done
                ),

                // 6. ACCIÓN DEL TECLADO
                // Define qué ocurre cuando el usuario pulsa esa tecla "Hecho" que configuramos arriba.
                // Aquí llamamos a la función de comprobar, igual que si pulsara el botón físico.
                keyboardActions = KeyboardActions(
                    onDone = { onKeyboardDone() }
                ),

                // 7. ESTÉTICA
                singleLine = true,                   // Evita que la caja crezca hacia abajo (solo una línea).
                shape = MaterialTheme.shapes.medium, // Redondea las esquinas para que quede moderno.
                modifier = Modifier.fillMaxWidth()   // Estira la caja para ocupar todo el ancho disponible.
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onComprobar
            ) {
                Text(text = "Comprobar")
            }
        }
    }
}

// -------------------------------------------------------------------------
// 3. GameStatus (INTENTOS REALIZADOS)
// -------------------------------------------------------------------------
/**
 * Muestra información secundaria (contador de intentos).
 * Al separarlo, mantenemos el código del GameScreen limpio y modular.
 */
    @Composable
    fun GameStatus(intentos: Int, modifier: Modifier = Modifier) {
        Card(modifier = modifier) {
            Text(
                text = "Intentos realizados: $intentos",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
// -------------------------------------------------------------------------
// 3. UTILIDADES (EXTENSIONS)
// -------------------------------------------------------------------------
/**
 * IMPORTANTE: Solución al problema de ContextWrapper.
 *
 * En Compose, `LocalContext.current` no siempre devuelve directamente la `Activity`.
 * A menudo devuelve un `ContextWrapper` (por temas, Hilt, o librerías internas).
 * Si intentamos hacer un cast directo `as Activity`, la app crasheará.
 *
 * Esta función recursiva "desenvuelve" (unwrap) el contexto capa por capa
 * hasta encontrar la Activity real para poder llamar a `.finish()`.
 */
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this                     // ¿Eres la Activity? ¡Genial!
    is ContextWrapper -> baseContext.findActivity() // ¿Eres un envoltorio? Quítate y mira qué hay dentro.
    else -> null                            // No encontré nada.
}

// -------------------------------------------------------------------------
// 4. FinalScoreDialog (DIALOG)
// -------------------------------------------------------------------------
/**
 * Ventana emergente (AlertDialog) que bloquea la interacción hasta que se decida.
 */
@Composable
private fun FinalScoreDialog(
    intentos: Int, // El número para mostrar ("Ganaste en 5 intentos")
    numeroSecreto: Int, // El número secreto (para mostrar)
    onPlayAgain: () -> Unit, // La función a ejecutar si quiere jugar de nuevo
    modifier: Modifier = Modifier
) {
    // Obtenemos el contexto actual de la composición
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = {
            // Dejamos vacío para impedir que la ventana flotante se cierre si tocamos fuera de la ventana
        },
        title = { Text(text = "¡Felicidades!") },
        text = { Text(text = "Has adivinado el número $numeroSecreto en $intentos intentos.") },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    // USO DE LA UTILIDAD: Buscamos la Activity de forma segura y cerramos la App
                    context.findActivity()?.finish()
                }
            ) {
                Text(text = "Salir")
            }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = "Jugar otra vez")
            }
        }
    )
}

// -------------------------------------------------------------------------
// PREVIEW
// -------------------------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun GameScreenPreviewRefactor() {
    MaterialTheme {
        // En la preview, GameScreen creará internamente un ViewModel nuevo.
        // Como nuestro VM no tiene dependencias complejas (base de datos, red),
        // la preview funcionará correctamente sin configuración extra.
        GameScreen()
    }
}
