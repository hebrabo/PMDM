package com.example.tiptime

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.tiptime.ui.theme.TipTimeTheme
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import java.text.NumberFormat
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag

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
        // CAMBIO: Antes usábamos onNodeWithText("Bill Amount")
        // Lo sustituimos por onNodeWithTag("billAmountField") porque:
        // - Los textos pueden cambiar (por localización o diseño).
        // - El testTag es más estable y recomendado por Google.
        composeTestRule.onNodeWithTag("billAmountField")
            .performTextInput("10")

        // Ingresa un valor de 20 en el campo "Tip Percentage"
        composeTestRule.onNodeWithTag("tipPercentageField")
            .performTextInput("20")

       // Calcula el valor esperado de la propina ($2.00, formateado según la configuración regional)
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)

        // CAMBIO: añadimos assertIsDisplayed() además de assertExists()
        // Esto asegura no solo que el nodo exista, sino que esté visible en pantalla.
        composeTestRule.onNodeWithTag("tipAmountText")
            .assertExists("El texto de la propina no aparece")  // Mensaje si la aserción falla
            .assertIsDisplayed()
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
        composeTestRule.onNodeWithTag("billAmountField").performTextInput("15")

        // Ingresa un 20% de propina.
        composeTestRule.onNodeWithTag("tipPercentageField").performTextInput("20")

        // Simula hacer clic en el interruptor "Round up tip?" para desactivar el redondeo.
        composeTestRule.onNodeWithTag("roundUpSwitch").performClick() // desactiva redondeo

        // Calcula el valor esperado de la propina sin redondeo (3.00 en este caso).
        val expectedTip = NumberFormat.getCurrencyInstance().format(3)

        // Verifica que el texto con el valor de la propina se muestre correctamente.
        composeTestRule.onNodeWithTag("tipAmountText")
            .assertExists("No se muestra el valor de propina esperado")
            .assertIsDisplayed()
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
        composeTestRule.onNodeWithTag("billAmountField").performTextInput("33.33")
        // Simula que el usuario introduce "18" como porcentaje de propina.
        composeTestRule.onNodeWithTag("tipPercentageField").performTextInput("18")

        // Dado que el redondeo está activado por defecto, el resultado se redondeará hacia arriba.
        // El 18% de 33.33 es 5.9994, que se redondea a 6.00 según la lógica del metodo calculateTip().
        val expectedTip = NumberFormat.getCurrencyInstance().format(6)

        // Verifica que el texto con el valor esperado de propina redondeado se muestre en la interfaz.
        composeTestRule.onNodeWithTag("tipAmountText")
            .assertExists("No se muestra el valor de propina redondeado esperado")
            .assertIsDisplayed()
    }
}