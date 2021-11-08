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
import com.example.theweather.domain.usecase.GetWeatherListByCityUseCase
import com.example.theweather.domain.usecase.SelectWeatherUseCase
import com.example.theweather.utils.TemperatureUtils
import javax.inject.Inject

class WeatherByCityListViewModel(
    private val getSavedWeatherModelsUseCase: GetSavedWeatherModelsUseCase,
    private val getCurrentTemperatureUnitsTypeUseCase: GetCurrentTemperatureUnitsTypeUseCase,
    private val selectWeatherUseCase: SelectWeatherUseCase,
    private val getWeatherListByCityUseCase: GetWeatherListByCityUseCase
) : ViewModel() {

    private val weatherLists = mutableListOf<WeatherList>()
    private val weatherListModelByCity = mutableMapOf<String, WeatherList>()
    private var weatherListsMutableLiveData = MutableLiveData<List<WeatherList>>(weatherLists)
    val weatherListsLiveData: LiveData<List<WeatherList>> = weatherListsMutableLiveData

    fun getCurrentTemperatureUnitsType(): LiveData<TemperatureUtils.TemperatureUnitsType> =
        getCurrentTemperatureUnitsTypeUseCase.execute()

    fun getWeatherModels(): LiveData<List<WeatherModel>> {
        return getSavedWeatherModelsUseCase.execute()
    }

    fun updateData(weatherModels: List<WeatherModel>) {
        weatherListModelByCity.clear()

        for (weatherModel in weatherModels) {
            val cityName = weatherModel.city
            if (weatherListModelByCity.contains(cityName))
                continue

            weatherListModelByCity[cityName] = getWeatherListByCityUseCase.execute(cityName)
        }

        val selectedModel = weatherModels.maxByOrNull { it.dateTime }
        selectedModel?.let { selectWeatherUseCase.execute(it) }
        onWeatherListChangedNotify()
    }

    private fun onWeatherListChangedNotify() {
        weatherLists.clear()
        weatherLists.addAll(weatherListModelByCity.values)
        weatherListsMutableLiveData.value = weatherLists
    }

    class Factory @Inject constructor(
        private val getSavedWeatherModelsUseCase: GetSavedWeatherModelsUseCase,
        private val getCurrentTemperatureUnitsTypeUseCase: GetCurrentTemperatureUnitsTypeUseCase,
        private val selectWeatherUseCase: SelectWeatherUseCase,
        private val getWeatherListByCityUseCase: GetWeatherListByCityUseCase
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WeatherByCityListViewModel(
                getSavedWeatherModelsUseCase,
                getCurrentTemperatureUnitsTypeUseCase,
                selectWeatherUseCase,
                getWeatherListByCityUseCase
            ) as T
        }
    }
}