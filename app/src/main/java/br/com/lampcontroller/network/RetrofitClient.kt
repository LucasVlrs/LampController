package br.com.lampcontroller.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    // Trocar pra o IP do ESP
    private const val BASE_URL = "http://SEU_IP_DO_ESP32/"

    val instance: Esp32Api by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(Esp32Api::class.java)
    }
}
