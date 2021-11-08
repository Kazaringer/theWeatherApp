package com.example.theweather.domain.usecase

import androidx.lifecycle.LiveData
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetSavedWeatherModelsUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    fun execute(): LiveData<List<WeatherModel>> = weatherRepository.getLocalWeatherModels()

}