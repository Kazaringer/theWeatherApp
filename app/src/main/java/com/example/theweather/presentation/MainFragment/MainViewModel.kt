package com.example.theweather.presentation.mainFragment

import androidx.lifecycle.*
import com.example.theweather.domain.controllers.SelectedWeatherProvider
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.usecase.AddNewWeatherModelUseCase
import com.example.theweather.domain.usecase.ChangeCurrentTemperatureUnitsTypeUseCase
import com.example.theweather.domain.usecase.GetCurrentWeatherModelUseCase
import com.example.theweather.utils.DebugConsole
import com.example.theweather.utils.TemperatureUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel constructor(
    private val getCurrentWeatherModelByCoordinatesUseCase: GetCurrentWeatherModelUseCase,
    private val addNewWeatherModelUseCase: AddNewWeatherModelUseCase,
    private val changeCurrentTemperatureUnitsTypeUseCase: ChangeCurrentTemperatureUnitsTypeUseCase,
    private val selectedWeatherProvider: SelectedWeatherProvider
) :
    ViewModel() {
    val selectedWeatherModel = selectedWeatherProvider.getSelectedWeatherModel()

    fun switchToCelsius() {
        changeCurrentTemperatureUnitsTypeUseCase.execute(TemperatureUtils.TemperatureUnitsType.CELSIUS)
    }

    fun switchToFahrenheit() {
        changeCurrentTemperatureUnitsTypeUseCase.execute(TemperatureUtils.TemperatureUnitsType.FAHRENHEIT)
    }

    fun getWeatherByCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentWeather = getCurrentWeatherAsync(latitude, longitude)
                addWeatherToStorage(currentWeather)
            } catch (exception: Exception) {
                DebugConsole.error(
                    this,
                    exception.message ?: "getCurrentWeather: something went wrong!"
                )
            }
        }
    }

    private suspend fun getCurrentWeatherAsync(latitude: Double, longitude: Double): WeatherModel {

        val model = viewModelScope.async(Dispatchers.IO) { getCurrentWeatherModelByCoordinatesUseCase.execute(latitude,longitude) }
        return model.await()
    }

    private fun addWeatherToStorage(weatherModel: WeatherModel) {
        addNewWeatherModelUseCase.execute(weatherModel)
    }


    class Factory @Inject constructor(
        private val getCurrentWeatherModelUseCase: GetCurrentWeatherModelUseCase,
        private val addNewWeatherModelUseCase: AddNewWeatherModelUseCase,
        private val changeCurrentTemperatureUnitsTypeUseCase: ChangeCurrentTemperatureUnitsTypeUseCase,
        private val selectedWeatherProvider: SelectedWeatherProvider
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(
                getCurrentWeatherModelUseCase,
                addNewWeatherModelUseCase,
                changeCurrentTemperatureUnitsTypeUseCase,
                selectedWeatherProvider
            ) as T;
        }
    }
}