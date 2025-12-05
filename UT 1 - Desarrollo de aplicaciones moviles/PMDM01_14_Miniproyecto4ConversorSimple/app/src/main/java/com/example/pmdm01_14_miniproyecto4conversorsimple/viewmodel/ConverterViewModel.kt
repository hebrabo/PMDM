package com.example.pmdm01_14_miniproyyecto4conversorsimple.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Locale

class ConverterViewModel : ViewModel() {

    /*
     * Valor introducido por el usuario en el campo de texto.
     * mutableStateOf hace que Compose se recomponga cuando cambia.
     */
    var inputText by mutableStateOf("")
        private set

    /*
     * Tipo de conversión:
     * true  -> Celsius → Fahrenheit
     * false -> Fahrenheit → Celsius
     */
    var isCelsiusToFahrenheit by mutableStateOf(true)
        private set

    /*
     * Texto del resultado final convertido.
     */
    var resultText by mutableStateOf("")
        private set

    /*
     * Se ejecuta cuando el usuario escribe algo nuevo.
     * Guardamos el texto y recalculamos automáticamente.
     */
    fun onInputChanged(newValue: String) {
        inputText = newValue
        calculate()
    }

    /*
     * Cambia el modo de conversión cuando el usuario mueve el Switch.
     */
    fun onConversionTypeChanged(isCelsius: Boolean) {
        isCelsiusToFahrenheit = isCelsius
        calculate()
    }

    /*
     * Función principal que realiza la conversión.
     * Se llama automáticamente cada vez que cambia el input o el tipo de conversión.
     */
    private fun calculate() {
        // Convertimos el texto del usuario a número.
        val number = inputText.toDoubleOrNull()
        // Si no es número válido (vacío, letras…), borramos el resultado.
        if (number == null) {
            resultText = ""
            return
        }

        // Conversión
        val result = if (isCelsiusToFahrenheit) {
            (number * 1.8) + 32
        } else {
            (number - 32) / 1.8
        }

        // Formateamos a dos decimales
        resultText = "%.2f".format(Locale.US, result)
    }
}
