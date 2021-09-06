package com.mankahla.weatherappkotlin.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.mankahla.weatherappkotlin.R
import com.mankahla.weatherappkotlin.data.DayWeather
import java.text.SimpleDateFormat
import java.util.*

fun isConnected(context: Context?): Boolean {
    val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

fun getDayNumberOld(datetime: Long): String {
    val calender = Calendar.getInstance()
    calender.time = Date(datetime * 1000L)
    val dayOfWeek = calender[Calendar.DAY_OF_WEEK]
    return (getDayOfWeek(dayOfWeek))
}

private fun getDayOfWeek(dayOfWeek: Int): String {
    var day = ""
    when (dayOfWeek) {
        1 -> day = "Sunday"
        2 -> day = "Monday"
        3 -> day = "Tuesday"
        4 -> day = "Wednesday"
        5 -> day = "Thursday"
        6 -> day = "Friday"
        7 -> day = "Saturday"
    }
    return day
}

@BindingAdapter("imageResource")
fun setImageResource(imageView: ImageView, icon: String) {
    when (icon) {
        "01n" -> imageView.setImageDrawable(imageView, R.mipmap.ic_sunny)
        "01d" -> imageView.setImageDrawable(imageView, R.mipmap.ic_sunny)
        "02n",
        "02d",
        "03n",
        "03d",
        "04n",
        "04d" -> imageView.setImageDrawable(imageView, R.mipmap.ic_cloudy)
        "09n",
        "09d",
        "10n",
        "10d",
        "11n",
        "11d" -> imageView.setImageDrawable(imageView, R.mipmap.ic_rainy)
        "13n",
        "13d" -> imageView.setImageDrawable(imageView, R.drawable.ic_snow)
        "50n",
        "50d" -> imageView.setImageDrawable(imageView, R.drawable.ic_mist)
    }
}

fun changeLayoutBackground(context: Context, icon: String, linearLayout: LinearLayout) {

    when (icon) {
        "01n",
        "01d" -> linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.sunny))
        "02n",
        "02d",
        "03n",
        "03d",
        "04n",
        "04d" -> linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.cloudy))
        "09n",
        "09d",
        "10n",
        "10d",
        "11d",
        "11n" -> linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.rainy))
        "13n",
        "13d",
        "50d",
        "50n" -> linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
    }
}


private fun ImageView.setImageDrawable(view: ImageView, image: Int) {
    Glide.with(context).load(image).into(view)
}

fun transformList(dayWeatherList: List<DayWeather>): List<DayWeather> {
    val weatherList: MutableList<DayWeather> = mutableListOf()


    for (dayWeather in dayWeatherList) {

        if (!isSameDay(dayWeather.dt) && isMidDay(dayWeather.dt)) {
            weatherList.add(dayWeather)
        }
    }
    return weatherList
}

private fun isMidDay(dateTime: Long): Boolean {
    val midday = "14 00 00"
    val time = SimpleDateFormat("HH mm ss").format(Date(dateTime * 1000L))
    return midday == time
}

private fun isSameDay(dateTime: Long): Boolean {
    val fmt = SimpleDateFormat("dd-MM-yyyy")
    val currentDate = Calendar.getInstance().time
    val weatherDate = Date(dateTime * 1000L)
    return fmt.format(currentDate) == fmt.format(weatherDate)
}