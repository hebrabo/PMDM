package com.example.pmdm01_17_miniproyectoconectarseinternet

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// 1. Interfaz: He creado este ID específico para tu app de ABN
interface AbnApiService {
    @GET("37603f90-8805-4f40-8488-842245b59a85")
    suspend fun getJuegos(): List<JuegoAbn>
}

// 2. Cliente Retrofit: Conectamos con el servidor de Mocky
object RetrofitInstance {
    private const val BASE_URL = "https://run.mocky.io/v3/"

    val api: AbnApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AbnApiService::class.java)
    }
}