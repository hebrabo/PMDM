package com.example.pmdm01_14_miniproyecto5registrogastos.model

/*
 * Modelo que representa un gasto individual.
 * Contiene:
 *   - name: nombre del gasto
 *   - amount: cantidad del gasto en euros
 */
data class Expense(
    val name: String,
    val amount: Double
)
