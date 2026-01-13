package com.example.pmdm01_17_miniproyectoconectarseinternet

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// 1. La Interfaz: Solo pedimos el nombre del archivo
interface AbnApiService {
    @GET("juegos_abn.json")
    suspend fun getJuegos(): List<JuegoAbn>
}

// 2. El Objeto Retrofit: Aquí va la ruta de la carpeta en GitHub
object RetrofitInstance {
    // IMPORTANTE: La URL base debe terminar en /
    private const val BASE_URL = "https://raw.githubusercontent.com/hebrabo/PMDM/main/UT%201%20-%20Desarrollo%20de%20aplicaciones%20moviles/PMDM01_17_MiniproyectoConectarseInternet/data/"

    val api: AbnApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AbnApiService::class.java)
    }
}