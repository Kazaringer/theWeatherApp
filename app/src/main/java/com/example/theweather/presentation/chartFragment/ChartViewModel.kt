package com.example.theweather.presentation.chartFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.theweather.domain.usecase.GetCurrentTemperatureUnitsTypeUseCase
import com.example.theweather.utils.TemperatureUtils
import javax.inject.Inject

class ChartViewModel(
    private val getCurrentTemperatureUnitsTypeUseCase: GetCurrentTemperatureUnitsTypeUseCase,
) : ViewModel() {

    fun getCurrentTemperatureUnitsType(): LiveData<TemperatureUtils.TemperatureUnitsType> =
        getCurrentTemperatureUnitsTypeUseCase.execute()

    class Factory @Inject constructor(
        private val getCurrentTemperatureUnitsTypeUseCase: GetCurrentTemperatureUnitsTypeUseCase,
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChartViewModel(
                getCurrentTemperatureUnitsTypeUseCase
            ) as T;
        }
    }
}