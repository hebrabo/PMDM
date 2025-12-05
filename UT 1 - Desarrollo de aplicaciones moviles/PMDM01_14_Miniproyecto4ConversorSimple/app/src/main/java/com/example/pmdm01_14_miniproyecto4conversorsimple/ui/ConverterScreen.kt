package com.example.pmdm01_14_miniproyecto4conversorsimple.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pmdm01_14_miniproyyecto4conversorsimple.viewmodel.ConverterViewModel

@Composable
fun ConverterScreen(
    modifier: Modifier = Modifier,
    viewModel: ConverterViewModel = viewModel() // Se obtiene el ViewModel asociado a esta pantalla
) {
    // Se obtiene el ViewModel asociado a esta pantalla
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Centra el contenido
        verticalArrangement = Arrangement.Center // Lo sitúa en el centro de la pantalla
    ) {
        // Título principal
        Text(
            text = "Conversor de Temperatura",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        /*
         * Campo de texto donde el usuario introduce un número.
         * El estado se lee directamente del ViewModel.
         * Cada vez que cambia, ViewModel recalcula el resultado.
         */
        OutlinedTextField(
            value = viewModel.inputText,
            onValueChange = { viewModel.onInputChanged(it) },
            label = {
                Text(if (viewModel.isCelsiusToFahrenheit) "Grados Celsius" else "Grados Fahrenheit")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        /*
         * Interruptor (Switch) que cambia el tipo de conversión.
         * true  -> Celsius a Fahrenheit
         * false -> Fahrenheit a Celsius
         */
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Fahrenheit → Celsius")
            Switch(
                checked = viewModel.isCelsiusToFahrenheit,
                onCheckedChange = { viewModel.onConversionTypeChanged(it) },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text("Celsius → Fahrenheit")
        }

        Spacer(modifier = Modifier.height(32.dp))

        /*
         * Mostramos el resultado solo cuando no está vacío.
         * (Si el usuario no ha introducido nada, no aparece nada).
         */
        if (viewModel.resultText.isNotEmpty()) {
            Text(text = "Resultado:", style = MaterialTheme.typography.labelLarge)
            Text(
                text = "${viewModel.resultText} ${if (viewModel.isCelsiusToFahrenheit) "°F" else "°C"}",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ConverterScreenPreview() {
    ConverterScreen()
}
