package com.example.abntutorpanel.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * El DAO define las operaciones que podemos realizar sobre la tabla "missions".
 */
@Dao
interface MissionDao {

    // 1. Obtener todas las misiones ordenadas por ID
    // Usamos Flow para que la UI se actualice automáticamente si algo cambia
    @Query("SELECT * FROM missions ORDER BY id ASC")
    fun getAllMissions(): Flow<List<Mission>>

    // 2. Insertar una nueva misión o actualizar una existente
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMission(mission: Mission)

    // 3. Eliminar una misión específica
    @Delete
    suspend fun deleteMission(mission: Mission)
}