package com.example.theweather.domain.usecase

import android.content.Context
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.repository.WeatherRepository
import com.example.theweather.presentation.applicationComponent
import java.util.*
import javax.inject.Inject

class GetSavedWeatherModelsUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    fun execute(): List<WeatherModel> {
        return weatherRepository.getWeatherModels()
    }
}