package com.example.theweather.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.theweather.data.storage.CurrentWeatherProvider
import com.example.theweather.data.storage.WeatherStorage
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.repository.WeatherRepository
import com.example.theweather.utils.DebugConsole
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherProvider: CurrentWeatherProvider,
    private val weatherStorage: WeatherStorage
) : WeatherRepository {

    private val weatherList = mutableListOf<WeatherModel>()
    private var weatherListLiveData = MutableLiveData<List<WeatherModel>>()

    override suspend fun getWeatherModels(): LiveData<List<WeatherModel>> =
        coroutineScope {
            if (weatherListLiveData.value != null) {
                return@coroutineScope weatherListLiveData
            }

            val storageWeatherModels = async { weatherStorage.get() }

            launch(Dispatchers.Main) {
                for (storageWeatherModel in storageWeatherModels.await()) {
                    weatherList.add(convertModels(storageWeatherModel))
                }

                weatherListLiveData.value = weatherList
            }

            return@coroutineScope weatherListLiveData
        }


    override suspend fun saveWeatherModel(weatherModel: WeatherModel) {
        val storageWeatherModel = convertModels(weatherModel)
        weatherStorage.set(storageWeatherModel)
        weatherList.add(weatherModel)
        coroutineScope {
            launch(Dispatchers.Main) {
                weatherListLiveData.value = weatherList;
            }
        }
    }

    override suspend fun getCurrentWeatherModel(): WeatherModel {
        val weatherModel = weatherProvider.provide()
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