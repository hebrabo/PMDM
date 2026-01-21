package com.example.busschedule.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
 * Crea una base de datos de Room que use Entity y tu DAO.
 * La base de datos se inicializa con datos del archivo
 * assets/database/bus_schedule.db en el código de inicio.
 */
@Database(entities = [BusSchedule::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Metodo abstracto para obtener el DAO
    abstract fun scheduleDao(): BusScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                )
                    /**
                     * Cargamos los datos iniciales de los autobuses.
                     */
                    .createFromAsset("database/bus_schedule.db")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}