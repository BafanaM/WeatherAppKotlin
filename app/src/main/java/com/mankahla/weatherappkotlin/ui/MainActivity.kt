package com.mankahla.weatherappkotlin.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mankahla.weatherappkotlin.R
import com.mankahla.weatherappkotlin.data.WeatherResponse
import com.mankahla.weatherappkotlin.databinding.ActivityMainBinding
import kotlin.math.roundToInt
import androidx.lifecycle.Observer
import com.mankahla.weatherappkotlin.data.CombineResponse
import com.mankahla.weatherappkotlin.data.FiveDayForecastResponse
import com.mankahla.weatherappkotlin.utils.Constant
import com.mankahla.weatherappkotlin.utils.changeLayoutBackground
import com.mankahla.weatherappkotlin.utils.transformList
import com.mankahla.weatherappkotlin.viewModel.WeatherViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var dataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        val intent = intent
        val latitude = intent.getStringExtra(EXTRA_LATITUDE)
        val longitude = intent.getStringExtra(EXTRA_LONGITUDE)
        viewModel.getForecastData(latitude.toString(), longitude.toString(), Constant.METRIC)

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.loadingIndicator.observe(this, Observer {
            if (it) {
                dataBinding.progressBar.visibility = View.GONE
            } else {
                dataBinding.progressBar.visibility = View.VISIBLE
            }
        })
        viewModel.combineResponseData.observe(this, Observer {
            dataBinding.mainContainer.visibility = View.VISIBLE
            processResponse(it)
        })
    }

    private fun processResponse(combineResponse: CombineResponse?) {
        displayCurrentWeather(combineResponse?.weatherResponse)
        displayForecast(combineResponse?.fiveDayForecastResponse)
    }

    private fun displayForecast(fiveDayForecastResponse: FiveDayForecastResponse?) {
        val filteredList = fiveDayForecastResponse?.list?.let { transformList(it) }
        val forecastRecyclerView = findViewById<RecyclerView>(R.id.recyclerView_forecast)

        var forecastAdapter = ForecastAdapter(filteredList!!)
        forecastRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = forecastAdapter
        }
    }

    private fun displayCurrentWeather(weatherResponse: WeatherResponse?) {
        dataBinding.textViewDescription.text = weatherResponse?.weather?.get(0)?.main
        dataBinding.textViewCurrentWeather.text =
            this.getString(R.string.degree, weatherResponse?.main?.temp?.roundToInt())
        dataBinding.textViewMinTemperature.text =
            this.getString(R.string.degree, weatherResponse?.main?.temp_min?.roundToInt())
        dataBinding.textViewCurrentTemperature.text =
            this.getString(R.string.degree, weatherResponse?.main?.temp?.roundToInt())
        dataBinding.textViewMaxTemperature.text =
            this.getString(R.string.degree, weatherResponse?.main?.temp_max?.roundToInt())
        val linearLayout = findViewById<LinearLayout>(R.id.main_container)
        weatherResponse?.weather?.get(0)?.icon?.let {
            changeLayoutBackground(
                this,
                it, linearLayout
            )
        }
    }

    companion object {
        private const val EXTRA_LATITUDE = "EXTRA_LATITUDE"
        private const val EXTRA_LONGITUDE = "EXTRA_LONGITUDE"
        fun getStartIntent(context: Context, latitude: String?, longitude: String?): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_LATITUDE, latitude)
            intent.putExtra(EXTRA_LONGITUDE, longitude)
            return intent
        }
    }
}