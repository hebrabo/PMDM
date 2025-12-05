package com.example.pmdm01_14_miniproyecto8repositoriocitas

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pmdm01_14_miniproyecto8repositoriocitas.model.Quote

/*
 * El ViewModel gestiona la lista de citas y cuál está seleccionada.
 * Tiene:
 *  - Una lista fija de citas
 *  - Un estado que representa la cita actual mostrada
 */

class QuoteViewModel : ViewModel() {

    // Lista fija de citas predefinidas
    private val quotes = listOf(
        Quote("La vida es 10% lo que te ocurre y 90% cómo reaccionas."),
        Quote("El éxito es la suma de pequeños esfuerzos repetidos día tras día."),
        Quote("Haz lo que puedas, con lo que tengas, donde estés."),
        Quote("La mejor manera de predecir el futuro es crearlo."),
        Quote("No cuentes los días, haz que los días cuenten.")
    )

    // Estado observable con la cita seleccionada
    var currentQuote = mutableStateOf(quotes.random())
        private set

    // Cambia la cita por otra aleatoria
    fun loadRandomQuote() {
        currentQuote.value = quotes.random()
    }
}
