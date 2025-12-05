package com.example.pmdm01_14_miniproyecto8repositoriocitas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.pmdm01_14_miniproyecto8repositoriocitas.ui.QuoteScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                QuoteScreen()
            }
        }
    }
}