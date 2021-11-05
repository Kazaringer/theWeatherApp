package com.example.theweather.di

import com.example.theweather.data.repository.WeatherRepositoryImpl
import com.example.theweather.data.storage.CurrentWeatherProvider
import com.example.theweather.data.storage.LocalWeatherStorage
import com.example.theweather.data.storage.NetworkWeatherProvider
import com.example.theweather.data.storage.WeatherStorage
import com.example.theweather.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module

@Module
class ApplicationModule {

}

@Module
interface ApplicationBindModule {
    @Binds
    fun provideWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    fun provideWeatherStorage(localWeatherStorage: LocalWeatherStorage): WeatherStorage

    @Binds
    fun provideWeatherProvider(networkWeatherProvider: NetworkWeatherProvider): CurrentWeatherProvider
}