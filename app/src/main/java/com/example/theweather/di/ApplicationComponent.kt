package com.example.theweather.di

import com.example.theweather.presentation.MainActivity
import com.example.theweather.presentation.mainFragment.MainFragment
import com.example.theweather.presentation.chartFragment.ChartFragment
import com.example.theweather.presentation.citiesListFragment.CitiesListFragment
import com.example.theweather.presentation.weatherByCityListFragment.WeatherByCityListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ApplicationBindModule::class, NetworkModule::class, DBModule::class])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainActivity: MainFragment)
    fun inject(chartFragment: ChartFragment)
    fun inject(citiesListFragment: CitiesListFragment)
    fun inject(weatherByCityListFragment: WeatherByCityListFragment)

}