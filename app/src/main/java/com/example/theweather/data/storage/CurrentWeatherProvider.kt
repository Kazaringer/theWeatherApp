package com.example.theweather.data.storage

import com.example.theweather.data.storage.Models.WeatherModel

interface CurrentWeatherProvider {
    suspend fun getModelByCoordinates(latitude: Double, longitude: Double): WeatherModel
}