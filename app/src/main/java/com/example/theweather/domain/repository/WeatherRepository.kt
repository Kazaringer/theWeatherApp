package com.example.theweather.domain.repository

import com.example.theweather.domain.models.WeatherModel

interface WeatherRepository {
    fun getWeatherModels(): List<WeatherModel>
    fun getCurrentWeatherModel(): WeatherModel
    fun saveWeatherModel(weatherModel: WeatherModel)
}