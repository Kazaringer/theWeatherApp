package com.example.theweather.domain.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.theweather.data.storage.Models.NetworkModels.Weather

class WeatherList() {
    var preview = MutableLiveData<WeatherModel>()

    private val weatherModels: MutableList<WeatherModel> = mutableListOf()
    val iterator: MutableIterator<WeatherModel> = weatherModels.iterator()


    fun addModel(weatherModel: WeatherModel) {
        if (preview.value == null || preview.value!!.dateTime < weatherModel.dateTime)
            preview.value = weatherModel

        weatherModels.add(weatherModel)
    }

    fun sort() {
        weatherModels.sortBy { it.dateTime }
    }


}