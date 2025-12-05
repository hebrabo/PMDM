package com.example.pmdm01_14_miniproyecto10checklistcompras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme

/*
 * 10. Checklist de compras
 * Lista de compras donde cada elemento se puede marcar como “comprado”.
 * El ViewModel gestiona la lista y los estados de cada ítem.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                ShoppingListScreen()
            }
        }
    }
}