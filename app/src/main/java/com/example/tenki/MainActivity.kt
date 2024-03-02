package com.example.tenki

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tenki.model.WeatherData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = (application as MyApplication).weatherApiService.getCurrentWeather(apiKey = "df86c238c7a34f89b9d35944240203", location = "City, Country")
                withContext(Dispatchers.Main) {
                    updateUi(response)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError()
                }
            }
        }
    }

    private fun updateUi(weatherData: WeatherData) {
        // Implementasi pembaruan UI
    }

    private fun showError() {
        // Implementasi penanganan kesalahan
    }
}