/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.unscramble.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unscramble.R
import com.example.unscramble.ui.theme.UnscrambleTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


@Composable
fun GameScreen(
    // Se pasa el ViewModel al Composable.
    // Si no se proporciona, se crea uno por defecto usando la función viewModel().
    gameViewModel: GameViewModel = viewModel()
) {
    // Observa el estado de la UI desde el StateFlow expuesto por el ViewModel.
    // collectAsState() convierte el StateFlow en un State de Compose que se actualiza automáticamente
    // cuando el StateFlow emite un nuevo valor, provocando la recomposición de los Composables que lo usan.
    val gameUiState by gameViewModel.uiState.collectAsState()

    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding()
            .padding(mediumPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.app_name),
            style = typography.titleLarge,
        )

        GameLayout(
            // Pasa la palabra desordenada actual desde el ViewModel al Composable GameLayout.
            // Cada vez que gameUiState.currentScrambledWord cambie, GameLayout se recompondrá automáticamente.
            currentScrambledWord = gameUiState.currentScrambledWord,
            userGuess = gameViewModel.userGuess, // Valor actual del intento del usuario
            wordCount = gameUiState.currentWordCount, // Pasa la cantidad de palabras que lleva completadas el jugador.
            onUserGuessChanged = { gameViewModel.updateUserGuess(it) }, // Actualiza el intento en ViewModel
            onKeyboardDone = { gameViewModel.checkUserGuess() }, // Verifica intento al presionar Done
            isGuessWrong = gameUiState.isGuessedWordWrong, // Indica si el intento es incorrecto
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(mediumPadding)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding),
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Botón Submit en GameScreen que envía el intento del usuario al ViewModel
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { gameViewModel.checkUserGuess() } // Llama a checkUserGuess al hacer clic
            ) {
                Text(
                    text = stringResource(R.string.submit),
                    fontSize = 16.sp
                )
            }

            OutlinedButton(
                // Al hacer clic en "Skip", se salta la palabra actual y se genera una nueva
                onClick = { gameViewModel.skipWord() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.skip),
                    fontSize = 16.sp
                )
            }
        }

        GameStatus(score = gameUiState.score, modifier = Modifier.padding(20.dp))
    }
}

@Composable
// Muestra la puntuación actual obtenida por el jugador.
// Se actualiza automáticamente cuando uiState.score cambia.
fun GameStatus(score: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.score, score),
            style = typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
// Composable GameLayout que recibe la palabra desordenada actual, el intento del usuario
// y callbacks para actualizar el intento y manejar la acción "Done" del teclado
fun GameLayout(
    currentScrambledWord: String, // Recibe la palabra desordenada actual como argumento para mostrarla en la UI
    isGuessWrong: Boolean, // Indica si el intento anterior fue incorrecto
    userGuess: String, // Texto ingresado por el usuario
    onUserGuessChanged: (String) -> Unit, // Callback para actualizar userGuess en ViewModel
    onKeyboardDone: () -> Unit, // Callback al presionar "Done" en el teclado
    wordCount: Int, // Número de palabra actual que se está jugando

    modifier: Modifier = Modifier
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(mediumPadding)
        ) {
            Text(
                modifier = Modifier
                    .clip(shapes.medium)
                    .background(colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(alignment = Alignment.End),
                // Muestra el contador de palabras usando el recurso string con formato
                text = stringResource(R.string.word_count, wordCount),
                style = typography.titleMedium,
                color = colorScheme.onPrimary
            )
            // Muestra la palabra desordenada en la parte superior del layout
            Text(
                text = currentScrambledWord, // Muestra la palabra desordenada en la parte superior del layout
                fontSize = 45.sp,
                modifier = modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(R.string.instructions),
                textAlign = TextAlign.Center,
                style = typography.titleMedium
            )
            // Campo de texto para que el usuario ingrese su intento
            // usa isGuessWrong para mostrar error visual
            OutlinedTextField(
                value = userGuess, // Observa el valor de userGuess desde ViewModel
                singleLine = true,
                shape = shapes.large,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface,
                ),
                onValueChange = onUserGuessChanged, // Cada cambio actualiza userGuess en ViewModel
                label = {
                    if (isGuessWrong) {
                        Text(stringResource(R.string.wrong_guess)) // Etiqueta de error
                    } else {
                        Text(stringResource(R.string.enter_your_word)) // Etiqueta normal
                    }
                },
                isError = isGuessWrong, // Cambia el color y estilo si el intento es incorrecto
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { }
                )
            )
        }
    }
}

/*
 * Creates and shows an AlertDialog with final score.
 */
@Composable
private fun FinalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
        },
        title = { Text(text = stringResource(R.string.congratulations)) },
        text = { Text(text = stringResource(R.string.you_scored, score)) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text(text = stringResource(R.string.exit))
            }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = stringResource(R.string.play_again))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    UnscrambleTheme {
        GameScreen()
    }
}
