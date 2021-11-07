package com.example.theweather.utils

import java.math.RoundingMode

class TemperatureUtils {
    enum class TemperatureUnitsType {
        CELSIUS, FAHRENHEIT
    }

    companion object {
        fun fahrenheitToCelsius(temperature: Double): Double {
            val celsius = ((temperature - 32) * 5) / 9
            return celsius.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
        }
    }
}