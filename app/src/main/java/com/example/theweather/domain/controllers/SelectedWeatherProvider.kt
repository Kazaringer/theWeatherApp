package com.example.theweather.domain.controllers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.theweather.domain.models.WeatherModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectedWeatherProvider @Inject constructor() {
    private val currentWeatherModelMutable = MutableLiveData<WeatherModel>()

    fun getSelectedWeatherModel(): LiveData<WeatherModel> {
        return currentWeatherModelMutable
    }

    fun setSelectedWeatherModel(weatherModel: WeatherModel) {
        currentWeatherModelMutable.value = weatherModel
    }
}