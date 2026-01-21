package com.example.abntutorpanel.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorScreen(
    modifier: Modifier = Modifier,
    tutorViewModel: TutorViewModel = viewModel(factory = TutorViewModel.Factory)
) {
    // 1. Estado y Contexto
    val uiState by tutorViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 2. Navegación y Seguridad
    var isTeacherMode by remember { mutableStateOf(false) }
    var showParentalGate by remember { mutableStateOf(false) }
    var parentalAnswer by remember { mutableStateOf("") }
    val challenge = remember { "8 + 7" }
    val correctAnswer = "15"

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (isTeacherMode) "PANEL DEL PROFESOR" else "ZONA DE JUEGOS ABN") },
                actions = {
                    IconButton(onClick = {
                        if (isTeacherMode) isTeacherMode = false
                        else showParentalGate = true
                    }) {
                        Icon(
                            imageVector = if (isTeacherMode) Icons.Default.Person else Icons.Default.Lock,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = modifier.fillMaxSize().padding(padding).padding(16.dp)) {

            if (isTeacherMode) {
                // VISTA DE ADMINISTRACIÓN (TUTOR)
                TeacherPanel(
                    uiState = uiState,
                    onToggleRestricted = { tutorViewModel.toggleRestrictedMode(it) },
                    onAddMission = { t, d, g -> tutorViewModel.addMission(t, d, g) }
                )
            } else {
                // VISTA DE JUEGO (NIÑO 3-5 AÑOS) [cite: 2026-01-06]
                ChildView(
                    uiState = uiState,
                    onGameSelected = { gameId ->
                        Toast.makeText(context, "Abriendo actividad ABN nº $gameId...", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        // --- CONTROL PARENTAL ---
        if (showParentalGate) {
            AlertDialog(
                onDismissRequest = { showParentalGate = false },
                title = { Text("Acceso Restringido") },
                text = {
                    Column {
                        Text("Resuelve para entrar al panel:")
                        Text(text = challenge, style = MaterialTheme.typography.headlineMedium)
                        OutlinedTextField(
                            value = parentalAnswer,
                            onValueChange = { parentalAnswer = it },
                            label = { Text("Resultado") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (parentalAnswer == correctAnswer) {
                            isTeacherMode = true
                            showParentalGate = false
                            parentalAnswer = ""
                        }
                    }) { Text("Entrar") }
                },
                dismissButton = {
                    TextButton(onClick = { showParentalGate = false }) { Text("Cancelar") }
                }
            )
        }
    }
}

@Composable
fun TeacherPanel(
    uiState: TutorUiState,
    onToggleRestricted: (Boolean) -> Unit,
    onAddMission: (String, String, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var gId by remember { mutableStateOf("") }

    Text("Gestión de Misiones", style = MaterialTheme.typography.titleLarge)

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Modo Restringido (Solo Misiones)", Modifier.weight(1f))
            Switch(checked = uiState.isRestrictedMode, onCheckedChange = onToggleRestricted)
        }
    }

    Spacer(Modifier.height(8.dp))
    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Nombre de la misión") }, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Instrucciones") }, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(value = gId, onValueChange = { gId = it }, label = { Text("ID del Juego (1-100)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

    Button(
        onClick = {
            if (title.isNotBlank() && gId.isNotBlank()) {
                onAddMission(title, desc, gId.toIntOrNull() ?: 1)
                title = ""; desc = ""; gId = ""
            }
        },
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
    ) { Text("Guardar Misión en Room") }
}

@Composable
fun ChildView(
    uiState: TutorUiState,
    onGameSelected: (Int) -> Unit
) {
    if (uiState.isRestrictedMode) {
        // Modo Restringido: El niño solo ve las misiones de Room [cite: 2026-01-06]
        Text("Tus tareas de hoy:", style = MaterialTheme.typography.headlineSmall)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 8.dp)) {
            items(uiState.missionList) { mission ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onGameSelected(mission.gameId) }
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(50.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("${mission.gameId}", color = Color.White, style = MaterialTheme.typography.titleLarge)
                            }
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(mission.title, style = MaterialTheme.typography.titleMedium)
                            if (mission.description.isNotBlank()) Text(mission.description, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    } else {
        // Juego Libre: Los 100 juegos de ABN [cite: 2026-01-06, 2026-01-14]
        Text("¡Elige un juego!", style = MaterialTheme.typography.headlineSmall)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(100) { index ->
                val gameId = index + 1
                // Se pueden implementar juegos bloqueados/desbloqueados [cite: 2026-01-14]
                Card(
                    modifier = Modifier.padding(8.dp).height(110.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    onClick = { onGameSelected(gameId) }
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("Actividad $gameId", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}