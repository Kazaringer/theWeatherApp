package com.example.theweather.data.storage

import androidx.lifecycle.LiveData
import com.example.theweather.data.storage.Models.WeatherModel


interface WeatherStorage {

    suspend fun get(): List<WeatherModel>
    suspend fun set(weatherModel: WeatherModel)
}