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
fun TipTimeLayout() {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        // Muestra el cuadro de texto en la pantalla
        EditNumberField(
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.tip_amount, "$0.00"),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
/* Función componible que le permite al usuario ingresar texto en la app.
 * - El parámetro value es un cuadro de texto que muestra el valor de cadena que pasas aquí.
 * - El parámetro 'onValueChange' es una lambda que se ejecuta cada vez que el usuario modifica el texto.
 *   Su propósito es actualizar el valor almacenado y reflejar los cambios en la interfaz.
 * Esta función utiliza la API de estado de Jetpack Compose para recordar y conservar el valor del texto
 * incluso después de las recomposiciones, gracias al uso de 'remember' y 'mutableStateOf'.
 * Además, convierte el texto ingresado a un número y calcula el importe de la propina
 * mediante la función calculateTip(), mostrando el valor en formato monetario.
 */
fun EditNumberField(modifier: Modifier = Modifier) {
    // Se declara una variable de estado 'amountInput' utilizando el delegado 'by' de Kotlin.
    // 'remember' asegura que el valor de 'amountInput' se conserve entre recomposiciones,
    // evitando que se reinicie a su valor inicial cada vez que la UI se vuelva a dibujar.
    // 'mutableStateOf("")' crea un estado observable con una cadena vacía como valor inicial.
    var amountInput by remember { mutableStateOf("") }
    // Convierte el texto ingresado (String) a un valor Double.
    // Si la conversión falla (por ejemplo, el campo está vacío o contiene texto no numérico),
    // se asigna el valor 0.0 gracias al uso del operador Elvis (?:).
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    // Calcula el importe de la propina en base al monto ingresado utilizando la función calculateTip().
    // Esta función devuelve una cadena formateada como valor monetario (por ejemplo, "$3.45").
    val tip = calculateTip(amount)
    // Componente de entrada de texto que muestra y actualiza el valor de 'amountInput'.
    TextField(
        // Muestra el texto actual almacenado en la variable de estado 'amountInput'.
        value = amountInput,
        // Se ejecuta cada vez que el usuario modifica el texto en el campo.
        // Actualiza el valor de 'amountInput', lo que provoca una recomposición
        // y actualiza automáticamente la interfaz con el nuevo valor.
        onValueChange = { amountInput = it },
        // 'label' muestra un texto descriptivo dentro del campo (por ejemplo, "Bill amount").
        // Este texto actúa como guía para el usuario y se mueve hacia arriba cuando el campo gana foco.
        label = { Text(stringResource(R.string.bill_amount)) },
        // Limita el campo a una sola línea de texto (evita que el usuario agregue saltos de línea).
        singleLine = true,
        // 'keyboardOptions' define el tipo de teclado que se mostrará en pantalla.
        // En este caso, KeyboardType.Number muestra un teclado numérico para ingresar montos.
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )

    // (Nota) Actualmente el importe calculado de la propina ('tip') no se muestra en pantalla
    // dentro de esta función. En la siguiente sección se elevará el estado para que pueda
    // ser usado desde TipTimeLayout() y así mostrar el importe de la propina en la UI.
    
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
