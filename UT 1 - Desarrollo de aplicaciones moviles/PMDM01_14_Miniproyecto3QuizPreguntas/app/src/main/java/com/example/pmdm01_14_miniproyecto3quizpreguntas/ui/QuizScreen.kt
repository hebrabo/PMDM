package com.example.pmdm01_14_miniproyecto3quizpreguntas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pmdm01_14_miniproyecto3quizpreguntas.model.Question
import com.example.pmdm01_14_miniproyecto3quizpreguntas.viewModel.QuizViewModel

// -----------------------------------------------------------------------------
// Pantalla principal del juego. Decide cuál UI mostrar:
//
//  - Si el juego terminó → pantalla de resultados
//  - Si no → pantalla de pregunta
//
// Esta función NO contiene lógica del juego; solo lee el estado del ViewModel.
// -----------------------------------------------------------------------------
@Composable
fun QuizScreen(viewModel: QuizViewModel = viewModel()) {
    // La UI decide qué mostrar basándose en el estado del ViewModel
    if (viewModel.isGameOver) {
        // Mostrar pantalla final cuando no hay más preguntas
        ResultScreen(
            score = viewModel.score,
            total = viewModel.totalQuestions,
            onRestart = { viewModel.resetQuiz() }
        )
    } else {
        // Mostrar una pregunta
        QuestionScreen(
            question = viewModel.currentQuestion,
            currentProgress = viewModel.currentQuestionIndex + 1,
            totalQuestions = viewModel.totalQuestions,
            onAnswerSelected = { index -> viewModel.submitAnswer(index) }
        )
    }
}

// -----------------------------------------------------------------------------
// Pantalla que muestra una pregunta y sus opciones.
//
// El usuario pulsa una opción → onAnswerSelected(index)
// -----------------------------------------------------------------------------
@Composable
fun QuestionScreen(
    question: Question,
    currentProgress: Int,
    totalQuestions: Int,
    onAnswerSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Progreso del quiz
        Text(
            text = "Pregunta $currentProgress de $totalQuestions",
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Texto de la pregunta
        Text(
            text = question.text,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Botones para las opciones
        question.options.forEachIndexed { index, optionText ->
            Button(
                onClick = { onAnswerSelected(index) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = optionText)
            }
        }
    }
}

// -----------------------------------------------------------------------------
// Pantalla final con la puntuación y botón para reiniciar el juego.
// -----------------------------------------------------------------------------
@Composable
fun ResultScreen(score: Int, total: Int, onRestart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¡Juego Terminado!",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tu puntuación: $score / $total",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onRestart) {
            Text("Jugar de nuevo")
        }
    }
}