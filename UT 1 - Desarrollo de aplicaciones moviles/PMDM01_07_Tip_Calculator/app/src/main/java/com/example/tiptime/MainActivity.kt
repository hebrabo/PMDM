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
import androidx.annotation.DrawableRes
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import org.jetbrains.annotations.VisibleForTesting
import androidx.compose.ui.platform.testTag

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
         * Función componible principal que construye la interfaz de la app Tip Time.
         *
         * - Administra los estados del importe, porcentaje y opción de redondeo.
         * - Convierte los valores de texto a Double y calcula la propina usando `calculateTip()`.
         * - Muestra campos de entrada, un interruptor de redondeo y el resultado calculado.
         * - Habilita desplazamiento vertical para adaptarse a pantallas horizontales.
         */
fun TipTimeLayout() {
    // Estado recordado que almacena el texto ingresado por el usuario (importe de la factura).
    var amountInput by remember { mutableStateOf("") }

    // Estado recordado para el porcentaje de propina.
    var tipInput by remember { mutableStateOf("") }

    // Convierte el importe y el porcentaje a Double, o usa 0.0 si el valor no es válido.
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

    // Variable de estado que guarda si el usuario desea redondear la propina.
    // Se recuerda durante las recomposiciones y su valor inicial es `false` (sin redondear).
    var roundUp by remember { mutableStateOf(false) }

    // Calcula la propina en función del importe, el porcentaje y el estado del interruptor.
    val tip = calculateTip(amount, tipPercent, roundUp)

    Column(
        modifier = Modifier
            .padding(40.dp)
            // Habilita el desplazamiento vertical en la columna.
            // `verticalScroll(rememberScrollState())` recuerda la posición de scroll y permite
            // desplazarse por la interfaz cuando el dispositivo está en orientación horizontal
            // o cuando el contenido no cabe completamente en pantalla.
            .verticalScroll(rememberScrollState()),
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
        // Incluye un ícono de dinero para indicar visualmente el tipo de dato esperado.
        // Usa ImeAction.Next para pasar al siguiente campo del formulario.
        EditNumberField(
            label = R.string.bill_amount,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = amountInput,
            onValueChange = { amountInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
                .testTag("billAmountField") // Añadido para pruebas
        )
        // Campo de texto para ingresar el porcentaje de propina personalizado.
        // Muestra un ícono de porcentaje (％) como referencia visual del valor esperado.
        // Usa ImeAction.Done para indicar que el usuario ha terminado la entrada.
        EditNumberField(
            label = R.string.how_was_the_service,
            leadingIcon = R.drawable.percent,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = tipInput,
            onValueChange = { tipInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
                .testTag("tipPercentageField") // Añadido para pruebas
        )

        // Fila con el texto "Round up tip?" y el interruptor (Switch) correspondiente.
        RoundTheTipRow(
            roundUp = roundUp,
            onRoundUpChanged = { roundUp = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .testTag("roundUpSwitch") // Añadido para identificar el interruptor
        )

        // Muestra el importe de la propina calculado, con formato monetario local.
        Text(
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.testTag("tipAmountText") // Añadido para pruebas
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
        /*
         * Función componible sin estado que muestra un campo de texto numérico reutilizable.
         *
         * Parámetros:
         *  - `label`: recurso de cadena usado como etiqueta del campo (por ejemplo, "Bill amount").
         *    Anotado con `@StringRes` para garantizar que el valor provenga de `strings.xml`.
         * @param leadingIcon Ícono que se muestra al inicio del campo de texto.
         *  Ayuda a comunicar visualmente el tipo de información esperada.
         * *  - `keyboardOptions`: opciones de teclado, incluyendo tipo de entrada e `ImeAction`.
         *  - `value`: texto actual que se muestra en el campo.
         *  - `onValueChange`: callback que se ejecuta cuando el usuario modifica el texto.
         *  - `modifier`: permite personalizar el espaciado, tamaño o posición del campo.
         *
         * Esta función no gestiona estado propio, lo que facilita su reutilización.
         */
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Campo de texto que permite ingresar solo números.
    // `leadingIcon` muestra el ícono recibido como parámetro (por ejemplo, dinero o porcentaje).
    TextField(
        value = value, // Muestra el valor actual.
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) }, // Ícono que se muestra al inicio del campo de texto.
        onValueChange = onValueChange, // Actualiza el estado cuando el usuario escribe.
        label = { Text(stringResource(label)) }, // Usa el recurso de cadena como etiqueta del campo.
        singleLine = true, // Limita el campo a una sola línea de texto.
        keyboardOptions = keyboardOptions, // Aplica las opciones de teclado recibidas.
        modifier = modifier
    )

}

@Composable
        /**
         * Fila que muestra un texto descriptivo y un interruptor (Switch) para redondear la propina.
         *
         * @param roundUp indica si el interruptor está activado.
         * @param onRoundUpChanged callback que actualiza el estado cuando el usuario cambia el valor.
         * @param modifier permite personalizar el espaciado o la posición de la fila.
         */
fun RoundTheTipRow(
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.round_up_tip))
        Switch(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .testTag("roundUpSwitch"), // Añadido para pruebas
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
        )
    }
}

/**
 * Calcula el importe de la propina según los valores ingresados.
 *
 * @param amount importe total de la factura.
 * @param tipPercent porcentaje de propina.
 * @param roundUp si es `true`, redondea el valor al número entero más próximo.
 *
 * @return una cadena formateada en la moneda local, por ejemplo "$10.00".
 */

@VisibleForTesting
internal fun calculateTip(
    amount: Double,
    tipPercent: Double = 15.0,
    roundUp: Boolean
): String {
    var tip = tipPercent / 100 * amount
    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}

/**
 * Vista previa de la interfaz en el editor de Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    TipTimeTheme {
        TipTimeLayout()
    }
}
