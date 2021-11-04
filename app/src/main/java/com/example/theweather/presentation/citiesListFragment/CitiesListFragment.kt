package com.example.theweather.presentation.citiesListFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theweather.R
import com.example.theweather.presentation.applicationComponent
import dagger.Lazy
import javax.inject.Inject

class CitiesListFragment @Inject constructor() : Fragment() {

    @Inject
    lateinit var viewModelFactory: Lazy<CitiesListViewModel.Factory>

    @Inject
    lateinit var adapterFactory: WeatherRecycleViewAdapter.Factory
    private val viewModel: CitiesListViewModel by viewModels() { viewModelFactory.get() }
    private var adapter: WeatherRecycleViewAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.cities_list_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.applicationComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupRecycleView(view)
        observeWeatherModels()
    }

    private fun setupRecycleView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.weatherRecycleView)
        adapter = viewModel.weatherListLiveData.value?.let { adapterFactory.create(it) }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun switchToCelsius() {
        adapter?.switchToCelsius()
    }

    private fun switchToFahrenheit() {
        adapter?.switchToFahrenheit()
    }

    /*  private fun updateTemperatureUnitsTypeView() {
          when (viewModel.temperatureUnitsType.value) {
              TemperatureUtils.TemperatureUnitsType.CELSIUS -> switchToCelsius()
              else -> switchToFahrenheit()
          }
      }*/

    private fun observeViewModel() {
        /*   viewModel.temperatureUnitsType.observe(viewLifecycleOwner, {
               updateTemperatureUnitsTypeView()
           })*/

        viewModel.weatherListLiveData.observe(viewLifecycleOwner, {
            adapter?.notifyDataSetChanged()
        })
    }

    private fun observeWeatherModels() {
        viewModel.getWeatherModels().observe(viewLifecycleOwner, {
            val models = it.value
            if (models != null)
                viewModel.updateWeatherLists(models)
        })
    }
}