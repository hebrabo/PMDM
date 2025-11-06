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
/**
* Función componible principal que organiza la interfaz de usuario.
*
* - Administra el estado 'amountInput', que representa el texto que el usuario ingresa.
* - Convierte la entrada a número usando `toDoubleOrNull()` y calcula el importe de la propina con `calculateTip()`.
* - Muestra el campo de texto (EditNumberField) y el resultado de la propina calculada.
*
* La función sigue el patrón de *elevación de estado*, manteniendo el estado en el nivel superior
* (TipTimeLayout) y pasando los valores necesarios a los elementos componibles hijos.
*/
fun TipTimeLayout() {
    // Estado recordado que almacena el texto ingresado por el usuario.
    var amountInput by remember { mutableStateOf("") }
    // Convierte el texto a Double o usa 0.0 si no es un número válido.
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    // Calcula el importe de la propina usando la función auxiliar.
    val tip = calculateTip(amount)
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
        // Campo de texto que recibe el importe de la cuenta.
        // Se pasa el valor actual y la función de actualización (estado elevado).
        EditNumberField(
            value = amountInput,
            onValueChange = { amountInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        // Muestra el importe calculado de la propina en formato monetario.
        Text(
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
/*
* Función componible sin estado que muestra un campo de texto para ingresar el importe.
*
* - Recibe dos parámetros principales:
*   - 'value': el valor actual que se muestra en el campo de texto.
*   - 'onValueChange': una lambda que se ejecuta cuando el usuario cambia el texto.
*
* Esta función no almacena estado propio; el valor y las actualizaciones se manejan desde
* la función que la llama (TipTimeLayout), siguiendo el principio de *elevación de estado*.
*/
fun EditNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Campo de texto para ingreso numérico.
    TextField(
        // Muestra el valor actual recibido como parámetro.
        value = value,
        // Llama a la función proporcionada cuando el usuario modifica el texto.
        onValueChange = onValueChange,
        /// 'label' muestra una descripción del campo (por ejemplo, "Bill amount").
        // El texto se desplaza hacia arriba cuando el campo obtiene foco.
        label = { Text(stringResource(R.string.bill_amount)) },
        // Limita el campo a una sola línea de texto (evita que el usuario agregue saltos de línea).
        singleLine = true,
        // 'keyboardOptions' define el tipo de teclado que se mostrará en pantalla.
        // En este caso, KeyboardType.Number muestra un teclado numérico para ingresar montos.
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )

}

/**
 * Calculates the tip based on the user input and format the tip amount
 * according to the local currency.
 * Example would be "$10.00".
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
