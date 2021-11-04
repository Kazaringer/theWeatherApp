package com.example.theweather.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val weatherList = mutableListOf<WeatherModel>()
    private val weatherListLiveData = MutableLiveData<List<WeatherModel>>()

    override suspend fun getWeatherModels(): LiveData<List<WeatherModel>> {

        if (weatherListLiveData.value != null) {
            return weatherListLiveData
        }

        val storageWeatherModels = coroutineScope { weatherStorage.get() }

        for (storageWeatherModel in storageWeatherModels) {
            weatherList.add(convertModels(storageWeatherModel))
        }

        weatherListLiveData.value = weatherList;

        return weatherListLiveData
    }

    override suspend fun saveWeatherModel(weatherModel: WeatherModel) {
        val storageWeatherModel = convertModels(weatherModel);
        weatherStorage.set(storageWeatherModel)
        weatherList.add(weatherModel)
        weatherListLiveData.value = weatherList;
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