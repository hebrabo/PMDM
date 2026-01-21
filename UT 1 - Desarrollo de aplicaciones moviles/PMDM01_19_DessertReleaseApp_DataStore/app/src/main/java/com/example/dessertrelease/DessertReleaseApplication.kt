package com.example.dessertrelease

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.dessertrelease.data.local.UserPreferencesRepository


// LAYOUT_PREFERENCE_NAME: Es el nombre del archivo que se guardará en la memoria del teléfono
private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"
/*
 * Context.dataStore: Estamos "añadiendo" una propiedad a la clase Context de Android.
 *      Como Application y Activity son hijos de Context,
 *      ahora todas esas clases pueden ver el almacén de datos simplemente escribiendo dataStore.
 * by preferencesDataStore(...): La palabra clave by indica un Delegado.
 *      Su misión: Asegurarse de que solo se cree una instancia del archivo DataStore.
 *      Si intentaramos abrir el mismo archivo de preferencias desde dos lugares distintos a la vez,
 *      el archivo podría corromperse.
 *      Garantiza que toda la app
 *      use exactamente el mismo "edificio" para guardar los datos (Patrón Singleton)
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCE_NAME
)
/*
 * Esta clase es lo primero que se ejecuta cuando el usuario toca el icono de la app,
 * incluso antes de que aparezca la primera pantalla.
 * ¿Cómo funciona el flujo?
 * 1) El sistema Android arranca la clase DessertReleaseApplication.
 * 2) Se ejecuta onCreate() y se crea el UserPreferencesRepository.
 * 3) Ese repositorio recibe el dataStore (el acceso al archivo).
 * 4) A partir de aquí, el ViewModel es quien solicita el repositorio a la clase Application.
 *      El ViewModel actúa como intermediario: "observa" la tubería de datos (Flow) del repositorio
 *      y le entrega a la pantalla (UI) el diseño listo para mostrar.
 *      De esta forma, la interfaz no tiene que saber cómo se guardan los datos,
 *      solo se encarga de dibujarlos.
 */
class DessertReleaseApplication: Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}