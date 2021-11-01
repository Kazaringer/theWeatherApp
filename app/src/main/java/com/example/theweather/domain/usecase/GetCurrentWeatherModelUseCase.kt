package com.example.theweather.domain.usecase

import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherModelUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend fun execute(): WeatherModel {
        return weatherRepository.getCurrentWeatherModel()
    }
}