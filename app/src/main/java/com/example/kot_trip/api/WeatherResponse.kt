package com.example.kot_trip.api

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String
)

data class Main(
    val temp: Double,
    val pressure: Int,
    val humidity: Int
)

data class Weather(
    val description: String,
    val icon: String
)