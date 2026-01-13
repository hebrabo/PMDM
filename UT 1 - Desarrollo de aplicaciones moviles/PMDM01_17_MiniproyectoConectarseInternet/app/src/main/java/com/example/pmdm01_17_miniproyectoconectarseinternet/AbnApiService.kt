package com.example.pmdm01_17_miniproyectoconectarseinternet

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * 1. DEFINICIÓN DE LA INTERFAZ
 * Aquí definimos "qué" queremos pedirle a Internet.
 */
interface AbnApiService {

    /**
     * @GET: Indica que vamos a realizar una petición de lectura al servidor.
     * "juegos_abn.json": Es la ruta relativa que se sumará a la BASE_URL.
     * * SUSPEND: Indica que esta función es un "Punto de Suspensión".
     * La corrutina se pausará aquí mientras espera los datos de GitHub
     * y se reanudará automáticamente cuando lleguen, TODO esto sin bloquear
     * el hilo principal (Main Thread), manteniendo la app fluida.
     */
    @GET("juegos_abn.json")
    suspend fun getJuegos(): List<JuegoAbn>
}

/**
 * 2. OBJETO SINGLETON (Patrón de diseño)
 * Usamos 'object' en Kotlin para asegurar que solo exista UNA instancia
 * de Retrofit en toda la aplicación, ahorrando memoria y recursos.
 */
object RetrofitInstance {

    /**
     * BASE_URL: Es la dirección raíz del servidor.
     * IMPORTANTE: Siempre debe terminar en "/" para que Retrofit
     * pueda concatenar correctamente las rutas de la interfaz.
     */
    private const val BASE_URL = "https://raw.githubusercontent.com/hebrabo/PMDM/main/UT%201%20-%20Desarrollo%20de%20aplicaciones%20moviles/PMDM01_17_MiniproyectoConectarseInternet/data/"

    /**
     * 'by lazy': Inicialización perezosa.
     * El objeto API no se creará hasta que alguien lo llame por primera vez.
     * Esto hace que la app arranque más rápido.
     */
    val api: AbnApiService by lazy {
        Retrofit.Builder()
            // Configuramos la dirección del servidor
            .baseUrl(BASE_URL)
            // GSON: El "traductor". Convierte el texto plano del JSON de GitHub
            // automáticamente en objetos de nuestra clase 'JuegoAbn'.
            .addConverterFactory(GsonConverterFactory.create())
            // Construimos la configuración
            .build()
            // Creamos la implementación real de nuestra interfaz
            .create(AbnApiService::class.java)
    }
}