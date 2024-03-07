package com.example.tenki

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tenki.databinding.ItemHourWeatherBinding
import com.example.tenki.model.HourlyForecast

class HourWeatherAdapter(private val hourlyWeatherList: List<HourlyForecast>) :
    RecyclerView.Adapter<HourWeatherAdapter.HourWeatherViewHolder>() {

    inner class HourWeatherViewHolder(private val binding: ItemHourWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hourlyForecast: HourlyForecast) {
            binding.tvHour.text = hourlyForecast.time.toString() // Adjust time formatting as needed
            binding.tvTemp.text = hourlyForecast.temp_c.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourWeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHourWeatherBinding.inflate(inflater, parent, false)
        return HourWeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourWeatherViewHolder, position: Int) {
        val hourlyForecast = hourlyWeatherList[position]
        holder.bind(hourlyForecast)
    }

    override fun getItemCount(): Int {
        return hourlyWeatherList.size
    }
}
