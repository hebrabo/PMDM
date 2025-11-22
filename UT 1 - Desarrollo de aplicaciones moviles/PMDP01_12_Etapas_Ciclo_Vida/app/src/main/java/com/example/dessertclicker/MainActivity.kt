package com.example.dessertclicker

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import com.example.dessertclicker.ui.theme.DessertClickerTheme
import android.util.Log
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel

// TAG para usar en Logcat y depuración
private const val TAG = "MainActivity"

/**
 * Actividad principal de la app
 * Hereda de ComponentActivity, que permite usar Jetpack Compose
 * setContent define la UI con composables
 */
class MainActivity : ComponentActivity() {
    /**
     * onCreate se llama cuando la actividad se crea por primera vez.
     * Es equivalente a la "fase de huevo" del ciclo de vida de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate Called") // Registro en Logcat para depuración
        // setContent define la UI usando Compose
        setContent {
            DessertClickerTheme { // Tema de la app
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                ) {
                    // Llamamos al composable principal conectado al ViewModel
                    DessertClickerApp()
                }
            }
        }
    }

    // Los siguientes métodos registran el ciclo de vida de la actividad
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart Called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart Called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
    }
}



/**
 * Función que comparte la información de postres vendidos
 * utilizando un Intent ACTION_SEND para otras apps.
 */
private fun shareSoldDessertsInformation(
    intentContext: Context,
    dessertsSold: Int,
    revenue: Int
) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            intentContext.getString(R.string.share_text, dessertsSold, revenue)
        )
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)

    // Intent seguro: si no hay app para compartir, mostramos un Toast
    try {
        intentContext.startActivity(shareIntent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            intentContext,
            intentContext.getString(R.string.sharing_not_available),
            Toast.LENGTH_LONG
        ).show()
    }
}

@Composable
/**
 * Composable principal conectado al ViewModel
 */
fun DessertClickerApp(viewModel: DessertViewModel = viewModel()) {

    val uiState = viewModel.uiState.value
    val intentContext = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        topBar = {
            DessertClickerAppBar(
                onShareButtonClicked = {
                    shareSoldDessertsInformation(
                        intentContext,
                        uiState.dessertsSold,
                        uiState.revenue
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateStartPadding(layoutDirection),
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(layoutDirection),
                    )
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        DessertClickerScreen(
            revenue = uiState.revenue,
            dessertsSold = uiState.dessertsSold,
            dessertImageId = uiState.currentDessertImageId,
            onDessertClicked = { viewModel.onDessertClicked() },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
/**
 * Barra superior de la app con el título y el botón de compartir.
 */
private fun DessertClickerAppBar(
    onShareButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium)),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
        )
        IconButton(
            onClick = onShareButtonClicked,
            modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_medium)),
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(R.string.share),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
 /**
 * Pantalla principal con imagen de fondo y postre clickeable.
 */
fun DessertClickerScreen(
    revenue: Int,
    dessertsSold: Int,
    @DrawableRes dessertImageId: Int,
    onDessertClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // Imagen de fondo
        Image(
            painter = painterResource(R.drawable.bakery_back),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column {
            Box(
                modifier = Modifier
                    .weight(1f) // Ocupa el espacio restante
                    .fillMaxWidth(),
            ) {
                // Imagen del postre clickeable
                Image(
                    painter = painterResource(dessertImageId),
                    contentDescription = null,
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.image_size))
                        .height(dimensionResource(R.dimen.image_size))
                        .align(Alignment.Center)
                        .clickable { onDessertClicked() }, // Detecta clics
                    contentScale = ContentScale.Crop,
                )
            }
            // Información de transacciones debajo del postre
            TransactionInfo(
                revenue = revenue,
                dessertsSold = dessertsSold,
                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
            )
        }
    }
}

@Composable
/**
 * Composable que Muestra la información de ventas y de ingresos.
 */
private fun TransactionInfo(
    revenue: Int,
    dessertsSold: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        DessertsSoldInfo(
            dessertsSold = dessertsSold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
        RevenueInfo(
            revenue = revenue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}

@Composable
/**
 * Muestra los ingresos totales de manera destacada
 */
private fun RevenueInfo(revenue: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.total_revenue),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = "$${revenue}",
            textAlign = TextAlign.Right,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
/**
 * Muestra la cantidad de postres vendidos
 */
private fun DessertsSoldInfo(dessertsSold: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.dessert_sold),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = dessertsSold.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}


@Preview
@Composable
/**
  * Preview de la app en Compose
*/
fun MyDessertClickerAppPreview() {
    DessertClickerTheme {
        DessertClickerApp()
    }
}
