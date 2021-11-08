package com.example.theweather.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.theweather.R
import com.google.android.libraries.places.api.Places

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
        setupGooglePlaces()

        applicationComponent.inject(this)
    }

    private fun setupGooglePlaces() {
        val apiKey = resources.getString(R.string.google_api_key)
        Places.initialize(applicationContext, apiKey)
        val placeClient = Places.createClient(this)
    }
}