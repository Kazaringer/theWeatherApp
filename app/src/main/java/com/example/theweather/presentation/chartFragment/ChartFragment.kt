package com.example.theweather.presentation.chartFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.theweather.R
import com.example.theweather.data.storage.Models.NetworkModels.Weather
import com.example.theweather.domain.models.WeatherList
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.usecase.GetWeatherListByCityUseCase
import com.example.theweather.presentation.applicationComponent
import com.example.theweather.presentation.weatherByCityListFragment.WeatherByCityListViewModel
import com.example.theweather.presentation.weatherByCityListFragment.WeatherRecycleViewAdapter
import com.example.theweather.utils.TemperatureUtils
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.Lazy
import javax.inject.Inject

class ChartFragment @Inject constructor() : Fragment(R.layout.chart_fragment) {
    @Inject
    lateinit var viewModelFactory: Lazy<ChartViewModel.Factory>

    private val viewModel: ChartViewModel by viewModels() { viewModelFactory.get() }
    private val args: ChartFragmentArgs by navArgs()
    private var lineChart: LineChart? = null

    private val celsiusEntries: ArrayList<Entry> = ArrayList()
    private val fahrenheitEntries: ArrayList<Entry> = ArrayList()


    private var currentTemperatureUnitsType: TemperatureUtils.TemperatureUnitsType =
        TemperatureUtils.TemperatureUnitsType.FAHRENHEIT

    private val entries: ArrayList<Entry>
        get() = when (currentTemperatureUnitsType) {
            TemperatureUtils.TemperatureUnitsType.CELSIUS ->
                celsiusEntries
            else ->
                fahrenheitEntries
        }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.applicationComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGraphView(view)
        observeViewModel()
    }

    private fun setupGraphView(view: View) {
        lineChart = view.findViewById(R.id.weatherGraph)

        lineChart?.setTouchEnabled(false)
        lineChart?.setDragEnabled(false)
        lineChart?.setScaleEnabled(false)
        lineChart?.setPinchZoom(false)
        lineChart?.setDoubleTapToZoomEnabled(false)
        lineChart?.getLegend()?.setEnabled(false)
        lineChart?.getDescription()?.setEnabled(false)

        val xAxis = lineChart?.xAxis
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.setDrawLabels(false)
    }

    private fun observeViewModel() {
        observeCurrentTemperatureUnitsType()
        observeWeatherList()
    }

    private fun switchToCelsius() {
        currentTemperatureUnitsType = TemperatureUtils.TemperatureUnitsType.CELSIUS
        updateGraphData()
    }

    private fun switchToFahrenheit() {
        currentTemperatureUnitsType = TemperatureUtils.TemperatureUnitsType.FAHRENHEIT
        updateGraphData()
    }

    private fun updateGraphData() {
        val entries = this.entries
        if (entries.size <= 0)
            return

        val dataSet = LineDataSet(entries, "");
        val data = LineData(dataSet)
        lineChart?.data = data
        lineChart?.invalidate()
    }

    private fun observeWeatherList() {
        val cityName = args.cityName
        val weatherList = viewModel.getWeatherList(cityName)
        weatherList.weatherModels.observe(viewLifecycleOwner, {
            updateEntries(it)
            updateGraphData()
            viewModel.onWeatherListUpdate(it)
        })
    }

    private fun observeCurrentTemperatureUnitsType() {
        viewModel.getCurrentTemperatureUnitsType().observe(viewLifecycleOwner, {
            when (it) {
                TemperatureUtils.TemperatureUnitsType.CELSIUS -> {
                    switchToCelsius()
                }
                else -> switchToFahrenheit()
            }
        })
    }

    private fun updateEntries(weatherModels: List<WeatherModel>) {
        celsiusEntries.clear()
        fahrenheitEntries.clear()

        val firstWeatherModel = weatherModels.minByOrNull { it.dateTime }
        var minTime = 0f
        firstWeatherModel?.let { minTime = it.dateTime.time.toFloat() }

        for (weather in weatherModels) {
            val x = (weather.dateTime.time - minTime)
            val yCelsius = weather.temperatureCelsius.toFloat()
            val yFahrenheit = weather.temperatureFahrenheit.toFloat()

            celsiusEntries.add(Entry(x, yCelsius))
            fahrenheitEntries.add(Entry(x, yFahrenheit))
        }
    }
}