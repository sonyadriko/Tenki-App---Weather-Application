package com.example.tenki

import com.example.tenki.model.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("/v1/current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String
    ): WeatherData
//    @GET("/v1/current.json")
//    suspend fun getCurrentWeather(
//        @Query("key") apiKey: String,
//        @Query("lat") latitude: Double?,
//        @Query("lon") longitude: Double?
//    ): WeatherData

}