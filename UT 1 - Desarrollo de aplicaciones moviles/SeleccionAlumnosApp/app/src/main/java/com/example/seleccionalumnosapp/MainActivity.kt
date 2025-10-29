package com.example.seleccionalumnosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.seleccionalumnosapp.ui.theme.SeleccionAlumnosAppTheme
import kotlin.random.Random

// Clase Alumno (similar a Tareas)
class Alumno(val id: Int, val nombre: String, var seleccionado: Boolean = false) {
    fun marcarComoSeleccionado() {
        seleccionado = true
    }

    fun mostrarInfo(): String {
        val estado = if (seleccionado) "[✔]" else "[ ]"
        return "$estado $id - $nombre"
    }
}

// Clase ListaAlumnos (similar a ListaTareas)
class ListaAlumnos {
    private val alumnos: MutableList<Alumno> = mutableListOf()

    fun agregarAlumno(alumno: Alumno) {
        alumnos.add(alumno)
        println("Alumno agregado: ${alumno.nombre}")
    }

    fun mostrarAlumnos(): List<String> {
        return alumnos.map { it.mostrarInfo() }
    }

    fun seleccionarAlumno(): Alumno? {
        val restantes = alumnos.filter { !it.seleccionado }
        if (restantes.isEmpty()) {
            // Todos han sido seleccionados → reiniciar
            alumnos.forEach { it.seleccionado = false }
        }
        val nuevo = alumnos.filter { !it.seleccionado }.random()
        nuevo.marcarComoSeleccionado()
        return nuevo
    }

    fun reset() {
        alumnos.forEach { it.seleccionado = false }
    }

    fun obtenerAlumnos(): List<Alumno> = alumnos
}

// Composable principal
@Composable
fun SeleccionAlumnosApp() {
    // Crear la lista de alumnos
    val listaAlumnos = remember { ListaAlumnos() }

    // Inicializar alumnos si la lista está vacía
    if (listaAlumnos.obtenerAlumnos().isEmpty()) {
        val nombres = listOf(
            "Ana Martínez", "Luis González", "Carlos Pereira",
            "Marta López", "Sofía Martín", "Mireia Santos", "Pedro Ruiz",
            "Laura Fernández", "David Ortega", "Lucía Gómez", "Javier Ruiz"
        )
        nombres.forEachIndexed { index, nombre ->
            listaAlumnos.agregarAlumno(Alumno(index + 1, nombre))
        }
    }

    // Estado para forzar recomposición al seleccionar o reiniciar
    var dummy by remember { mutableStateOf(0) }

    // Estado para el scroll
    val scrollState = rememberScrollState()

    // Composición de la UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Lista de alumnos
        listaAlumnos.mostrarAlumnos().forEach { info ->
            Text(
                text = info,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Alumno seleccionado
        Text(
            text = "Alumno seleccionado: ${listaAlumnos.obtenerAlumnos().find { it.seleccionado }?.nombre ?: "-"}",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Botones
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {
                listaAlumnos.seleccionarAlumno()
                dummy++ // fuerza recomposición
            }) {
                Text("Seleccionar alumno")
            }

            Button(onClick = {
                listaAlumnos.reset()
                dummy++
            }) {
                Text("Reiniciar")
            }
        }
    }
}

// Actividad principal
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeleccionAlumnosAppTheme(darkTheme = false, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SeleccionAlumnosApp()
                }
            }
        }
    }
}






