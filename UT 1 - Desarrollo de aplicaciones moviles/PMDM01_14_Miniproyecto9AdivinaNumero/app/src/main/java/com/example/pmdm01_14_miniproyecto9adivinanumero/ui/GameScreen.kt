package com.example.pmdm01_14_miniproyecto9adivinanumero.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pmdm01_14_miniproyecto9adivinanumero.ui.GameViewModel

@Composable
fun GuessNumberScreen(vm: GameViewModel = viewModel()) {

    // Estado local para almacenar la entrada del usuario
    var userInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center, // Centra verticalmente
        horizontalAlignment = Alignment.CenterHorizontally, // Centra horizontalmente
    ) {

        // Column interna con separación uniforme entre elementos
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            // Título del juego
            Text(
                text = "Bienvenido al juego de:\nAdivina el número (1 - 100)",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = Gray

            )

            // Campo de texto para introducir el número
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { Text("Introduce un número") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Botón para probar la suposición
            Button(
                onClick = {
                    val guess = userInput.toIntOrNull() // Convertir a entero
                    if (guess != null) {
                        vm.checkGuess(guess) // Verificar el número con el ViewModel
                        userInput = "" // Limpiar input
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Probar")
            }

            // Mostrar mensaje de pista o acierto
            Text(
                text = vm.message.value,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(100.dp))

            // Botón para reiniciar el juego
            Button(
                onClick = {
                    vm.resetGame() // Genera nuevo número secreto
                    userInput = ""  // Limpiar input
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reiniciar juego")
            }
        }
    }
}
