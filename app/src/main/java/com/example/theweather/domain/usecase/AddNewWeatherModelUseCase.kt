package com.example.theweather.domain.usecase

import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.repository.WeatherRepository
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class AddNewWeatherModelUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend fun execute(weatherModel: WeatherModel) =
        weatherRepository.saveWeatherModel(weatherModel = weatherModel)

}


