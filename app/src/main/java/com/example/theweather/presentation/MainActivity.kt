package com.example.theweather.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.example.theweather.R
import com.google.android.libraries.places.api.Places

class MainActivity : AppCompatActivity() {
    private val GOOGLE_API_KEY = "AIzaSyBv1nUCYpkb0x2_Lmrsh1CXKfkSRXOOFBc"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setupGooglePlaces()

        applicationComponent.inject(this)

    }

    private fun setupGooglePlaces() {
        Places.initialize(applicationContext, GOOGLE_API_KEY)
        val placeClient = Places.createClient(this)
    }
}