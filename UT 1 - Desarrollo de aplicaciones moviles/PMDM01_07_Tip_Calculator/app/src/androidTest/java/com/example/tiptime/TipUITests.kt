package com.example.tiptime

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.tiptime.ui.theme.TipTimeTheme
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import java.text.NumberFormat

class TipUITests {

    // Regla que configura el entorno de prueba de Compose
    @get:Rule
    val composeTestRule = createComposeRule()

    // Prueba de instrumentación para comprobar el cálculo del 20% de propina
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
}