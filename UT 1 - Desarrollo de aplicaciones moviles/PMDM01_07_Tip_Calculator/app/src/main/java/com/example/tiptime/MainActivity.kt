/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
/*
* Función componible principal que organiza la interfaz de usuario de la app Tip Calculator.
*
* - Administra dos variables de estado:
*     - `amountInput`: texto ingresado por el usuario para el importe de la cuenta.
*     - `tipInput`: texto ingresado para el porcentaje de propina personalizado.
* - Convierte ambas entradas a números con `toDoubleOrNull()`, usando 0.0 si no son válidas.
* - Calcula el importe de la propina con la función auxiliar `calculateTip()`.
* - Muestra dos campos de texto reutilizando `EditNumberField()` y el resultado del cálculo.
*
* Esta función sigue el patrón de *elevación de estado*: los estados se mantienen aquí y
* se pasan como parámetros a los elementos componibles hijos, que no almacenan su propio estado.
*/
fun TipTimeLayout() {
    // Estado recordado que almacena el texto ingresado por el usuario (importe de la factura).
    var amountInput by remember { mutableStateOf("") }

    // Estado recordado para el porcentaje de propina.
    var tipInput by remember { mutableStateOf("") }

    // Convierte el importe y el porcentaje a Double, o usa 0.0 si el valor no es válido.
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

    // Calcula el importe de la propina usando la función auxiliar.
    val tip = calculateTip(amount, tipPercent)



    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título superior de la pantalla
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        // Campo de texto para ingresar el importe de la factura.
        EditNumberField(
            label = R.string.bill_amount,
            value = amountInput,
            onValueChange = { amountInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        // Campo de texto para ingresar el porcentaje de propina personalizado.
        EditNumberField(
            label = R.string.how_was_the_service,
            value = tipInput,
            onValueChange = { tipInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        // Muestra el importe de la propina calculado, con formato monetario local.
        Text(
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
/*
* Función componible sin estado que muestra un campo de texto numérico reutilizable.
*
* Parámetros:
* - `label`: recurso de cadena (Int) usado como etiqueta del campo (por ejemplo, "Bill amount").
*   La anotación `@StringRes` garantiza que el valor provenga de `strings.xml`.
* - `value`: valor actual mostrado en el campo.
* - `onValueChange`: lambda que se ejecuta cada vez que el usuario cambia el texto.
* - `modifier`: permite aplicar espaciado, tamaño o posición personalizados.
*
* Esta función es reutilizable y no gestiona estado propio: el valor y la actualización se
* controlan desde el nivel superior (TipTimeLayout).
*/
fun EditNumberField(
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Campo de texto que permite ingresar solo números.
    TextField(
        value = value, // Muestra el valor actual.
        onValueChange = onValueChange, // Actualiza el estado cuando el usuario escribe.
        label = { Text(stringResource(label)) }, // Usa el recurso de cadena como etiqueta del campo.
        singleLine = true, // Limita el campo a una sola línea de texto.
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Muestra teclado numérico.
        modifier = modifier
    )

}

/**
 * Función auxiliar que calcula la propina según los valores ingresados y la formatea
 * con el símbolo de moneda local (por ejemplo, "$10.00").
 */
private fun calculateTip(amount: Double, tipPercent: Double = 15.0): String {
    val tip = tipPercent / 100 * amount
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    TipTimeTheme {
        TipTimeLayout()
    }
}
