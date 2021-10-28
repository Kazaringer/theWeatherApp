package com.example.theweather.data.repository

import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.repository.WeatherRepository
import java.util.*
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor() : WeatherRepository {
    override fun getWeatherModels(): List<WeatherModel> {
        return listOf(
            WeatherModel("Petrozavodsk", "37", Date()),
            WeatherModel("Petrozavodsk", "37", Date()),
            WeatherModel("Petrozavodsk", "37", Date())
        )
    }

    override fun getCurrentWeatherModel(): WeatherModel {
        return WeatherModel("Petrozavodsk", "37", Date())
    }

    override fun saveWeatherModel(weatherModel: WeatherModel) {

    }
}