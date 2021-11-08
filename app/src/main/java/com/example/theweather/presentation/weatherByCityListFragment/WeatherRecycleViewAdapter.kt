package com.example.theweather.presentation.weatherByCityListFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.theweather.R
import com.example.theweather.domain.models.WeatherList
import com.example.theweather.utils.TemperatureUtils
import com.example.theweather.utils.TimeUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class WeatherRecycleViewAdapter @AssistedInject constructor(
    @Assisted private val dataSet: List<WeatherList>,
    @Assisted private val navController: NavController
) :
    RecyclerView.Adapter<WeatherRecycleViewAdapter.WeatherViewHolder>() {

    val MIN_VALUE_FOR_GRAPH_BUTTON = 1;

    private val viewHolders = hashSetOf<WeatherViewHolder>()
    private var selectedTemperatureUnitsType = TemperatureUtils.TemperatureUnitsType.FAHRENHEIT

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cityAndFahrenheitTextView: TextView? = null
        var cityAndCelsiusTextView: TextView? = null
        var dateTextView: TextView? = null
        var temperatureUnitsType = TemperatureUtils.TemperatureUnitsType.FAHRENHEIT

        init {
            cityAndFahrenheitTextView = itemView.findViewById(R.id.cityAndFahrenheitTextView)
            cityAndCelsiusTextView = itemView.findViewById(R.id.cityAndCelsiusTextView)
            dateTextView = itemView.findViewById(R.id.dateTextView)
        }

        fun setCelsiusView() {
            temperatureUnitsType = TemperatureUtils.TemperatureUnitsType.CELSIUS
            cityAndFahrenheitTextView?.visibility = View.INVISIBLE
            cityAndCelsiusTextView?.visibility = View.VISIBLE
        }

        fun setFahrenheitView() {
            temperatureUnitsType = TemperatureUtils.TemperatureUnitsType.FAHRENHEIT
            cityAndFahrenheitTextView?.visibility = View.VISIBLE
            cityAndCelsiusTextView?.visibility = View.INVISIBLE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return WeatherViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val data = dataSet[position]
        val weatherPreview = data.preview;
        val view = holder.itemView

        weatherPreview.observe(view.context as LifecycleOwner, {
            holder.cityAndFahrenheitTextView?.text = "${it.city}, ${it.temperatureFahrenheit}°F"
            holder.cityAndCelsiusTextView?.text = "${it.city}, ${it.temperatureCelsius}°C"
            holder.dateTextView?.text = TimeUtils.timestampToDateString(it.dateTime)
        })

        when (selectedTemperatureUnitsType) {
            TemperatureUtils.TemperatureUnitsType.CELSIUS -> holder.setCelsiusView()
            else -> holder.setFahrenheitView()
        }

        setupButtons(view, data)
        viewHolders.add(holder)
    }

    private fun setupButtons(view: View, weatherList: WeatherList) {
        val chartButton = view.findViewById<ImageButton>(R.id.chartButton)

        chartButton.setOnClickListener { openChartFragment(weatherList.cityName) }

        val models = weatherList.weatherModels.value
        if (models == null || models.size <= MIN_VALUE_FOR_GRAPH_BUTTON)
            chartButton.visibility = View.GONE
        else
            chartButton.visibility = View.VISIBLE

    }

    private fun openChartFragment(citiName: String) {
        val action =
            WeatherByCityListFragmentDirections.actionWeatherByCityListFragmentToChartFragment(
                citiName
            )
        navController.navigate(action)
    }

    override fun onViewRecycled(holder: WeatherViewHolder) {
        super.onViewRecycled(holder)

        viewHolders.remove(holder)
    }

    fun switchToCelsius() {
        selectedTemperatureUnitsType = TemperatureUtils.TemperatureUnitsType.CELSIUS

        for (viewHolder in viewHolders)
            viewHolder.setCelsiusView()
    }

    fun switchToFahrenheit() {
        selectedTemperatureUnitsType = TemperatureUtils.TemperatureUnitsType.FAHRENHEIT

        for (viewHolder in viewHolders)
            viewHolder.setFahrenheitView()
    }

    override fun getItemCount() = dataSet.size

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted dataSet: List<WeatherList>,
            navController: NavController
        ): WeatherRecycleViewAdapter
    }
}