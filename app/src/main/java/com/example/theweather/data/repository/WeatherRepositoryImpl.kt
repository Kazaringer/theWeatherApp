package com.example.theweather.data.repository

import com.example.theweather.data.storage.CurrentWeatherProvider
import com.example.theweather.data.storage.WeatherStorage
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.repository.WeatherRepository
import com.example.theweather.utils.TemperatureUtils
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    val networkWeatherProvider: CurrentWeatherProvider,
    val weatherStorage: WeatherStorage
) : WeatherRepository {

    override suspend fun getWeatherModels(): List<WeatherModel> {
        val storageWeatherModels = coroutineScope { weatherStorage.get() }

        val weatherModels = mutableListOf<WeatherModel>()

        for (storageWeatherModel in storageWeatherModels) {
            weatherModels.add(convertModels(storageWeatherModel))
        }

        return weatherModels
    }

    override suspend fun saveWeatherModel(weatherModel: WeatherModel) {
        val storageWeatherModel = convertModels(weatherModel);
        weatherStorage.set(storageWeatherModel)
    }

    override suspend fun getCurrentWeatherModel(): WeatherModel {
        val weatherModel = networkWeatherProvider.provide()
        return convertModels(weatherModel)
    }

    private fun convertModels(weatherModel: com.example.theweather.data.storage.Models.WeatherModel): WeatherModel {
        return WeatherModel(
            city = weatherModel.city,
            temperatureFahrenheit = weatherModel.temperatureFahrenheit,
            temperatureCelsius = weatherModel.temperatureCelsius,
            dateTime = weatherModel.dateTime,
        )
    }

    private fun convertModels(weatherModel: WeatherModel): com.example.theweather.data.storage.Models.WeatherModel {
        return com.example.theweather.data.storage.Models.WeatherModel(
            city = weatherModel.city,
            temperatureFahrenheit = weatherModel.temperatureFahrenheit,
            temperatureCelsius = weatherModel.temperatureCelsius,
            dateTime = weatherModel.dateTime
        )
    }
}