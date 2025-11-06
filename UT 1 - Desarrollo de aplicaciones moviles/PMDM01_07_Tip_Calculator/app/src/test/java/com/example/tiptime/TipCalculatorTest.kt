package com.example.tiptime

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.NumberFormat

/**
 * Clase de prueba local para verificar el cálculo de propinas.
 *
 * Esta clase prueba la función `calculateTip()` definida en MainActivity.kt.
 * Las pruebas locales se ejecutan en la JVM (sin necesidad de un dispositivo Android).
 */
class TipCalculatorTests {

    /**
     * Prueba que el cálculo de propina del 20% sobre una factura de $10
     * sin redondear el resultado produzca un valor correcto ($2.00).
     *
     * Escenario:
     *  - Importe: 10.00
     *  - Porcentaje de propina: 20%
     *  - Redondeo: desactivado
     *
     * Resultado esperado:
     *  - Propina: $2.00 (según la configuración regional del dispositivo)
     */

    @Test
    fun calculateTip_20PercentNoRoundup() {
        val amount = 10.00
        val tipPercent = 20.00
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)

        // Llamada a la función que se prueba
        val actualTip = calculateTip(amount = amount, tipPercent = tipPercent, false)

        // Aserción: compara el valor esperado con el valor real
        // Si no coinciden, la prueba falla.
        assertEquals(expectedTip, actualTip)
    }
}