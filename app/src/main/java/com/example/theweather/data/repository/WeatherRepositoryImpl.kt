package com.example.theweather.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.theweather.data.storage.CurrentWeatherProvider
import com.example.theweather.data.storage.WeatherStorage
import com.example.theweather.domain.models.WeatherList
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.repository.WeatherRepository
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherProvider: CurrentWeatherProvider,
    private val weatherStorage: WeatherStorage
) : WeatherRepository {

    private val isLoadedMutable = MutableLiveData<Boolean>(false)
    override val isLoaded: LiveData<Boolean> = isLoadedMutable

    private val weatherListByCity = mutableMapOf<String, WeatherList>()
    private val weatherModels = mutableListOf<WeatherModel>()
    private var weatherListLiveData = MutableLiveData<List<WeatherModel>>()

    init {
        GlobalScope.launch(Dispatchers.IO) { load() }
    }

    private suspend fun load(): LiveData<Boolean> =
        coroutineScope {
            if (isLoaded.value == true)
                return@coroutineScope isLoaded

            val storageWeatherModels = async { weatherStorage.get() }
            launch(Dispatchers.Main) {
                for (storageWeatherModel in storageWeatherModels.await()) {
                    cacheModel(storageWeatherModel)
                }
                weatherListLiveData.value = weatherModels
            }

            return@coroutineScope isLoaded
        }


    private fun cacheModel(weatherModel: com.example.theweather.data.storage.Models.WeatherModel) {
        val convertedModel = convertModels(weatherModel);

        weatherModels.add(convertedModel)

        val city = weatherModel.city
        if (!weatherListByCity.containsKey(city))
            weatherListByCity[city] = WeatherList()

        weatherListByCity[city]?.addModel(convertedModel)
        weatherListLiveData.value = weatherModels
    }

    override fun getLocalWeatherModels(): LiveData<List<WeatherModel>> {
        return weatherListLiveData;
    }


    override fun saveLocalWeatherModel(weatherModel: WeatherModel) {
        val storageWeatherModel = convertModels(weatherModel)
        GlobalScope.launch(Dispatchers.Main) { cacheModel(storageWeatherModel) }
        GlobalScope.launch { weatherStorage.set(storageWeatherModel) }
    }

    override fun getLocalWeatherList(city: String): WeatherList {
        if (!weatherListByCity.containsKey(city))
            weatherListByCity[city] = WeatherList()

        return weatherListByCity[city]!!
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