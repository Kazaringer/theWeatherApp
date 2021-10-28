package com.example.theweather.presentation.MainFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.usecase.GetSavedWeatherModelsUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(getSavedWeatherModelsUseCase: GetSavedWeatherModelsUseCase) :
    ViewModel() {

    private val weatherModelsMutableLiveData = MutableLiveData<ArrayList<WeatherModel>>()
    val weatherModelsLiveData: LiveData<ArrayList<WeatherModel>> = weatherModelsMutableLiveData

    private val weatherModels = ArrayList<WeatherModel>()

    init {
        weatherModels.addAll(getSavedWeatherModelsUseCase.execute())
        weatherModelsMutableLiveData.value = weatherModels
    }

    class Factory @Inject constructor(private val getSavedWeatherModelsUseCase: GetSavedWeatherModelsUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(getSavedWeatherModelsUseCase) as T;
        }

    }
}