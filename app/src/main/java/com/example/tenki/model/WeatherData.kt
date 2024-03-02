package com.example.tenki.model

class WeatherData {
    data class WeatherData(
        val location: Location,
        val current: Current
    )

    data class Location(
        val name: String,
        val region: String,
        val country: String
    )

    data class Current(
        val tempc: Double,
        val condition: Condition
    )

    data class Condition(
        val text: String,
        val icon: String
    )

}