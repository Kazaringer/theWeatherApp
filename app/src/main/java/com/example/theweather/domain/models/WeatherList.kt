package com.example.theweather.domain.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.theweather.data.storage.Models.NetworkModels.Weather
import java.io.Serializable

class WeatherList {

    val cityName: String
        get() {
            var name = ""
            preview.value?.city?.let { name = it }
            return name
        }

    private var previewMutable = MutableLiveData<WeatherModel>()
    var preview: LiveData<WeatherModel> = previewMutable

    private val weatherModelsCache: MutableList<WeatherModel> = mutableListOf()
    private val weatherModelsMutable: MutableLiveData<List<WeatherModel>> =
        MutableLiveData<List<WeatherModel>>(weatherModelsCache)
    val weatherModels: LiveData<List<WeatherModel>> = weatherModelsMutable


    fun addModel(weatherModel: WeatherModel) {
        if (previewMutable.value == null || previewMutable.value!!.dateTime < weatherModel.dateTime)
            previewMutable.value = weatherModel

        weatherModelsCache.add(weatherModel)
        weatherModelsMutable.value = weatherModelsCache
    }
}