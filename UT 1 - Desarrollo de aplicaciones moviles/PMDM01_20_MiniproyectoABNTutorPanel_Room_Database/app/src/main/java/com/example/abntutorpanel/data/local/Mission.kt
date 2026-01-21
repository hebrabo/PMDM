package com.example.abntutorpanel.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Esta clase define la tabla "missions" en la base de datos de Room.
 * Cada misión representa una tarea de ABN que el tutor asigna al niño.
 */
@Entity(tableName = "missions")
data class Mission(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,            // Room generará un ID único automáticamente
    val title: String,         // Ej: "Contar hasta 10"
    val description: String,   // Ej: "Pon 10 palillos en el bote"
    val gameId: Int,           // El ID del juego de ABN que se debe abrir
    val isCompleted: Boolean = false // Para saber si el niño ya la terminó
)