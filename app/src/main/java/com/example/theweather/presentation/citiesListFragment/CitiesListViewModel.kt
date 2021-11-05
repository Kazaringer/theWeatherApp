package com.example.theweather.presentation.citiesListFragment

import androidx.lifecycle.*
import com.example.theweather.domain.models.WeatherList
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.usecase.ChangeCurrentTemperatureUnitsTypeUseCase
import com.example.theweather.domain.usecase.GetCurrentTemperatureUnitsTypeUseCase
import com.example.theweather.domain.usecase.GetSavedWeatherModelsUseCase
import com.example.theweather.utils.Resource
import com.example.theweather.utils.TemperatureUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CitiesListViewModel @Inject constructor() : ViewModel() {

}