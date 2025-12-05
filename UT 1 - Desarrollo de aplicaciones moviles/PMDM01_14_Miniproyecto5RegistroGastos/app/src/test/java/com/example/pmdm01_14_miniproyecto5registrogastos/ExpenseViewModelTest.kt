package com.example.pmdm01_14_miniproyecto5registrogastos

import com.example.pmdm01_14_miniproyecto5registrogastos.viewmodel.ExpenseViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/*
 * Tests unitarios del ViewModel.
 * Aquí comprobamos la lógica sin necesidad de interfaz.
 */

class ExpenseViewModelTest {

    private lateinit var viewModel: ExpenseViewModel

    @Before
    fun setup() {
        // Se crea un ViewModel nuevo para cada test
        viewModel = ExpenseViewModel()
    }

    @Test
    fun addExpenseAddAvalidExpense() {
        // Agregar un gasto válido
        viewModel.addExpense("Comida", 50.0)

        Assert.assertEquals(1, viewModel.expenses.size)
        Assert.assertEquals("Comida", viewModel.expenses[0].name)
        Assert.assertEquals(50.0, viewModel.expenses[0].amount, 0.0)
    }

    @Test
    fun addExpenseDoesNotAddExpenseWithBlankName() {
        // Nombre vacío → No debe agregar
        viewModel.addExpense("", 50.0)

        Assert.assertTrue(viewModel.expenses.isEmpty())
        Assert.assertEquals(0.0, viewModel.total.value, 0.0)
    }

    @Test
    fun addExpenseDoesNotAddExpenseWithNonPositiveAmount() {
        // Cantidad ≤ 0 → No se agrega
        viewModel.addExpense("Comida", 0.0)
        viewModel.addExpense("Transporte", -10.0)

        Assert.assertTrue(viewModel.expenses.isEmpty())
        Assert.assertEquals(0.0, viewModel.total.value, 0.0)
    }

    @Test
    fun totalIsCalculatedCorrectly() {
        // Dos gastos → total = 80
        viewModel.addExpense("Comida", 50.0)
        viewModel.addExpense("Transporte", 30.0)

        Assert.assertEquals(2, viewModel.expenses.size)
        Assert.assertEquals(80.0, viewModel.total.value, 0.0)
    }
}
