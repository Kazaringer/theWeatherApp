package com.example.theweather.domain.controllers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.theweather.utils.TemperatureUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemperatureUnitsTypeSwitcher @Inject constructor() {
    private val currentTemperatureUnitsType =
        MutableLiveData(TemperatureUtils.TemperatureUnitsType.FAHRENHEIT)

    fun getCurrentTemperatureUnitsType(): LiveData<TemperatureUtils.TemperatureUnitsType> {
        return currentTemperatureUnitsType;
    }

    fun changeCurrentTemperatureUnitsType(temperatureUnitsType: TemperatureUtils.TemperatureUnitsType) {
        currentTemperatureUnitsType.value = temperatureUnitsType
    }
}