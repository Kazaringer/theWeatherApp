package com.example.theweather.presentation.MainFragment

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.theweather.R
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.utils.TemperatureUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.text.SimpleDateFormat

class WeatherRecycleViewAdapter @AssistedInject constructor(
    @Assisted private val dataSet: List<WeatherModel>,
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


        init {
            cityAndFahrenheitTextView = itemView.findViewById(R.id.cityAndFahrenheitTextView)
            cityAndCelsiusTextView = itemView.findViewById(R.id.cityAndCelsiusTextView)
            dateTextView = itemView.findViewById(R.id.dateTextView)
        }

        fun setCelsiusView() {
            cityAndFahrenheitTextView?.visibility = View.INVISIBLE
            cityAndCelsiusTextView?.visibility = View.VISIBLE
        }

        fun setFahrenheitView() {
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

        holder.cityAndFahrenheitTextView?.text = "${data.city}, ${data.temperatureFahrenheit}°F"
        holder.cityAndCelsiusTextView?.text = "${data.city}, ${data.temperatureCelsius}°C"
        holder.dateTextView?.text = "${dateFormat.format(data.dateTime)}"

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
        fun create(
            @Assisted dataSet: List<WeatherModel>
        ): WeatherRecycleViewAdapter
    }
}