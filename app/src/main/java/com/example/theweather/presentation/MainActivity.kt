package com.example.theweather.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.theweather.R
import com.example.theweather.presentation.mainFragment.MainFragment
import dagger.Lazy
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        applicationComponent.inject(this)

        setSupportActionBar(findViewById(R.id.toolBar))
    }
}