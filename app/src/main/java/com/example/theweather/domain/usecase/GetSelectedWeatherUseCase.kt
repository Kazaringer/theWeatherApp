package com.example.theweather.domain.usecase

import androidx.lifecycle.LiveData
import com.example.theweather.domain.controllers.SelectedWeatherProvider
import com.example.theweather.domain.models.WeatherModel
import javax.inject.Inject

class GetSelectedWeatherUseCase @Inject constructor(private val selectedWeatherProvider: SelectedWeatherProvider) {
    fun execute(): LiveData<WeatherModel> = selectedWeatherProvider.getSelectedWeatherModel()
}