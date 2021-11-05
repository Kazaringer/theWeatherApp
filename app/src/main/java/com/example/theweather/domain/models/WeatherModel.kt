package com.example.theweather.domain.models

import java.io.Serializable
import java.util.*

data class WeatherModel(
    var city: String,
    var temperatureFahrenheit: Double,
    var temperatureCelsius: Double,
    var dateTime: Date
) : Serializable

