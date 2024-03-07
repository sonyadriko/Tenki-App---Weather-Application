import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tenki.HourWeatherAdapter
import com.example.tenki.databinding.FragmentWeatherByTimeBinding
import com.example.tenki.model.HourlyForecast
import java.util.*

private const val ARG_WEATHER_TYPE = "weatherType"

class WeatherByTimeFragment : Fragment() {

    private var _binding: FragmentWeatherByTimeBinding? = null
    private val binding get() = _binding!!

    private val hourlyForecastList = ArrayList<HourlyForecast>()
    private lateinit var hourlyWeatherAdapter: HourWeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherByTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val day = arguments?.getString(ARG_WEATHER_TYPE)

        setupRecyclerView()

        fetchAndPopulateHourlyWeather()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        hourlyWeatherAdapter = HourWeatherAdapter(hourlyForecastList)
        binding.recyclerView.apply {
            adapter = hourlyWeatherAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun fetchAndPopulateHourlyWeather() {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        for (i in currentHour until currentHour + 12) {
            val hour = if (i >= 24) i - 24 else i
//            val temperature =
//            val weatherIcon = when {
//                i < 6 -> R.drawable.rain
//                i < 18 -> R.drawable.humidity
//                else -> R.drawable.storm
//            }
//            val hourlyForecast = HourlyForecast(Date(), temperature.toDouble())
//            hourlyForecastList.add(hourlyForecast)
        }

        hourlyWeatherAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(day: String): WeatherByTimeFragment {
            val fragment = WeatherByTimeFragment()
            val args = Bundle()
            args.putString(ARG_WEATHER_TYPE, day) // Use the constant here
            fragment.arguments = args
            return fragment
        }
    }
}
