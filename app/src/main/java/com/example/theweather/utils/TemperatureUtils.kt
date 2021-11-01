package com.example.theweather.utils

import java.math.RoundingMode
import java.text.DecimalFormat

class TemperatureUtils {
    enum class TemperatureUnitsType {
        CELSIUS, FAHRENHEIT
    }

    companion object {
        fun fahrenheitToCelsius(temperature: Double): Double {
            val celsius = ((temperature - 32) * 5) / 9
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            return df.format(celsius).toDouble()
        }
    }
}