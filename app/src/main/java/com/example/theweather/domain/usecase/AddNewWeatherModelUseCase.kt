package com.example.theweather.domain.usecase

import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.repository.WeatherRepository
import javax.inject.Inject

class AddNewWeatherModelUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    fun execute(weatherModel: WeatherModel) =
        weatherRepository.saveLocalWeatherModel(weatherModel = weatherModel)

}


