package com.example.theweather.presentation.mainFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.theweather.domain.controllers.SelectedWeatherProvider
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.usecase.*
import com.example.theweather.utils.Resource
import com.example.theweather.utils.TemperatureUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class MainViewModel constructor(
    private val getCurrentWeatherModelByCoordinatesUseCase: GetCurrentWeatherModelUseCase,
    private val getCurrentWeatherModelByCityUseCase: GetCurrentWeatherModelByCityUseCase,
    private val addNewWeatherModelUseCase: AddNewWeatherModelUseCase,
    private val changeCurrentTemperatureUnitsTypeUseCase: ChangeCurrentTemperatureUnitsTypeUseCase,
    private val getSelectedWeatherUseCase: GetSelectedWeatherUseCase
) :
    ViewModel() {
    val selectedWeatherModel = getSelectedWeatherUseCase.execute()

    fun switchToCelsius() {
        changeCurrentTemperatureUnitsTypeUseCase.execute(TemperatureUtils.TemperatureUnitsType.CELSIUS)
    }

    fun switchToFahrenheit() {
        changeCurrentTemperatureUnitsTypeUseCase.execute(TemperatureUtils.TemperatureUnitsType.FAHRENHEIT)
    }

    fun getWeatherByCityName(cityName: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val currentWeather = getCurrentWeatherAsync(cityName)
            addWeatherToStorage(currentWeather)
            emit(Resource.success(data = currentWeather))
        } catch (exception: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = exception.message ?: "getWeatherByCoordinates: something went wrong!"
                )
            )
        }
    }

    fun getWeatherByCoordinates(latitude: Double, longitude: Double) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val currentWeather = getCurrentWeatherAsync(latitude, longitude)
            addWeatherToStorage(currentWeather)
            emit(Resource.success(data = currentWeather))
        } catch (exception: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = exception.message ?: "getWeatherByCoordinates: something went wrong!"
                )
            )
        }
    }

    private suspend fun getCurrentWeatherAsync(latitude: Double, longitude: Double): WeatherModel {
        val model = viewModelScope.async(Dispatchers.IO) {
            getCurrentWeatherModelByCoordinatesUseCase.execute(
                latitude,
                longitude
            )
        }
        return model.await()
    }

    private suspend fun getCurrentWeatherAsync(cityName: String): WeatherModel {
        val model = viewModelScope.async(Dispatchers.IO) {
            getCurrentWeatherModelByCityUseCase.execute(
                cityName
            )
        }
        return model.await()
    }

    private fun addWeatherToStorage(weatherModel: WeatherModel) {
        addNewWeatherModelUseCase.execute(weatherModel)
    }

    class Factory @Inject constructor(
        private val getCurrentWeatherModelUseCase: GetCurrentWeatherModelUseCase,
        private val getCurrentWeatherModelByCityUseCase: GetCurrentWeatherModelByCityUseCase,
        private val addNewWeatherModelUseCase: AddNewWeatherModelUseCase,
        private val changeCurrentTemperatureUnitsTypeUseCase: ChangeCurrentTemperatureUnitsTypeUseCase,
        private val getSelectedWeatherUseCase: GetSelectedWeatherUseCase

    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(
                getCurrentWeatherModelUseCase,
                getCurrentWeatherModelByCityUseCase,
                addNewWeatherModelUseCase,
                changeCurrentTemperatureUnitsTypeUseCase,
                getSelectedWeatherUseCase
            ) as T
        }
    }
}