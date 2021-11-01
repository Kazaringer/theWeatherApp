package com.example.theweather.data.storage.Models

import io.realm.annotations.Required
import java.util.*

data class WeatherModel
    (
    var city: String,
    var temperatureFahrenheit: Double,
    var temperatureCelsius: Double,
    var dateTime: Date
)

