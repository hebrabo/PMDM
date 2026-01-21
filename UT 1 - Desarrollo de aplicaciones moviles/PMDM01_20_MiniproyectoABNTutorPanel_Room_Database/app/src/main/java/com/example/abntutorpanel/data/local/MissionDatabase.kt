package com.example.abntutorpanel.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Clase principal de la base de datos de Room.
 * Definimos las entidades y la versión (importante para futuras actualizaciones).
 */
@Database(entities = [Mission::class], version = 1, exportSchema = false)
abstract class MissionDatabase : RoomDatabase() {

    // Le decimos a la base de datos cómo acceder al DAO
    abstract fun missionDao(): MissionDao

    companion object {
        @Volatile
        private var Instance: MissionDatabase? = null

        // Función para obtener la base de datos (si no existe, la crea)
        fun getDatabase(context: Context): MissionDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    MissionDatabase::class.java,
                    "mission_database" // Nombre del archivo físico
                ).build().also { Instance = it }
            }
        }
    }
}