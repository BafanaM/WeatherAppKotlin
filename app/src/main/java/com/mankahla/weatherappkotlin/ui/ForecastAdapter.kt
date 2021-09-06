package com.mankahla.weatherappkotlin.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.mankahla.weatherappkotlin.R
import com.mankahla.weatherappkotlin.data.DayWeather
import com.mankahla.weatherappkotlin.utils.getDayNumberOld
import com.mankahla.weatherappkotlin.utils.setImageResource
import kotlin.math.roundToLong

class ForecastAdapter(private val dayWeatherList: List<DayWeather>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_five_day_forecast_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() : Int{
        return dayWeatherList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(dayWeatherList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(dayWeather: DayWeather) = with(itemView) {
            val context = itemView.context
            val textViewDayOfWeek = itemView.findViewById<TextView>(R.id.textView_day_of_week)
            val textViewCurrentTextView =
                itemView.findViewById<TextView>(R.id.textView_current_temperature)
            val weatherIconImageView =
                itemView.findViewById<AppCompatImageView>(R.id.image_view_weather_icon)

            textViewDayOfWeek.text = getDayNumberOld(dayWeather.dt)
            textViewCurrentTextView.text = context.getString(
                R.string.degree,
                dayWeather.main.temp.roundToLong()
            )

            setImageResource(weatherIconImageView, dayWeather.weather[0].icon)
        }
    }

}