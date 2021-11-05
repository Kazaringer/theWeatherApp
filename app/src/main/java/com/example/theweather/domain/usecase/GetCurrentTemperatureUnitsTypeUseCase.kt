package com.example.theweather.domain.usecase

import androidx.lifecycle.LiveData
import com.example.theweather.domain.controllers.TemperatureUnitsTypeSwitcher
import com.example.theweather.utils.TemperatureUtils
import javax.inject.Inject

class GetCurrentTemperatureUnitsTypeUseCase @Inject constructor(private val temperatureUnitsTypeSwitcher: TemperatureUnitsTypeSwitcher) {
    fun execute(): LiveData<TemperatureUtils.TemperatureUnitsType> =
        temperatureUnitsTypeSwitcher.getCurrentTemperatureUnitsType()
}