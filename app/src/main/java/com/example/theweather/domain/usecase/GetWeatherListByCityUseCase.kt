package com.example.theweather.domain.usecase

import com.example.theweather.domain.models.WeatherList
import com.example.theweather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherListByCityUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    fun execute(cityName: String): WeatherList =
        weatherRepository.getLocalWeatherList(cityName)

}