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

// -----------------------------
// CLASE Alumno
// -----------------------------
// Representa a cada alumno con un id, nombre y estado de selección
class Alumno(val id: Int, val nombre: String, var seleccionado: Boolean = false) {

    // Marca el alumno como seleccionado
    fun marcarComoSeleccionado() {
        seleccionado = true
    }
}

// -----------------------------
// CLASE ListaAlumnos
// -----------------------------
// Gestiona la lista de alumnos y sus operaciones
class ListaAlumnos {

    // Lista mutable de alumnos (ampliable)
    private val alumnos: MutableList<Alumno> = mutableListOf()

    // Agrega un nuevo alumno a la lista
    fun agregarAlumno(alumno: Alumno) {
        alumnos.add(alumno)
    }

    // Selecciona un alumno al azar entre los no seleccionados
    fun seleccionarAlumno(): Alumno {
        // Filtra los alumnos que aún no han sido seleccionados
        val restantes = alumnos.filter { !it.seleccionado }

        // Si todos han sido seleccionados, se reinician
        if (restantes.isEmpty()) {
            alumnos.forEach { it.seleccionado = false }
        }

        // Escoge un alumno aleatorio entre los que quedan y lo marca como seleccionado
        val nuevo = alumnos.filter { !it.seleccionado }.random()
        nuevo.marcarComoSeleccionado()
        return nuevo
    }

    // Reinicia el estado de todos los alumnos
    fun reset() {
        alumnos.forEach { it.seleccionado = false }
    }

    // Devuelve la lista de alumnos
    fun obtenerAlumnos(): List<Alumno> = alumnos
}

// -----------------------------
// COMPOSABLE PRINCIPAL
// -----------------------------
// Interfaz de usuario de la aplicación
@Composable
fun SeleccionAlumnosApp() {
    // Se crea y recuerda la lista de alumnos
    val listaAlumnos = remember { ListaAlumnos() }

    // Si la lista está vacía, se inicializa con nombres predefinidos
    if (listaAlumnos.obtenerAlumnos().isEmpty()) {
        val nombres = listOf(
            "Ana Martínez", "Luis González", "Carlos Pereira",
            "Marta López", "Sofía Martín", "Mireia Santos", "Pedro Ruiz",
            "Laura Fernández", "David Ortega", "Lucía Gómez", "Javier Ruiz"
        )
        // Se agregan los alumnos con un id incremental
        nombres.forEachIndexed { index, nombre ->
            listaAlumnos.agregarAlumno(Alumno(index + 1, nombre))
        }
    }

    // Estado para guardar el índice del alumno actualmente seleccionado
    var indiceActual by remember { mutableStateOf<Int?>(null) }

    // Controla el desplazamiento (scroll) de la lista
    val scrollState = rememberScrollState()

    // Contenedor principal en columna (vertical)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // -----------------------------
        // SECCIÓN FIJA (no se desplaza)
        // -----------------------------

        // Obtiene el alumno actualmente seleccionado, si existe
        val alumnoSeleccionado = indiceActual?.let { listaAlumnos.obtenerAlumnos()[it] }

        // Muestra el nombre del alumno seleccionado y los botones
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Alumno seleccionado:", color = Color.Black)
            Text(
                text = alumnoSeleccionado?.nombre ?: "-", // Muestra "-" si no hay ninguno
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Fila con los dos botones
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Botón para seleccionar un alumno aleatorio
                Button(onClick = {
                    val nuevo = listaAlumnos.seleccionarAlumno()
                    indiceActual = listaAlumnos.obtenerAlumnos().indexOf(nuevo)
                }) {
                    Text("Seleccionar alumno")
                }

                // Botón para reiniciar el estado de selección
                Button(onClick = {
                    listaAlumnos.reset()
                    indiceActual = null
                }) {
                    Text("Reiniciar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -----------------------------
        // SECCIÓN SCROLLABLE (lista)
        // -----------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // ocupa el espacio restante
                .verticalScroll(scrollState)
        ) {
            // Recorre y muestra cada alumno con color según su estado
            listaAlumnos.obtenerAlumnos().forEachIndexed { index, alumno ->
                val colorFondo = when {
                    indiceActual == index -> Color(0xFF00C853) // Verde → alumno actual
                    alumno.seleccionado -> Color(0xFFD50000) // Rojo → ya salió antes
                    else -> Color(0xFF333333) // Gris → no seleccionado
                }

                // Caja para cada alumno
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

    // -----------------------------
    // EFECTO DE AUTOSCROLL
    // -----------------------------
    // Cuando cambia el alumno seleccionado, desplaza la lista automáticamente
    LaunchedEffect(indiceActual) {
        indiceActual?.let { scrollState.animateScrollTo(it * 60) }
    }
}

// -----------------------------
// ACTIVIDAD PRINCIPAL (MainActivity)
// -----------------------------
// Configura la pantalla y aplica el tema visual
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeleccionAlumnosAppTheme(darkTheme = false, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llama al composable principal
                    SeleccionAlumnosApp()
                }
            }
        }
    }
}







