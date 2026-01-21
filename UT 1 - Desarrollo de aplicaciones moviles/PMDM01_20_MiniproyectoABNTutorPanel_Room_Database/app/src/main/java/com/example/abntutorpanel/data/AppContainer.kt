package com.example.abntutorpanel.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.abntutorpanel.data.local.MissionDatabase
import com.example.abntutorpanel.data.local.TutorPreferencesRepository

// Definimos el DataStore como una extensión del Contexto
private const val TUTOR_PREFERENCES_NAME = "tutor_preferences"
private val Context.dataStore by preferencesDataStore(name = TUTOR_PREFERENCES_NAME)

/**
 * Contenedor para la inyección de dependencias a nivel de aplicación.
 */
interface AppContainer {
    val missionDao: com.example.abntutorpanel.data.local.MissionDao
    val tutorPreferencesRepository: TutorPreferencesRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    // Inicializamos el DAO de Room
    override val missionDao by lazy {
        MissionDatabase.getDatabase(context).missionDao()
    }

    // Inicializamos el repositorio de DataStore
    override val tutorPreferencesRepository by lazy {
        TutorPreferencesRepository(context.dataStore)
    }
}