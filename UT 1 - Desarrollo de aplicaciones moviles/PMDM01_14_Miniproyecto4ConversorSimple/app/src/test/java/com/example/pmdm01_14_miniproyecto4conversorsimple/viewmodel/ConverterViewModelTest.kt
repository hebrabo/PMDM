package com.example.pmdm01_14_miniproyyecto4conversorsimple.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConverterViewModelTest {

    private lateinit var viewModel: ConverterViewModel

    @Before
    fun setup() {
        viewModel = ConverterViewModel()
    }

    @Test
    fun `celsius to fahrenheit conversion is correct`() {
        viewModel.onConversionTypeChanged(true) // Celsius → Fahrenheit
        viewModel.onInputChanged("0")
        assertEquals("32.00", viewModel.resultText)

        viewModel.onInputChanged("100")
        assertEquals("212.00", viewModel.resultText)
    }

    @Test
    fun `fahrenheit to celsius conversion is correct`() {
        viewModel.onConversionTypeChanged(false) // Fahrenheit → Celsius
        viewModel.onInputChanged("32")
        assertEquals("0.00", viewModel.resultText)

        viewModel.onInputChanged("212")
        assertEquals("100.00", viewModel.resultText)
    }

    @Test
    fun `switch conversion updates result`() {
        // Iniciar en Celsius → Fahrenheit
        viewModel.onConversionTypeChanged(true)
        viewModel.onInputChanged("100")
        assertEquals("212.00", viewModel.resultText)

        // Cambiar a Fahrenheit → Celsius
        viewModel.onConversionTypeChanged(false)
        assertEquals("37.78", viewModel.resultText) // 100°F = 37.78°C
    }

    @Test
    fun `invalid input clears result`() {
        viewModel.onInputChanged("abc")
        assertEquals("", viewModel.resultText)

        viewModel.onInputChanged("")
        assertEquals("", viewModel.resultText)

        viewModel.onInputChanged("   ")
        assertEquals("", viewModel.resultText)
    }

    @Test
    fun `result is formatted to two decimals`() {
        viewModel.onConversionTypeChanged(true) // Celsius → Fahrenheit
        viewModel.onInputChanged("37.7778")
        assertEquals("100.00", viewModel.resultText) // Aproximado a 2 decimales
    }
}
