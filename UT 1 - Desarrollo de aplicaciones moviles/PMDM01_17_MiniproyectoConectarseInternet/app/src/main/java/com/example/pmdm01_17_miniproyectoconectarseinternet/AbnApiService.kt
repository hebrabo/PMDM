package com.example.pmdm01_17_miniproyectoconectarseinternet

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

/**
 * 1. DEFINICIÓN DE LA INTERFAZ
 */
interface AbnApiService {
    /**
     * @GET: Realiza una solicitud a un servicio web REST mediante una URI.
     * SUSPEND: Indica un "Punto de Suspensión".
     * Permite que la corrutina se pause sin bloquear el hilo principal (Main Thread)
     * mientras se espera la respuesta del servidor.
     */
    @GET("juegos_abn.json")
    suspend fun getJuegos(): List<JuegoAbn>
}

/**
 * 2. OBJETO SINGLETON (Patrón de diseño)
 */
object RetrofitInstance {

    private const val BASE_URL = "https://raw.githubusercontent.com/hebrabo/PMDM/main/UT%201%20-%20Desarrollo%20de%20aplicaciones%20moviles/PMDM01_17_MiniproyectoConectarseInternet/data/"

    /**
     * CONFIGURACIÓN DE JSON:
     * ignoreUnknownKeys = true: Evita que la app falle si el JSON de GitHub tiene
     * claves que no hemos definido en nuestra Data Class.
     */
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * 'by lazy': Inicialización diferida.
     * Delega la creación del objeto a la primera vez que se usa para
     * mejorar el rendimiento.
     */
    val api: AbnApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            /**
             * CONVERSOR: kotlinx.serialization.
             * Convierte la cadena JSON recuperada en objetos Kotlin.
             * Usamos 'asConverterFactory' para integrar la serialización con Retrofit.
             */
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(AbnApiService::class.java)
    }
}