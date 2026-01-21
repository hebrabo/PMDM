package com.example.dessertrelease.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/*
 * ¿Qué es UserPreferencesRepository?
 * El Repositorio es una clase que centraliza el acceso a los datos.
 * En este caso, su única misión es gestionar las preferencias del usuario
 * (si quiere ver una lista o una cuadrícula).
 * Al crear esta clase, separamos la lógica de "cómo se guardan los datos"
 * de la lógica de "cómo se ve la pantalla".
 */
class UserPreferencesRepository(
    /*
     * En el constructor UserPreferencesRepository,
     * define una propiedad de valor privado para
     * representar una instancia del objeto DataStore
     * con un tipo Preferences.
     * Con esto estamos pasando el dataStore en el constructor
     * en lugar de crearlo dentro de la clase
     * para que sea más fácil de probar y
     * para que toda la app comparta un único archivo de preferencias)
     */
    private val dataStore: DataStore<Preferences>
){
    /*
     * DataStore almacena pares clave-valor (DataStore es el almacén).
     * Para acceder a un valor, debemos definir una clave.
     * El companion object es el lugar (el contenedor) donde se guarda la clave.
     */
    private companion object {
        // Es el nombre que le damos al dato de la clave (El identidicador)
        val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
        const val TAG = "UserPreferencesRepo" // Para que el Log.e funcione
    }

    // ------- LEE DESDE DATA STORE (de forma continua) ------
     /*
     * Esta es la "tubería" de la aplicación.
     * Flow: es como un grifo abierto.
     * No es un valor único (como un Boolean normal), sino una corriente de datos.
     * Cada vez que el usuario cambie la preferencia en el futuro,
     * esta "tubería" enviará el nuevo valor automáticamente a la interfaz de usuario.
     */
    val isLinearLayout: Flow<Boolean> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                /*
                 * emit(emptyPreferences()): Si algo sale mal,
                 * en lugar de cerrar la app con un error, le decimos:
                 * "No te preocupes, actúa como si no hubiera preferencias guardadas (vacío)".
                 */
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        /*
         * .map { preferences -> ... } (El filtro)
         * El "Almacén" (DataStore) contiene muchas preferencias,
         * pero a nosotros solo nos interesa una.
         * preferences: Es el mapa completo de todos los pares clave-valor que hay en el archivo.
         * IS_LINEAR_LAYOUT: Usamos nuestra llave del companion object para abrir el cajón correcto.
         */
        .map { preferences ->
            // si no hay nada guardado, la app mostrará la vista de lista (true).
            preferences[IS_LINEAR_LAYOUT] ?: true
        }

    /*
     * ----- ESCRIBE EN DATA STORE ------
     * Esta función sirve para que,
     * cuando el usuario pulse el botón de "Cambiar vista" en la app,
     * el cambio sea permanente.
     */
    suspend fun saveLayoutPreference(isLinearLayout: Boolean) {
        /*
         * dataStore.edit (La transacción) asegura que
         * si dos partes de la app intentan guardar algo al mismo tiempo,
         * no se rompa el archivo.
         * preferences: Dentro de las llaves { ... }, recibimos un objeto (llamado preferences)
         * que es como un bloc de notas temporal.
         * Todos los cambios que hagamos en ese bloc
         * se guardarán de golpe al cerrar la llave.
         */
        dataStore.edit { preferences ->
            /*
             * IS_LINEAR_LAYOUT: Es la llave
             * isLinearLayout: El valor que queremos guardar
             * Estamos diciendo: "En el cajón que tiene esta etiqueta, guarda este valor".
             */
            preferences[IS_LINEAR_LAYOUT] = isLinearLayout //(la asignación)
        }
    }
}