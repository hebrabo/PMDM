package com.example.tiptime

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.tiptime.ui.theme.TipTimeTheme
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import java.text.NumberFormat

class TipUITests {

    // Regla que configura el entorno de prueba de Compose
    @get:Rule
    val composeTestRule = createComposeRule()

    // Prueba 1: Prueba de instrumentación para comprobar el cálculo del 20% de propina
    @Test
    fun calculate_20_percent_tip() {
        // Configura el contenido de la interfaz de usuario que se probará
        composeTestRule.setContent {
            TipTimeTheme {
                TipTimeLayout() // Llama al diseño principal de la app
            }
        }
        // Ingresa un valor de 10 en el campo "Bill Amount"
        composeTestRule.onNodeWithText("Bill Amount")
            .performTextInput("10")

        // Ingresa un valor de 20 en el campo "Tip Percentage"
        composeTestRule.onNodeWithText("Tip Percentage").performTextInput("20")

       // Calcula el valor esperado de la propina ($2.00, formateado según la configuración regional)
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)

        // Verifica que el texto con el monto de la propina se muestre correctamente en la interfaz
        composeTestRule.onNodeWithText("Tip Amount: $expectedTip").assertExists(
            "No node with this text was found." // Mensaje si la aserción falla
        )
    }

    // Prueba 2: Verifica el cálculo del 20% de propina SIN redondear.
    @Test
    fun calculate_20_percent_tip_no_roundup() {

        // Configura nuevamente la IU para esta prueba.
        composeTestRule.setContent {
            TipTimeTheme {
                TipTimeLayout()
            }
        }

        // Ingresa un valor de factura de 15
        composeTestRule.onNodeWithText("Bill Amount").performTextInput("15")

        // Ingresa un 20% de propina.
        composeTestRule.onNodeWithText("Tip Percentage").performTextInput("20")

        // Simula hacer clic en el interruptor "Round up tip?" para desactivar el redondeo.
        composeTestRule.onNodeWithText("Round up tip?").performClick() // desactiva redondeo

        // Calcula el valor esperado de la propina sin redondeo (3.00 en este caso).
        val expectedTip = NumberFormat.getCurrencyInstance().format(3)

        // Verifica que el texto con el valor de la propina se muestre correctamente.
        composeTestRule.onNodeWithText("Tip Amount: $expectedTip").assertExists(
            "El texto con el valor esperado de propina no se encontró."
        )
    }

    // Prueba 3: Verifica el cálculo de una propina del 18% con redondeo activado.
    @Test
    fun calculate_18_percent_tip_with_roundup() {
        composeTestRule.setContent {
            TipTimeTheme {
                TipTimeLayout()
            }
        }

        // Simula que el usuario escribe "33.33" como importe de la factura.
        composeTestRule.onNodeWithText("Bill Amount").performTextInput("33.33")

        // Simula que el usuario introduce "18" como porcentaje de propina.
        composeTestRule.onNodeWithText("Tip Percentage").performTextInput("18")

        // Dado que el redondeo está activado por defecto, el resultado se redondeará hacia arriba.
        // El 18% de 33.33 es 5.9994, que se redondea a 6.00 según la lógica del metodo calculateTip().
        val expectedTip = NumberFormat.getCurrencyInstance().format(6)

        // Verifica que el texto con el valor esperado de propina redondeado se muestre en la interfaz.
        composeTestRule.onNodeWithText("Tip Amount: $expectedTip").assertExists(
            "El texto con el valor esperado de propina no se encontró."
        )
    }
}