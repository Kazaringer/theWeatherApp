package com.example.theweather.presentation.mainFragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.theweather.R
import com.example.theweather.presentation.applicationComponent
import com.google.android.material.switchmaterial.SwitchMaterial
import javax.inject.Inject

class MainFragment @Inject constructor() : Fragment(R.layout.main_fragment) {

    @Inject
    lateinit var viewModelFactory: MainViewModel.Factory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private var temperatureCelsiusText: TextView? = null
    private var temperatureFahrenheitText: TextView? = null;
    private var cityText: TextView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.applicationComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViews(view)
        setupUnitsSwitcher(view)
        setupAddButton(view)


        viewModel.selectedWeatherModel.observe(viewLifecycleOwner, { model ->
            if (model == null) return@observe
            temperatureCelsiusText?.text = "${model.temperatureCelsius}°"
            temperatureFahrenheitText?.text = "${model.temperatureFahrenheit}°"
        })
    }

    private fun setupAddButton(view: View) {
        val addButton = view.findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            viewModel.getCurrentWeather()
        }
    }

    private fun findViews(view: View) {
        temperatureCelsiusText = view.findViewById(R.id.temperatureCelsiusTextView)
        temperatureFahrenheitText = view.findViewById(R.id.temperatureFahrenheitTextView)
        cityText = view.findViewById(R.id.cityTextView)
    }

    private fun switchToCelsius() {
        temperatureCelsiusText?.visibility = View.VISIBLE
        temperatureFahrenheitText?.visibility = View.GONE
    }

    private fun switchToFahrenheit() {
        temperatureCelsiusText?.visibility = View.GONE
        temperatureFahrenheitText?.visibility = View.VISIBLE
    }

    private fun setupUnitsSwitcher(view: View) {
        val temperatureUnitsTypeSwitch = view.findViewById<SwitchMaterial>(R.id.unitsSwitch)
        temperatureUnitsTypeSwitch.setOnCheckedChangeListener { _, isChecked ->
            onSwitch(isChecked)
        }

        onSwitch(temperatureUnitsTypeSwitch.isChecked)
    }

    private fun onSwitch(isChecked: Boolean) {
        if (isChecked) {
            switchToCelsius()
            viewModel.switchToCelsius()
        } else {
            switchToFahrenheit()
            viewModel.switchToFahrenheit()
        }
    }
}
