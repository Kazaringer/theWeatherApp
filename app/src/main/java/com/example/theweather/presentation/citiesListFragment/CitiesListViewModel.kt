package com.example.theweather.presentation.citiesListFragment

import androidx.lifecycle.*
import com.example.theweather.domain.models.WeatherList
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.usecase.GetSavedWeatherModelsUseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CitiesListViewModel(
    private val getSavedWeatherModelsUseCase: GetSavedWeatherModelsUseCase,
) : ViewModel() {

    private var weatherListMutableLiveData = MutableLiveData<MutableList<WeatherList>>()
    val weatherListLiveData: LiveData<MutableList<WeatherList>> = weatherListMutableLiveData
    private val weatherLists = mutableListOf<WeatherList>()
    private val weatherListByCity = mutableMapOf<String, WeatherList>()


    init {
        weatherListMutableLiveData.value = weatherLists
    }

    fun getWeatherModels() = liveData(Dispatchers.IO) {
        emit(MutableLiveData())
        val models = getSavedWeatherModelsUseCase.execute()
        emit(models)
    }

    fun updateWeatherLists(weatherModels: List<WeatherModel>) {
        weatherListByCity.clear()
        weatherLists.clear()

        for (weatherModel in weatherModels) {
            addWeatherModel(weatherModel)
        }

        weatherListMutableLiveData.value = weatherLists
    }

    private fun addWeatherModel(weatherModel: WeatherModel) {
        val cityName = weatherModel.city

        if (!weatherListByCity.containsKey(cityName)) {
            val weatherList = WeatherList()
            weatherListByCity[cityName] = weatherList
            weatherLists.add(weatherList)
            onWeatherListChangedNotify()
        }

        weatherListByCity[cityName]?.addModel(weatherModel)
    }

    private fun onWeatherListChangedNotify() {
        weatherListMutableLiveData.value = weatherLists;
    }

    class Factory @Inject constructor(
        private val getSavedWeatherModelsUseCase: GetSavedWeatherModelsUseCase
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CitiesListViewModel(
                getSavedWeatherModelsUseCase
            ) as T;
        }
    }
}