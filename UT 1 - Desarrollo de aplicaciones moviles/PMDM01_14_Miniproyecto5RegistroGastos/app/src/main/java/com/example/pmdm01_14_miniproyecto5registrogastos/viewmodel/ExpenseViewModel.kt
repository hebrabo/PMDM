package com.example.pmdm01_14_miniproyecto5registrogastos.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pmdm01_14_miniproyecto5registrogastos.model.Expense

/*
 * ViewModel que gestiona:
 *   ✔ la lista de gastos
 *   ✔ el cálculo del total
 *   ✔ la lógica de validación
 *
 * NO contiene nada de UI (buena arquitectura).
 */

class ExpenseViewModel : ViewModel() {

    // Lista observable de gastos
    private val _expenses = mutableStateListOf<Expense>()
    val expenses: List<Expense> = _expenses   // exposición solo lectura

    // Total de gastos
    val total = mutableStateOf(0.0)

    // Agregar un nuevo gasto
    fun addExpense(name: String, amount: Double) {
        // Validaciones simples
        if (name.isBlank() || amount <= 0) return

        _expenses.add(Expense(name, amount))
        calculateTotal()
    }

    // Recalcular el total cada vez que se agrega un gasto
    private fun calculateTotal() {
        total.value = expenses.sumOf { it.amount }
    }
}
