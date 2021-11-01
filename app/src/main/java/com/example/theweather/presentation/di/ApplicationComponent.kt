package com.example.theweather.presentation.di

import com.example.theweather.presentation.MainActivity
import com.example.theweather.presentation.MainFragment.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ApplicationBindModule::class, NetworkModule::class, DBModule::class])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainActivity: MainFragment)

}