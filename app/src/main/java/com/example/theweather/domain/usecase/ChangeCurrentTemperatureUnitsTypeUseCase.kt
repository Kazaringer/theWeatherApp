package com.example.theweather.domain.usecase

import com.example.theweather.domain.controllers.TemperatureUnitsTypeSwitcher
import com.example.theweather.utils.TemperatureUtils
import javax.inject.Inject

class ChangeCurrentTemperatureUnitsTypeUseCase @Inject constructor(private val temperatureUnitsTypeSwitcher: TemperatureUnitsTypeSwitcher) {
    fun execute(temperatureUnitsType: TemperatureUtils.TemperatureUnitsType) {
        temperatureUnitsTypeSwitcher.changeCurrentTemperatureUnitsType(temperatureUnitsType)
    }
}