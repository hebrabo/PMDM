package com.example.pmdm01_14_miniproyecto9adivinanumero

import com.example.pmdm01_14_miniproyecto9adivinanumero.ui.GameViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GuessNumberViewModelTest {

    private lateinit var viewModel: GameViewModel

    @Before
    fun setup() {
        // Inicializamos el ViewModel antes de cada test
        viewModel = GameViewModel()
    }

    @Test
    fun comprobarIntento_siElNumeroEsMenor_muestraMensajeMayor() {
        // 1. Miramos "con rayos X" qué número secreto se ha generado aleatoriamente
        val secretoReal = viewModel.uiState.value.numeroSecreto

        // 2. Calculamos un intento que sea MENOR (para que el juego diga "Es MAYOR")
        // (Si el secreto es 1, usamos 0 para evitar errores, aunque el rango sea 1-100)
        val intentoMenor = if (secretoReal > 1) secretoReal - 1 else 0

        // 3. Simulamos que el usuario escribe y pulsa comprobar
        viewModel.actualizarIntentoUsuario(intentoMenor.toString())
        viewModel.comprobarIntento()

        // 4. Verificamos que el mensaje contiene la pista correcta
        val mensajeActual = viewModel.uiState.value.mensajeEstado
        assertTrue("El mensaje debería decir que es MAYOR", mensajeActual.contains("MAYOR"))
    }

    @Test
    fun comprobarIntento_siElNumeroEsMayor_muestraMensajeMenor() {
        // 1. Miramos el secreto
        val secretoReal = viewModel.uiState.value.numeroSecreto

        // 2. Calculamos un intento que sea MAYOR
        val intentoMayor = secretoReal + 1

        // 3. Jugamos
        viewModel.actualizarIntentoUsuario(intentoMayor.toString())
        viewModel.comprobarIntento()

        // 4. Verificamos
        val mensajeActual = viewModel.uiState.value.mensajeEstado
        assertTrue("El mensaje debería decir que es MENOR", mensajeActual.contains("MENOR"))
    }

    @Test
    fun comprobarIntento_siAcierta_juegoTerminado() {
        // 1. Miramos el secreto
        val secretoReal = viewModel.uiState.value.numeroSecreto

        // 2. Hacemos trampa y ponemos el número exacto
        viewModel.actualizarIntentoUsuario(secretoReal.toString())
        viewModel.comprobarIntento()

        // 3. Verificamos que el juego ha marcado "Terminado"
        assertTrue(viewModel.uiState.value.esJuegoTerminado)
    }

    @Test
    fun error_siEscribimosTextoNoNumerico() {
        // 1. Escribimos letras
        viewModel.actualizarIntentoUsuario("hola")
        viewModel.comprobarIntento()

        // 2. Verificamos que se activa el flag de error
        assertTrue(viewModel.uiState.value.esError)
        assertEquals("Por favor, introduce un número válido", viewModel.uiState.value.mensajeEstado)
    }

    @Test
    fun reiniciarJuego_reseteaValores() {
        // 1. Jugamos una vez para "ensuciar" el estado
        viewModel.actualizarIntentoUsuario("50")
        viewModel.comprobarIntento()

        // Guardamos el secreto antiguo para ver si cambia
        val secretoAntiguo = viewModel.uiState.value.numeroSecreto

        // 2. Reiniciamos
        viewModel.reiniciarJuego()

        // 3. Comprobamos que todo está limpio
        val estadoNuevo = viewModel.uiState.value

        assertEquals(0, estadoNuevo.numeroIntentos)
        assertEquals("", estadoNuevo.intentoUsuario)
        assertFalse(estadoNuevo.esJuegoTerminado)

        // NOTA: Existe una probabilidad minúscula de que el nuevo random sea igual al anterior.
        // Pero para este test básico asumimos que cambió o al menos que el estado se limpió.
        assertEquals("Adivina el número entre 1 y 100", estadoNuevo.mensajeEstado)
    }

    @Test
    fun boundary_limitInferior_esValido() {
        // Probamos el 1. No nos importa si gana o pierde,
        // solo que NO de error de formato y que cuente el intento.
        viewModel.actualizarIntentoUsuario("1")
        viewModel.comprobarIntento()

        val uiState = viewModel.uiState.value

        // Verificamos que NO es un error (es decir, lo acepta como número válido)
        assertFalse("El sistema debería aceptar el 1 como válido", uiState.esError)

        // Verificamos que el contador ha subido a 1
        assertEquals(1, uiState.numeroIntentos)
    }

    @Test
    fun boundary_limitSuperior_esValido() {
        // Probamos el 100.
        viewModel.actualizarIntentoUsuario("100")
        viewModel.comprobarIntento()

        val uiState = viewModel.uiState.value

        assertFalse("El sistema debería aceptar el 100 como válido", uiState.esError)
        assertEquals(1, uiState.numeroIntentos)
    }
}