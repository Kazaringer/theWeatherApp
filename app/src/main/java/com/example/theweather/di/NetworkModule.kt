package com.example.theweather.di

import com.example.theweather.data.services.NetworkWeatherService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providesWeatherService(): NetworkWeatherService {

        val retrofit = Retrofit.Builder()

            .baseUrl("https://api.openweathermap.org")

            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(NetworkWeatherService::class.java);
    }
}