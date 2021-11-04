package com.example.theweather.domain.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.theweather.data.storage.Models.NetworkModels.Weather

class WeatherList {
    private var previewMutable = MutableLiveData<WeatherModel>()
    var preview: LiveData<WeatherModel> = previewMutable

     val weatherModels: MutableList<WeatherModel> = mutableListOf()
    val iterator: MutableIterator<WeatherModel> = weatherModels.iterator()


    fun addModel(weatherModel: WeatherModel) {
        if (previewMutable.value == null || previewMutable.value!!.dateTime < weatherModel.dateTime)
            previewMutable.value = weatherModel

        weatherModels.add(weatherModel)
    }

    fun sort() {
        weatherModels.sortBy { it.dateTime }
    }
}