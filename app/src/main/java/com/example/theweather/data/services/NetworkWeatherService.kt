package com.example.theweather.data.services

import com.example.theweather.data.storage.Models.NetworkModels.NetworkWeatherModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface NetworkWeatherService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") user: String,
        @Query("appid") apiKey: String?,
        @Query("units") units: String = "imperial"
    ): NetworkWeatherModel
}