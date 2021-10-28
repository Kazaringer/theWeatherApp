package com.example.theweather.presentation.di

import com.example.theweather.data.repository.WeatherRepositoryImpl
import com.example.theweather.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

}

@Module
interface ApplicationBindModule {
    @Binds
    fun provideWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository
}