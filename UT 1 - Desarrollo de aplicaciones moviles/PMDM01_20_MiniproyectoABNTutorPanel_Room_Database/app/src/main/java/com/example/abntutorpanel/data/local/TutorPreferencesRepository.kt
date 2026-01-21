package com.example.abntutorpanel.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Repositorio que gestiona las preferencias del tutor mediante DataStore.
 */
class TutorPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private companion object {
        // Definimos las llaves para los datos
        val IS_RESTRICTED_MODE = booleanPreferencesKey("is_restricted_mode")
        val MAX_PLAYING_TIME = intPreferencesKey("max_playing_time")
        const val TAG = "TutorPreferencesRepo"
    }

    /* * LEE EL MODO RESTRINGIDO:
     * Si está activado, el niño solo verá las misiones de Room.
     */
    val isRestrictedMode: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error al leer preferencias.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[IS_RESTRICTED_MODE] ?: false // Por defecto, juego libre (false)
        }

    // GUARDA EL MODO RESTRINGIDO
    suspend fun saveRestrictedMode(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_RESTRICTED_MODE] = isEnabled
        }
    }
}