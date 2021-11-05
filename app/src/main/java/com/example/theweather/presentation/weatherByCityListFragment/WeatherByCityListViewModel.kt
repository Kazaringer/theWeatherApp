package com.example.theweather.presentation.weatherByCityListFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.theweather.domain.controllers.SelectedWeatherProvider
import com.example.theweather.domain.models.WeatherList
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.usecase.GetCurrentTemperatureUnitsTypeUseCase
import com.example.theweather.domain.usecase.GetSavedWeatherModelsUseCase
import com.example.theweather.utils.TemperatureUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class WeatherByCityListViewModel(
    private val getSavedWeatherModelsUseCase: GetSavedWeatherModelsUseCase,
    private val getCurrentTemperatureUnitsTypeUseCase: GetCurrentTemperatureUnitsTypeUseCase,
    private val selectedWeatherProvider: SelectedWeatherProvider
) : ViewModel() {

    private var weatherListMutableLiveData = MutableLiveData<MutableList<WeatherList>>()
    val weatherListLiveData: LiveData<MutableList<WeatherList>> = weatherListMutableLiveData
    private val weatherLists = mutableListOf<WeatherList>()
    private val weatherListByCity = mutableMapOf<String, WeatherList>()

    init {
        weatherListMutableLiveData.value = weatherLists
    }

    fun getCurrentTemperatureUnitsType(): LiveData<TemperatureUtils.TemperatureUnitsType> =
        getCurrentTemperatureUnitsTypeUseCase.execute()

    suspend fun getWeatherModels(): LiveData<List<WeatherModel>> {
        val models = coroutineScope {
            async(Dispatchers.IO) {
                getSavedWeatherModelsUseCase.execute()
            }
        }

        return models.await()
    }

    fun updateWeatherLists(weatherModels: List<WeatherModel>) {
        weatherListByCity.clear()
        weatherLists.clear()

        for (weatherModel in weatherModels) {
            addWeatherModel(weatherModel)
        }

        val selectedModel = weatherModels.minByOrNull { it.dateTime }
        selectedModel?.let { selectedWeatherProvider.setSelectedWeatherModel(it) }
        onWeatherListChangedNotify()
    }

    private fun addWeatherModel(weatherModel: WeatherModel) {
        val cityName = weatherModel.city

        if (!weatherListByCity.containsKey(cityName)) {
            val weatherList = WeatherList()
            weatherListByCity[cityName] = weatherList
            weatherLists.add(weatherList)
        }

        weatherListByCity[cityName]?.addModel(weatherModel)
    }

    private fun onWeatherListChangedNotify() {
        weatherListMutableLiveData.value = weatherLists;
    }


    class Factory @Inject constructor(
        private val getSavedWeatherModelsUseCase: GetSavedWeatherModelsUseCase,
        private val getCurrentTemperatureUnitsTypeUseCase: GetCurrentTemperatureUnitsTypeUseCase,
        private val selectedWeatherProvider: SelectedWeatherProvider
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WeatherByCityListViewModel(
                getSavedWeatherModelsUseCase,
                getCurrentTemperatureUnitsTypeUseCase,
                selectedWeatherProvider
            ) as T;
        }
    }

}