package com.example.theweather.presentation.weatherByCityListFragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theweather.R
import com.example.theweather.domain.models.WeatherList
import com.example.theweather.presentation.applicationComponent
import com.example.theweather.utils.TemperatureUtils
import dagger.Lazy
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherByCityListFragment @Inject constructor() :
    Fragment(R.layout.weather_by_city_list_fragment) {
    @Inject
    lateinit var viewModelFactory: Lazy<WeatherByCityListViewModel.Factory>

    @Inject
    lateinit var adapterFactory: WeatherRecycleViewAdapter.Factory
    private val viewModel: WeatherByCityListViewModel by viewModels() { viewModelFactory.get() }
    private var recyclerView: RecyclerView? = null
    private var adapter: WeatherRecycleViewAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.applicationComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViews(view)
        setupRecycleView()
        observeViewModel()
    }

    private fun findViews(view: View) {
        recyclerView = view.findViewById(R.id.weatherRecycleView)
    }

    private fun observeViewModel() {

        viewModel.weatherListsLiveData.observe(viewLifecycleOwner, {
            if (adapter == null) {
                createAdapter(it)
                return@observe
            }

            adapter?.notifyDataSetChanged()
        })

        viewModel.getCurrentTemperatureUnitsType().observe(viewLifecycleOwner, {
            when (it) {
                TemperatureUtils.TemperatureUnitsType.CELSIUS -> {
                    switchToCelsius()
                }
                else -> switchToFahrenheit()
            }
        })

        lifecycleScope.launch {
            viewModel.getWeatherModels().observe(viewLifecycleOwner, { models ->
                if (models != null) {
                    viewModel.updateData(models)
                }
            })
        }
    }

    private fun createAdapter(weatherLists: List<WeatherList>) {
        adapter = adapterFactory.create(weatherLists, findNavController())
        recyclerView?.adapter = adapter
    }

    private fun setupRecycleView() {
        recyclerView?.layoutManager = LinearLayoutManager(context)
        if (adapter != null)
            recyclerView?.adapter = adapter
    }


    private fun switchToCelsius() {
        adapter?.switchToCelsius()
    }

    private fun switchToFahrenheit() {
        adapter?.switchToFahrenheit()
    }
}