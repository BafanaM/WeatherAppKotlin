package com.mankahla.weatherappkotlin.data

data class WeatherResponse(
    val base: String,
    val main: Main,
    val weather: List<Weather>
)