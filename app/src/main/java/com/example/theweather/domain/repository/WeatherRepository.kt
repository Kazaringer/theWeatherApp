package com.example.theweather.domain.repository

import com.example.theweather.domain.models.WeatherModel

interface WeatherRepository {
    suspend fun getWeatherModels(): List<WeatherModel>
    suspend fun saveWeatherModel(weatherModel: WeatherModel)
    suspend fun getCurrentWeatherModel(): WeatherModel
}