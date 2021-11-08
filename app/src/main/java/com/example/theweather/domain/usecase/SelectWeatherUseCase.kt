package com.example.theweather.domain.usecase

import com.example.theweather.domain.controllers.SelectedWeatherProvider
import com.example.theweather.domain.models.WeatherModel
import javax.inject.Inject

class SelectWeatherUseCase @Inject constructor(private val selectedWeatherProvider: SelectedWeatherProvider) {
    fun execute(weatherModel: WeatherModel) {
        selectedWeatherProvider.setSelectedWeatherModel(weatherModel)
    }
}