package br.com.lampcontroller.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Esp32Api {
    @GET("/")
    fun getStatus(): Call<String>

    @GET("/led")
    fun controlLed(@Query("state") state: String): Call<String>
}