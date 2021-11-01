package com.example.theweather.presentation.MainFragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theweather.R
import com.example.theweather.presentation.applicationComponent
import dagger.Lazy
import javax.inject.Inject
import com.example.theweather.utils.TemperatureUtils
import com.google.android.material.switchmaterial.SwitchMaterial

class MainFragment @Inject constructor() : Fragment() {

    @Inject
    lateinit var viewModelFactory: Lazy<MainViewModel.Factory>

    @Inject
    lateinit var adapterFactory: WeatherRecycleViewAdapter.Factory

    private val viewModel: MainViewModel by viewModels { viewModelFactory.get() }
    private var adapter: WeatherRecycleViewAdapter? = null

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

        observeViewModel()
        setupRecycleView(view)
        setupUnitsSwitcher(view)

        val addButton = view.findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            viewModel.getCurrentWeather()
        }

        viewModel.updateSavedWeather()
    }

    private fun observeViewModel() {
        viewModel.temperatureUnitsType.observe(viewLifecycleOwner, {
            updateTemperatureUnitsTypeView()
        })

        viewModel.weatherListLiveData.observe(viewLifecycleOwner, {
            adapter?.notifyDataSetChanged()
            updateTemperatureUnitsTypeView()
        })
    }

    private fun setupRecycleView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.weatherRecycleView)
        adapter = viewModel.weatherListLiveData.value?.let { adapterFactory.create(it) }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
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

    private fun updateTemperatureUnitsTypeView() {
        when (viewModel.temperatureUnitsType.value) {
            TemperatureUtils.TemperatureUnitsType.CELSIUS -> switchToCelsius()
            else -> switchToFahrenheit()
        }
    }

    private fun switchToCelsius() {
        adapter?.switchToCelsius()
    }

    private fun switchToFahrenheit() {
        adapter?.switchToFahrenheit()
    }
}