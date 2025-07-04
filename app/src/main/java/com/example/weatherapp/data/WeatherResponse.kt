package com.example.weatherapp.data

data class WeatherResponse(
    val base: String, // stations
    val clouds: Clouds,
    val cod: Int, // 200
    val coord: Coord,
    val dt: Int, // 1751472796
    val id: Int, // 3163858
    val main: Main,
    val name: String, // Zocca
    val sys: Sys,
    val timezone: Int, // 7200
    val visibility: Int, // 10000
    val weather: List<Weather>,
    val wind: Wind
)