package com.example.tenki.model

data class WeatherData(
    val location: Location,
    val current: Current,
)

data class Location(
    val name: String,
    val region: String,
    val country: String
)

data class Current(
    val temp_c: Double,
    val condition: Condition,
    val last_updated: String,
    val wind_kph: Double,
    val humidity: Int,
    val gust_kph: Double
)

data class Condition(
    val text: String,
    val icon: String
)