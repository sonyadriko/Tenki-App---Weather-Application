package com.example.tenki

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.tenki.databinding.ActivityMainBinding
import com.example.tenki.model.WeatherData
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var weatherApiService: WeatherApiService
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_main)
        setContentView(binding.root)

        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        setupViewPager(viewPager)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)

        weatherApiService = (application as MyApplication).weatherApiService


        viewPager.currentItem = 0

        tabLayout.getTabAt(0)?.customView = createTabView("Today", 0, viewPager, tabLayout)
        tabLayout.getTabAt(1)?.customView = createTabView("Tomorrow", 1, viewPager, tabLayout)
        tabLayout.getTabAt(2)?.customView = createTabView("Next 10 days", 2, viewPager, tabLayout)


        fetchWeatherData()
    }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(TodayFragment(), "Today")
        adapter.addFragment(TomorrowFragment(), "Tomorrow")
        adapter.addFragment(TomorrowFragment(), "Next 10 days")
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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = (application as MyApplication).weatherApiService.getCurrentWeather(apiKey = "df86c238c7a34f89b9d35944240203", location = "Surabaya, Indonesia")
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
        binding.tvCityName.text = "${weatherData.location.name}, ${weatherData.location.region}, ${weatherData.location.country}"
        binding.tvDate.text = weatherData.current.last_updated
        binding.tvContiion.text = weatherData.condition.text
        binding.tvMainTemp.text = weatherData.current.tempc.toString()

    }

    private fun showError() {
        // Implementasi penanganan kesalahan
    }
}