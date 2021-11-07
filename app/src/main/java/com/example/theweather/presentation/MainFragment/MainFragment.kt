package com.example.theweather.presentation.mainFragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.theweather.R
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.presentation.applicationComponent
import com.example.theweather.utils.DebugConsole
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import android.app.Activity.RESULT_OK
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController


class MainFragment @Inject constructor() : Fragment(R.layout.main_fragment),
    FragmentManager.OnBackStackChangedListener {
    private val HIGH_VALUE = 25.0
    private val MIDDLE_RANGE = 10.0


    @Inject
    lateinit var viewModelFactory: MainViewModel.Factory


    private var fusedLocationClient: FusedLocationProviderClient? = null;

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private var temperatureCelsiusText: TextView? = null
    private var temperatureFahrenheitText: TextView? = null;
    private var cityText: TextView? = null
    private var temperatureMainView: View? = null
    private var locationTokenSource: CancellationTokenSource? = null
    private var temperatureTextView: View? = null
    private var backButton: ImageButton? = null

    private val checkPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                lifecycleScope.launch { getWeatherByCoordinates() }
            } else {
                Toast.makeText(
                    requireActivity(), "location permission was not granted",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private val getPlaceLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    val place = result.data?.let { Autocomplete.getPlaceFromIntent(it) }
                    place?.name?.let { viewModel.getWeatherByCityName(it) }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    val status = result.data?.let { it1 -> Autocomplete.getStatusFromIntent(it1) }
                    status?.statusMessage?.let { DebugConsole.error(this, it) }
                }
                else -> {
                    DebugConsole.error(this, "canceled")
                }
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.applicationComponent.inject(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }


    var fragmentContainerView: FragmentContainerView? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar(view)
        setupBackButton(view)
        findViews(view)
        setupWeatherEmptyPreview()
        setupUnitsSwitcher(view)
        setupTopButtons(view)
        observeViewModel()
        fragmentContainerView = view.findViewById(R.id.secondary_navigation_host_fragment)
        childFragmentManager.addOnBackStackChangedListener(this)

    }

    override fun onDestroyView() {
         super.onDestroyView()
        fragmentContainerView.onchange
        childFragmentManager.addOnBackStackChangedListener(this)
    }

    private fun setupBackButton(view: View) {
        backButton = view.findViewById<ImageButton>(R.id.backImageButton)
        backButton?.setOnClickListener {
            requireActivity().onBackPressed();
        }
    }

    private fun setupToolBar(view: View) {
        (requireActivity() as AppCompatActivity?)?.setSupportActionBar(view.findViewById(R.id.toolBar))
    }

    private fun getWeatherByCityName() {
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY,
            mutableListOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        ).setTypeFilter(TypeFilter.CITIES)
            .build(requireContext())


        getPlaceLauncher.launch(intent)
    }

    @SuppressLint("MissingPermission")
    private suspend fun getWeatherByCoordinates() {
        val cts = CancellationTokenSource()

        locationTokenSource?.cancel()

        var location =
            fusedLocationClient!!.getCurrentLocation(PRIORITY_BALANCED_POWER_ACCURACY, cts.token)
                .await()

        if (location == null)
            location = fusedLocationClient!!.lastLocation.await()

        location?.let {
            viewModel.getWeatherByCoordinates(location.latitude, location.longitude)
        }
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
        val findCityButton = view.findViewById<ImageButton>(R.id.searchLocationImageButton)

        nearMeImageButton.setOnClickListener {
            checkPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        findCityButton.setOnClickListener {
            getWeatherByCityName()
        }
    }


    private fun findViews(view: View) {
        temperatureCelsiusText = view.findViewById(R.id.temperatureCelsiusTextView)
        temperatureFahrenheitText = view.findViewById(R.id.temperatureFahrenheitTextView)
        cityText = view.findViewById(R.id.cityTextView)
        temperatureMainView = view.findViewById(R.id.temperatureMainView)
        temperatureTextView = view.findViewById(R.id.temperatureTextView)
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

    private fun observeViewModel() {
        viewModel.selectedWeatherModel.observe(viewLifecycleOwner, { model ->
            if (model == null) return@observe
            setupWeatherNotEmptyPreview()
            temperatureCelsiusText?.text = "${model.temperatureCelsius}°"
            temperatureFahrenheitText?.text = "${model.temperatureFahrenheit}°"
            cityText?.text = model.city
            updateColor(model)
        })
    }

    private fun setupWeatherEmptyPreview() {
        cityText?.text = resources.getString(R.string.welcome_text)
        temperatureTextView?.visibility = View.GONE
    }

    private fun setupWeatherNotEmptyPreview() {
        temperatureTextView?.visibility = View.VISIBLE
    }


    override fun onBackStackChanged() {
        if (childFragmentManager.backStackEntryCount > 1)
            backButton?.visibility = View.VISIBLE
        else
            backButton?.visibility = View.GONE
    }
}
