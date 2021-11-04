package com.example.theweather.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.theweather.R

class MainActivity : AppCompatActivity() {

   // @Inject
   // lateinit var mainFragment: Lazy<MainFragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        applicationComponent.inject(this)

        if (savedInstanceState == null) {
           // supportFragmentManager.beginTransaction()
               // .replace(R.id.container, mainFragment.get())
              //  .commitNow()
        }
    }
}