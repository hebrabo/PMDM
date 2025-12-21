package com.example.pmdm01_14_miniproyecto9adivinanumero.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class GameViewModel : ViewModel() {

    // ------------------------------------------------------------------------
    // ESTADO: Versión Editable (Privada) vs Versión Solo Lectura (Pública)
    // ------------------------------------------------------------------------

    // Es una variable privada y editable (Mutable) que guarda el estado del juego.
    // En el diagrama, _uiState está DENTRO del bloque azul que dice "ViewModel".
    // Se inicializa con los valores por defecto (GameUiState())
    // y es la herramienta que usa el ViewModel para escribir los cambios cada vez que ocurre algo en el juego.
    private val _uiState = MutableStateFlow(GameUiState())

    // Esta variable corresponde a la flecha hacia abajo del diagrama.
    // Es una variable publica de tipo State(siempre tiene el valor actual)+flow(si el valor cambia envia el nuevo dato).
    // GameUiState(es el tipo de dato que se envia).
    // El valor del dato es el valor de la variable _uiState(que está guardada en el ViewModel)
    // pero en formato solo lectura (.asStateFlow).
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    //init (Initialization) NO es una función, es una palabra reservada de Kotlin.
    // Es un bloque de código que se ejecuta automáticamente
    // en el momento en que se crea una instancia de la clase (es decir, cuando nace el ViewModel).
    init {
        reiniciarJuego()
    }

    // ------------------------------------------------------------------------
    // FUNCIONES
    // ------------------------------------------------------------------------

    /**
     * Esta función Se llama cada vez que el usuario escribe una letra/número en el TextField.
     * Esta función se llama EN TIEMPO REAL, letra a letra, mientras el usuario escribe.
     * * IMPORTANTE: Esto significa que la variable 'intentoUsuario' se actualiza
     * ANTES de que el usuario pulse el botón "Comprobar".
     * Cuando pulse el botón, el dato ya estará guardado y listo para validarse.
     * Usamos .update para leer el estado actual de forma segura.
     * Usamos .copy para crear un NUEVO estado idéntico al anterior,
     * pero modificando solo el texto del usuario y quitando el error.
     * (Recordar: En MVVM no modificamos datos viejos, creamos datos nuevos).
     */
    fun actualizarIntentoUsuario(intento: String) {
        _uiState.update { currentState ->
            currentState.copy(
                intentoUsuario = intento,
                esError = false
            )
        }
    }

    /**
     * Verifica si el número es válido y lo compara con el secreto.
     */
    fun comprobarIntento() {
        val intentoStr = _uiState.value.intentoUsuario
        // Intentamos convertir a número. Si falla (null), es que no era un número válido.
        val intentoNum = intentoStr.toIntOrNull()

        if(intentoNum == null) {
            _uiState.update {
                it.copy(
                    esError = true,
                    mensajeEstado = "Por favor, introduce un número válido"
                )}
            return // Salimos de la función sin contar el intento
        }

        _uiState.update { currentState ->
            val secreto = currentState.numeroSecreto
            val nuevosIntentos = currentState.numeroIntentos + 1

            when {
                // Caso: Adivinó
                intentoNum == secreto -> {
                    currentState.copy(
                        numeroIntentos = nuevosIntentos,
                        mensajeEstado = "¡Correcto! El número era $secreto",
                        esJuegoTerminado = true,
                        intentoUsuario = "" // Limpiamos el campo
                    )
                }
                // Caso: El número ingresado es menor al secreto (Pista: el secreto es mayor)
                intentoNum < secreto -> {
                    currentState.copy(
                        numeroIntentos = nuevosIntentos,
                        mensajeEstado = "El número es MAYOR que $intentoNum",
                        intentoUsuario = ""
                    )
                }
                // Caso: El número ingresado es mayor al secreto
                else -> { // intentoNum > secreto
                    currentState.copy(
                        numeroIntentos = nuevosIntentos,
                        mensajeEstado = "El número es MENOR que $intentoNum",
                        intentoUsuario = ""
                    )
                }
            }
        }
    }

    /**
     * Función que resetea el juego a su estado inicial.
     * Genera un nuevo número aleatorio y limpia los contadores.
     * _uiState.value = ... significa: "Toma este estado nuevo,
     * guárdalo en la caja (caja UIstate del diagrama) y
     * avisa inmediatamente a la pantalla para que se repinte con estos nuevos valores".
     */
    fun reiniciarJuego() {
        // Asignamos un nuevo estado completo
        _uiState.value = GameUiState(
            numeroSecreto = Random.nextInt(1, 101),
            mensajeEstado = "Adivina el número entre 1 y 100",
            numeroIntentos = 0,
            intentoUsuario = "",
            esJuegoTerminado = false
        )
    }
}