package com.example.theweather.data.storage

import com.example.theweather.data.storage.Models.WeatherModel


interface WeatherStorage {

    suspend fun get(): List<WeatherModel>
    suspend fun set(weatherModel: WeatherModel)
}