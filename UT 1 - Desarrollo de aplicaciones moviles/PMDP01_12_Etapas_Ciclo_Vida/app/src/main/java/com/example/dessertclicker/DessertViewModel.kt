package com.example.dessertclicker

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert

/**
 * ViewModel que contiene toda la lógica y el estado de la UI.
 *
 * Qué hace un ViewModel:
 * - Mantiene los datos y la lógica separados de la UI (MainActivity/Composable).
 * - Permite que los datos sobrevivan a cambios de configuración (rotación de pantalla).
 * - Compose puede "observar" su estado y actualizar la UI automáticamente cuando cambia.
 */
class DessertViewModel : ViewModel() {
    // Lista de postres disponible en la app, traída desde el DataSource.
    // Es una lista "constante" que no cambia durante la ejecución de la app.
    private val desserts = Datasource.dessertList

    // Estado interno mutable: contiene todos los datos que la UI necesita.
    // Usamos mutableStateOf para que Compose pueda observar los cambios automáticamente.
    private val _uiState = mutableStateOf(
        DessertUiState(
            dessertsSold = 0, // Cantidad de postres vendidos
            revenue= 0, // Total de ingresos acumulados
            currentDessertIndex = 0, // Índice del postre actual
            desserts[0].price, // Precio del postre actual
            currentDessertImageId = desserts[0].imageId // Imagen del postre actual
        )
    )

    // Exponemos el estado como solo lectura para que la UI pueda "suscribirse" a él.
    // Esto evita que la UI pueda modificar directamente los datos.
    val uiState: State<DessertUiState>  = _uiState

    /**
     * Método que se llama cuando el usuario hace clic en un postre.
     * Se encarga de:
     * 1) Incrementar ingresos y cantidad de postres vendidos.
     * 2) Determinar si se debe mostrar otro postre más caro.
     * 3) Actualizar el estado con los nuevos valores.
     */

    fun onDessertClicked(){
        // 1) Actualizar ingresos y cantidad de postres vendidos
        val newRevenue = _uiState.value.revenue + _uiState.value.currentDessertPrice
        val newDessertsSold = _uiState.value.dessertsSold + 1

        // 2) Determinar qué postre mostrar a continuación
        // La lógica decide si debemos avanzar al siguiente postre más caro
        val dessertToShow = determineDessertToShow(desserts, newDessertsSold)
        val newIndex = desserts.indexOf(dessertToShow)

        // 3) Actualizar el estado de la UI
        // Creamos una nueva instancia de DessertUiState usando copy(),
        // porque los estados de Compose son inmutables.
        _uiState.value = _uiState.value.copy(
            revenue = newRevenue,
            dessertsSold = newDessertsSold,
            currentDessertIndex = newIndex,
            currentDessertPrice = dessertToShow.price,
            currentDessertImageId = dessertToShow.imageId
        )
    }

    // Determina qué postre mostrar según la cantidad de postres vendidos.
    fun determineDessertToShow(
        desserts: List<Dessert>,
        dessertsSold: Int
    ): Dessert {
        var dessertToShow = desserts.first() // Inicia con el primer postre (el más barato)
        for (dessert in desserts) {
            // Si se han vendido suficientes postres, avanzamos al siguiente más caro
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // Como la lista está ordenada por precio, podemos salir del ciclo
                // cuando encontramos un postre que aún no se ha desbloqueado
                break
            }
        }

        return dessertToShow
    }
}