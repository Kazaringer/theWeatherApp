package com.example.theweather.presentation.chartFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.theweather.domain.controllers.SelectedWeatherProvider
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.usecase.GetCurrentTemperatureUnitsTypeUseCase
import com.example.theweather.domain.usecase.GetSelectedWeatherUseCase
import com.example.theweather.domain.usecase.GetWeatherListByCityUseCase
import com.example.theweather.domain.usecase.SelectWeatherUseCase
import com.example.theweather.utils.TemperatureUtils
import javax.inject.Inject

class ChartViewModel(
    private val getCurrentTemperatureUnitsTypeUseCase: GetCurrentTemperatureUnitsTypeUseCase,
    private val getWeatherListByCityUseCase: GetWeatherListByCityUseCase,
    private val selectWeatherUseCase: SelectWeatherUseCase
) : ViewModel() {

    fun getCurrentTemperatureUnitsType(): LiveData<TemperatureUtils.TemperatureUnitsType> =
        getCurrentTemperatureUnitsTypeUseCase.execute()

    fun getWeatherList(cityName: String) = getWeatherListByCityUseCase.execute(cityName)

    fun onWeatherListUpdate(weatherModels: List<WeatherModel>) {
        val lastModel = weatherModels.maxByOrNull { it.dateTime }
        lastModel?.let { selectWeatherUseCase.execute(it) }
    }

    class Factory @Inject constructor(
        private val getCurrentTemperatureUnitsTypeUseCase: GetCurrentTemperatureUnitsTypeUseCase,
        private val getWeatherListByCityUseCase: GetWeatherListByCityUseCase,
        private val selectWeatherUseCase: SelectWeatherUseCase
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChartViewModel(
                getCurrentTemperatureUnitsTypeUseCase,
                getWeatherListByCityUseCase,
                selectWeatherUseCase
            ) as T
        }
    }
}