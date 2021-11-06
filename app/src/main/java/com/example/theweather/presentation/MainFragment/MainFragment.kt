package com.example.theweather.presentation.mainFragment

import android.Manifest
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
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import com.example.theweather.domain.models.WeatherModel
import androidx.core.content.ContextCompat
import android.location.LocationManager
import android.location.LocationListener
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Context.LOCATION_SERVICE
import android.location.Location


class MainFragment @Inject constructor() : Fragment(R.layout.main_fragment) {

    private val MIN_LOCATION_UPDATE_TIME = 5 * 60 * 1000L;
    private val MIN_LOCATION_MANAGER_DISTANCE = 100f;

    private val HIGH_VALUE = 25.0
    private val MIDDLE_RANGE = 10.0


    @Inject
    lateinit var viewModelFactory: MainViewModel.Factory


    private var locationManager: LocationManager? = null

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private var temperatureCelsiusText: TextView? = null
    private var temperatureFahrenheitText: TextView? = null;
    private var cityText: TextView? = null
    private var temperatureMainView: View? = null

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                sendTemperatureRequest()
                Toast.makeText(
                    requireActivity(), "permission was granted",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireActivity(), "location permission was not granted",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    @SuppressLint("MissingPermission")
    private fun sendTemperatureRequest() {
        val location = getLastKnownLocation()
        location?.let {
            viewModel.getWeatherByCoordinates(location.latitude, location.longitude)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.applicationComponent.inject(this)
        locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViews(view)
        setupUnitsSwitcher(view)
        setupTopButtons(view)


        viewModel.selectedWeatherModel.observe(viewLifecycleOwner, { model ->
            if (model == null) return@observe
            temperatureCelsiusText?.text = "${model.temperatureCelsius}°"
            temperatureFahrenheitText?.text = "${model.temperatureFahrenheit}°"
            cityText?.text = model.city
            updateColor(model)
        })

        //  observeLocationManager()
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        if (locationManager == null)
            return null

        val providers: List<String> = locationManager!!.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val l: Location? = locationManager!!.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l!!.accuracy < bestLocation.accuracy) {
                bestLocation = l
            }
        }
        return bestLocation
    }

    @SuppressLint("MissingPermission")
    private fun observeLocationManager() {
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_LOCATION_UPDATE_TIME,
            MIN_LOCATION_MANAGER_DISTANCE,
            LocationListener {}
        )

        locationManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            MIN_LOCATION_UPDATE_TIME,
            MIN_LOCATION_MANAGER_DISTANCE,
            LocationListener {}
        )

    }


    private fun updateColor(weatherModel: WeatherModel) {
        if (temperatureMainView == null)
            return

        val color = getColor(weatherModel)
        temperatureMainView?.setBackgroundColor(color)
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

    private fun setupTopButtons(view: View) {
        val nearMeImageButton = view.findViewById<ImageButton>(R.id.nearMeImageButton)

        nearMeImageButton.setOnClickListener {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
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
