package com.example.busschedule.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Crea un objeto de acceso a datos (DAO) para acceder a la base de datos.
@Dao
interface BusScheduleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(schedule: BusSchedule)

    // El DAO proporciona un metodo para recuperar todos los elementos de la base de datos.
    // Asegúrate de ordenar el horario por hora de llegada
    @Query("SELECT * FROM schedule ORDER BY arrival_time ASC")
    fun getAll(): Flow<List<BusSchedule>>

    // El DAO proporciona un metodo para recuperar un solo elemento con el nombre de la parada de autobús
    @Query("SELECT * FROM schedule WHERE stop_name = :stopName ORDER BY arrival_time ASC")
    fun getByStopName(stopName: String): Flow<List<BusSchedule>>

}