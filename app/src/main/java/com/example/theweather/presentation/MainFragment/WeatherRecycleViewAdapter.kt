package com.example.theweather.presentation.MainFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.theweather.R
import com.example.theweather.domain.models.WeatherModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.text.SimpleDateFormat

class WeatherRecycleViewAdapter @AssistedInject constructor(@Assisted private val dataSet: ArrayList<WeatherModel>) :
    RecyclerView.Adapter<WeatherRecycleViewAdapter.WeatherViewHolder>() {

    val dateFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cityAndTemperatureTextView: TextView? = null
        var dateTextView: TextView? = null


        init {
            cityAndTemperatureTextView = itemView.findViewById(R.id.cityAndTemperatureTextView)
            dateTextView = itemView.findViewById(R.id.dateTextView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return WeatherViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val data = dataSet[position]

        holder.cityAndTemperatureTextView?.text = "${data.city}, ${data.temperature}°F"
        holder.dateTextView?.text = "${dateFormat.format(data.dateTime)}"
    }

    override fun getItemCount() = dataSet.size

    @AssistedFactory
    interface Factory {
        fun create(@Assisted dataSet: ArrayList<WeatherModel>): WeatherRecycleViewAdapter
    }
}