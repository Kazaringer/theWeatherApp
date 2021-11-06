package com.example.theweather.domain.repository

import androidx.lifecycle.LiveData
import com.example.theweather.domain.models.WeatherList
import com.example.theweather.domain.models.WeatherModel

interface WeatherRepository {
    val isLoaded: LiveData<Boolean>
    fun getLocalWeatherModels(): LiveData<List<WeatherModel>>
    fun saveLocalWeatherModel(weatherModel: WeatherModel)
    fun getLocalWeatherList(city: String): WeatherList
    suspend fun getCurrentWeatherModelByCoordinates(
        latitude: Double,
        longitude: Double
    ): WeatherModel
}