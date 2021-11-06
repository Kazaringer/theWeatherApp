package com.example.theweather.presentation.mainFragment

import android.animation.ArgbEvaluator
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
import android.animation.ObjectAnimator
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import com.example.theweather.domain.models.WeatherModel
import android.graphics.PorterDuff
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat


class MainFragment @Inject constructor() : Fragment(R.layout.main_fragment) {

    private val HIGH_VALUE = 25.0
    private val MIDDLE_RANGE = 10.0


    @Inject
    lateinit var viewModelFactory: MainViewModel.Factory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private var temperatureCelsiusText: TextView? = null
    private var temperatureFahrenheitText: TextView? = null;
    private var cityText: TextView? = null
    private var temperatureMainView: View? = null

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
            cityText?.text = model.city
            updateColor(model)
        })
    }

    private fun updateColor(weatherModel: WeatherModel) {
        if (temperatureMainView == null)
            return

        val color = getColor(weatherModel)

        temperatureMainView?.background?.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                color,
                BlendModeCompat.SRC
            )
    }


    private fun getColor(weatherModel: WeatherModel): Int {
        val temperature = weatherModel.temperatureCelsius

        if (temperature > HIGH_VALUE) {
            return ContextCompat.getColor(requireContext(), R.color.high_temperature_color)
        }

        if (temperature > MIDDLE_RANGE) {
            return ContextCompat.getColor(requireContext(), R.color.middle_temperature_color)
        }

        return ContextCompat.getColor(requireContext(), R.color.low_temperature_color)
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
        temperatureMainView = view.findViewById(R.id.temperatureMainView)
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
