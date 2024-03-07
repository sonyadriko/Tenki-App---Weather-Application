package com.example.tenki

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.Manifest
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.tenki.databinding.ActivityMainBinding
import com.example.tenki.model.WeatherData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var weatherApiService: WeatherApiService
    private lateinit var binding : ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationHelper: LocationHelper

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1001
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        setupViewPager(viewPager)

        val tabLayout = binding.tabLayout
        tabLayout.setupWithViewPager(viewPager)

        weatherApiService = (application as MyApplication).weatherApiService

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        viewPager.currentItem = 0

        tabLayout.getTabAt(0)?.customView = createTabView("Today", 0, viewPager, tabLayout)
        tabLayout.getTabAt(1)?.customView = createTabView("Tomorrow", 1, viewPager, tabLayout)
        tabLayout.getTabAt(2)?.customView = createTabView("Next 3 days", 2, viewPager, tabLayout)




        locationHelper = LocationHelper(this)


        fetchWeatherData()
    }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(WeatherByTimeFragment.newInstance("today"), "Today")
        adapter.addFragment(WeatherByTimeFragment.newInstance("tomorrow"), "Tomorrow")
        adapter.addFragment(WeatherByTimeFragment.newInstance("next_three_days"), "Next 3 days")
        viewPager.adapter = adapter
    }
    private fun createTabView(title: String, position: Int, viewPager: ViewPager, tabLayout: TabLayout): View {
        val tabView = LayoutInflater.from(this).inflate(R.layout.custom_tabview, null)
        val textView = tabView.findViewById<TextView>(android.R.id.text1)
        textView.text = title
        textView.setTextColor(ContextCompat.getColorStateList(this, R.color.tab_title_selector))

        val dotIndicator = tabView.findViewById<View>(R.id.indicator)

        val selectedPosition = tabLayout.selectedTabPosition // Get the initially selected tab position

        dotIndicator.visibility = if (position == selectedPosition) View.VISIBLE else View.INVISIBLE

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (position == tab.position) {
                    dotIndicator.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                if (position == tab.position) {
                    dotIndicator.visibility = View.INVISIBLE
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return tabView
    }

    private fun fetchWeatherData() {
        if (checkLocationPermission()) {
            locationHelper.getCurrentLocation { location ->
                location?.let {
                    fetchWeatherDataWithLocation(it.latitude, it.longitude)
                } ?: showError("Failed to retrieve location")
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun fetchWeatherDataWithLocation(latitude: Double, longitude: Double) {
        val cityName = getCityName(latitude, longitude)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = weatherApiService.getHourlyForecast(apiKey = "df86c238c7a34f89b9d35944240203", location = cityName)
                withContext(Dispatchers.Main) {
                    updateUi(response)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Failed to fetch weather data")
                }
            }
        }
    }
    private fun getCityName(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                return addresses[0].locality ?: ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }
    private fun checkLocationPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    // Helper method to request location permissions
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }

    // Handle permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchWeatherData()
                } else {
                    showError("Location permission denied")
                }
            }
        }
    }


//    private fun fetchWeatherData() {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = (application as MyApplication).weatherApiService.getCurrentWeather(apiKey = "df86c238c7a34f89b9d35944240203", location = "London")
//                Log.d(TAG, "Response: $response")
//                withContext(Dispatchers.Main) {
//                    updateUi(response)
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    showError()
//                }
//            }
//        }
//    }

    private fun showError() {

    }

//    private fun fetchWeatherData() {
//        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//        // Meminta izin lokasi secara dinamis jika belum diberikan
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            // Menggunakan FusedLocationProviderClient untuk mendapatkan lokasi
//            fusedLocationClient.lastLocation
//                .addOnSuccessListener { location: Location? ->
//                    // Handle lokasi berhasil diperoleh
//                    if (location != null) {
//                        val latitude = location.latitude
//                        val longitude = location.longitude
//
//                        // Menggunakan lokasi aktual untuk mendapatkan data cuaca
//                        CoroutineScope(Dispatchers.IO).launch {
//                            try {
//                                val response = (application as MyApplication).weatherApiService.getCurrentWeather(apiKey = "df86c238c7a34f89b9d35944240203", latitude = latitude, longitude = longitude)
//                                Log.d(TAG, "Latitude: ${location?.latitude}, Longitude: ${location?.longitude}")
//
//                                withContext(Dispatchers.Main) {
//                                    updateUi(response)
//                                }
//                            } catch (e: Exception) {
//                                withContext(Dispatchers.Main) {
//                                    Log.e(TAG, "Error accessing location: ${e.message}")
//                                    showError("Izin lokasi ditolak")
//                                }
//                            }
//                        }
//                    }
//                }
//                .addOnFailureListener { e ->
//                    // Handle kesalahan saat mencoba mendapatkan lokasi
//                    showError("Izin lokasi ditolak")
//                }
//        } else {
//            // Jika izin belum diberikan, minta izin
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                REQUEST_LOCATION_PERMISSION
//            )
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            REQUEST_LOCATION_PERMISSION -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Izin diberikan, lanjutkan dengan penggunaan lokasi
//                    fetchWeatherData()
//                } else {
//                    // Izin ditolak, tangani secara anggun
//                    showError("Izin lokasi ditolak")
//                }
//            }
//            // Handle other permission requests if needed
//        }
//    }


    private fun updateUi(weatherData: WeatherData) {
        if (weatherData != null) {
            // Implementasi pembaruan UI
//        binding.tvCityName.text = "${weatherData.location.name}, ${weatherData.location.region}, ${weatherData.location.country}"
            binding.tvCityName.text =
                "${weatherData.location.name}, ${weatherData.location.country}"
            binding.tvDate.text = weatherData.current.last_updated
            binding.tvContiion.text = weatherData.current.condition.text
            binding.tvMainTemp.text = weatherData.current.temp_c.toString()
            Glide.with(this)
                .load("https:${weatherData.current.condition.icon}") // Sesuaikan URL ikon
                .into(binding.mainWeather)
            binding.tvWind.text  = weatherData.current.wind_kph.toString()
            binding.tvHumidity.text = weatherData.current.humidity.toString()
            binding.tvRain.text = weatherData.current.gust_kph.toString()

        }else{
            binding.tvCityName.text =
                "GATAU"
            binding.tvDate.text = "GATAU"
            binding.tvContiion.text = "GATAU"
            binding.tvMainTemp.text = "GATAU"
        }
    }

    private fun showError(s: String) {
        // Implementasi penanganan kesalahan
    }
}