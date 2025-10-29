package com.example.seleccionalumnosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.seleccionalumnosapp.ui.theme.SeleccionAlumnosAppTheme
import androidx.compose.runtime.LaunchedEffect
import kotlin.random.Random

// Clase Alumno
class Alumno(val id: Int, val nombre: String, var seleccionado: Boolean = false) {
    fun marcarComoSeleccionado() {
        seleccionado = true
    }
}

// Clase ListaAlumnos
class ListaAlumnos {
    private val alumnos: MutableList<Alumno> = mutableListOf()

    fun agregarAlumno(alumno: Alumno) {
        alumnos.add(alumno)
    }

    fun seleccionarAlumno(): Alumno {
        val restantes = alumnos.filter { !it.seleccionado }
        if (restantes.isEmpty()) {
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
    val listaAlumnos = remember { ListaAlumnos() }

    // Inicializar alumnos
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

    var indiceActual by remember { mutableStateOf<Int?>(null) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Parte fija: texto + botones
        val alumnoSeleccionado = indiceActual?.let { listaAlumnos.obtenerAlumnos()[it] }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Alumno seleccionado:", color = Color.Black)
            Text(
                text = alumnoSeleccionado?.nombre ?: "-",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = {
                    val nuevo = listaAlumnos.seleccionarAlumno()
                    indiceActual = listaAlumnos.obtenerAlumnos().indexOf(nuevo)
                }) {
                    Text("Seleccionar alumno")
                }

                Button(onClick = {
                    listaAlumnos.reset()
                    indiceActual = null
                }) {
                    Text("Reiniciar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Parte scrollable: lista de alumnos
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            listaAlumnos.obtenerAlumnos().forEachIndexed { index, alumno ->
                val colorFondo = when {
                    indiceActual == index -> Color(0xFF00C853) // verde actual
                    alumno.seleccionado -> Color(0xFFD50000) // rojo ya seleccionado
                    else -> Color(0xFF333333) // gris no seleccionado
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(colorFondo)
                        .padding(12.dp)
                ) {
                    Text(text = alumno.nombre, color = Color.White)
                }
            }
        }
    }

    // Autoscroll al alumno seleccionado
    LaunchedEffect(indiceActual) {
        indiceActual?.let { scrollState.animateScrollTo(it * 60) }
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







