package com.example.theweather.presentation.mainFragment

import MainViewModel
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import com.example.theweather.R
import com.example.theweather.presentation.applicationComponent

import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.Lazy
import javax.inject.Inject

class MainFragment() : Fragment() {

    @Inject
    lateinit var viewModelFactory: Lazy<MainViewModel.Factory>

    private val viewModel: MainViewModel by viewModels { viewModelFactory.get() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.applicationComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUnitsSwitcher(view)

        val addButton = view.findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            viewModel.getCurrentWeather()
        }
    }


    private fun setupUnitsSwitcher(view: View) {
        val temperatureUnitsTypeSwitch = view.findViewById<SwitchMaterial>(R.id.unitsSwitch)
        temperatureUnitsTypeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.switchToCelsius()
            } else {
                viewModel.switchToFahrenheit()
            }
        }
    }
}