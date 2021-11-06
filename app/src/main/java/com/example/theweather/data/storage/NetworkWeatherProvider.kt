package com.example.theweather.data.storage

import com.example.theweather.data.services.NetworkWeatherService
import com.example.theweather.data.storage.Models.NetworkModels.NetworkWeatherModel
import com.example.theweather.data.storage.Models.WeatherModel
import com.example.theweather.utils.TemperatureUtils
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NetworkWeatherProvider @Inject constructor(private val networkWeatherService: NetworkWeatherService) :
    CurrentWeatherProvider {

    private val API_KEY = "56b1382eb3c79443540f88b7996c07d8";

    override suspend fun provide(): WeatherModel {
        val networkModel = networkWeatherService.getCurrentWeather("Agades", API_KEY)
        return convertModels(networkModel)
    }

    private fun convertModels(networkWeatherModel: NetworkWeatherModel): WeatherModel {

        val dateFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val netDate = Date()
        dateFormat.format(netDate)

        return WeatherModel(
            city = networkWeatherModel.name,
            temperatureFahrenheit = networkWeatherModel.main.temp,
            temperatureCelsius = TemperatureUtils.fahrenheitToCelsius(networkWeatherModel.main.temp),
            dateTime = netDate
        )
    }

}