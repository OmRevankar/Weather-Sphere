package com.example.weathersphere.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/v1/current.json")
    //making suspend fun as it is an asyncronous call
    suspend fun getWeather(
        @Query("key") apikey : String,
        @Query("q") city : String,
    ) : Response<WeatherModel>
}