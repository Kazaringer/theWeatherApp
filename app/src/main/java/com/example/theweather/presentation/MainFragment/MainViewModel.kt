package com.example.theweather.presentation.MainFragment

import android.util.Log
import androidx.lifecycle.*
import com.example.theweather.data.storage.Models.NetworkModels.Weather
import com.example.theweather.domain.models.WeatherList
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.usecase.AddNewWeatherModelUseCase
import com.example.theweather.domain.usecase.GetCurrentWeatherModelUseCase
import com.example.theweather.domain.usecase.GetSavedWeatherModelsUseCase
import com.example.theweather.utils.Resource
import com.example.theweather.utils.TemperatureUtils
import dagger.assisted.Assisted
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class MainViewModel @Inject constructor(
    private val getSavedWeatherModelsUseCase: GetSavedWeatherModelsUseCase,
    private val getCurrentWeatherModelUseCase: GetCurrentWeatherModelUseCase,
    private val addNewWeatherModelUseCase: AddNewWeatherModelUseCase
) :
    ViewModel() {

    private val temperatureUnitsTypeMutable =
        MutableLiveData<TemperatureUtils.TemperatureUnitsType>()
    val temperatureUnitsType: LiveData<TemperatureUtils.TemperatureUnitsType> =
        temperatureUnitsTypeMutable

    private val weatherListMutableLiveData = MutableLiveData<MutableList<WeatherModel>>()
    val weatherListLiveData: LiveData<MutableList<WeatherModel>> = weatherListMutableLiveData
    private val weatherList = mutableListOf<WeatherModel>()

    private val weatherListByCity = mutableMapOf<String, WeatherList>()
    private val weatherPreviewByCity = mutableMapOf<String, WeatherModel>()

    init {
        weatherListMutableLiveData.value = weatherList
    }

    fun switchToCelsius() {
        temperatureUnitsTypeMutable.value = TemperatureUtils.TemperatureUnitsType.CELSIUS
    }

    fun switchToFahrenheit() {
        temperatureUnitsTypeMutable.value = TemperatureUtils.TemperatureUnitsType.FAHRENHEIT
    }

    fun updateSavedWeather() {
        viewModelScope.launch {
            updateSavedWeatherAsync()
        }
    }

    private suspend fun updateSavedWeatherAsync() {
        val storageModels = viewModelScope.async(Dispatchers.IO) { getSavedWeatherModels() }
        val models = storageModels.await()
        weatherListByCity.clear()

        for (weatherModel in models) {
            addWeatherModel(weatherModel)
        }

        onWeatherListChangedNotify()
    }

    private fun onWeatherListChangedNotify() {
        weatherList.clear()
        weatherList.addAll(weatherPreviewByCity.values)
        weatherListMutableLiveData.value = weatherList;
    }

    private suspend fun getSavedWeatherModels(): List<WeatherModel> {
        return getSavedWeatherModelsUseCase.execute()
    }

    fun getCurrentWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentWeather = getCurrentWeatherAsync()
                addWeatherToStorage(currentWeather)
                CoroutineScope(coroutineContext).launch(Dispatchers.Main) {
                    addWeatherModel(currentWeather)
                    onWeatherListChangedNotify()
                }
            } catch (exception: Exception) {
                Log.e(
                    MainViewModel::class.toString(),
                    exception.message ?: "getCurrentWeather: something went wrong!"
                )
            }
        }

    }

    private suspend fun getCurrentWeatherAsync(): WeatherModel {

        val model = viewModelScope.async(Dispatchers.IO) { getCurrentWeatherModelUseCase.execute() }
        return model.await()
    }

    private suspend fun addWeatherToStorage(weatherModel: WeatherModel) {
        addNewWeatherModelUseCase.execute(weatherModel)
    }


    private fun addWeatherModel(weatherModel: WeatherModel) {
        val cityName = weatherModel.city

        if (!weatherListByCity.containsKey(cityName)) {
            weatherListByCity[cityName] = WeatherList()
        }

        weatherListByCity[cityName]?.addModel(weatherModel)

        if (!weatherPreviewByCity.containsKey(cityName)) {
            weatherPreviewByCity[cityName] = weatherModel
            return;
        }

        if (weatherPreviewByCity[cityName]!!.dateTime < weatherModel.dateTime) {
            weatherPreviewByCity[cityName] = weatherModel
        }

    }

    class Factory @Inject constructor(
        private val getSavedWeatherModelsUseCase: GetSavedWeatherModelsUseCase,
        private val getCurrentWeatherModelUseCase: GetCurrentWeatherModelUseCase,
        private val addNewWeatherModelUseCase: AddNewWeatherModelUseCase
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(
                getSavedWeatherModelsUseCase,
                getCurrentWeatherModelUseCase,
                addNewWeatherModelUseCase
            ) as T;
        }
    }

}