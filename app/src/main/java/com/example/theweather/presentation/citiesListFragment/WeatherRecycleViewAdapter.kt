package com.example.theweather.presentation.citiesListFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.theweather.R
import com.example.theweather.domain.models.WeatherList
import com.example.theweather.utils.TemperatureUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.text.SimpleDateFormat


class WeatherRecycleViewAdapter @AssistedInject constructor(
    @Assisted private val dataSet: List<WeatherList>,
) :
    RecyclerView.Adapter<WeatherRecycleViewAdapter.WeatherViewHolder>() {

    private val viewHolders = hashSetOf<WeatherViewHolder>()
    private var selectedTemperatureUnitsType: TemperatureUtils.TemperatureUnitsType =
        TemperatureUtils.TemperatureUnitsType.FAHRENHEIT

    val dateFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cityAndFahrenheitTextView: TextView? = null
        var cityAndCelsiusTextView: TextView? = null
        var dateTextView: TextView? = null
        var temperatureUnitsType: TemperatureUtils.TemperatureUnitsType =
            TemperatureUtils.TemperatureUnitsType.FAHRENHEIT

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

        weatherPreview.observe(holder.itemView.context as LifecycleOwner, {
            holder.cityAndFahrenheitTextView?.text = "${it.city}, ${it.temperatureFahrenheit}°F"
            holder.cityAndCelsiusTextView?.text = "${it.city}, ${it.temperatureCelsius}°C"
            holder.dateTextView?.text = "${dateFormat.format(it.dateTime)}"
        })

        when (selectedTemperatureUnitsType) {
            TemperatureUtils.TemperatureUnitsType.CELSIUS -> holder.setCelsiusView()
            else -> holder.setFahrenheitView()
        }
        viewHolders.add(holder)
    }

    override fun onViewRecycled(holder: WeatherViewHolder) {
        super.onViewRecycled(holder)

        viewHolders.remove(holder)
    }

    fun switchToCelsius() {
        selectedTemperatureUnitsType = TemperatureUtils.TemperatureUnitsType.CELSIUS
        for (viewHolder in viewHolders) {
            viewHolder.setCelsiusView()
        }
    }

    fun switchToFahrenheit() {
        selectedTemperatureUnitsType = TemperatureUtils.TemperatureUnitsType.FAHRENHEIT
        for (viewHolder in viewHolders) {
            viewHolder.setFahrenheitView()
        }
    }

    override fun getItemCount() = dataSet.size

    @AssistedFactory
    interface Factory {
        fun create(@Assisted dataSet: List<WeatherList>): WeatherRecycleViewAdapter
    }
}