package com.example.theweather.presentation

import android.app.Application
import android.content.Context
import com.example.theweather.presentation.di.ApplicationComponent
import com.example.theweather.presentation.di.DaggerApplicationComponent

class MainApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.create()
    }
}

val Context.applicationComponent: ApplicationComponent
    get() = when (this) {
        is MainApplication -> applicationComponent
        else -> this.applicationContext.applicationComponent
    }