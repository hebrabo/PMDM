package com.example.abntutorpanel

import android.app.Application
import com.example.abntutorpanel.data.AppContainer
import com.example.abntutorpanel.data.DefaultAppContainer

class ABNTutorApplication : Application() {
    /**
     * Instancia del contenedor de datos que usaremos en los ViewModels.
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
