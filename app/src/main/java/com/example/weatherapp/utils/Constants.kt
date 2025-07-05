package com.example.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.round

object Constants {
    const val APP_ID = "d0b2f652a2ed798701544354bde7e299"
    const val BASE_URL = "https://api.openweathermap.org/data/"
    const val METRIC_UNIT = "metric"

    // Define a function called isNetworkAvailable
    fun isNetWorkAvailable(context: Context): Boolean {
        // Get an instance of ConnectivityManager using context
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // This way is for Android 23+
        val network: Network = connectivityManager.activeNetwork ?: return false

        // Get network capabilities of the active network
        val networkCapabilities: NetworkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        // Check type of network capabilities and return true if it is Wi-fi/Cellular/Ethernet
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }

        /*
        Way for Android under 22

        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
         */
    }

    /**
     * Convert time stamp to time
     * @param time Time stamp
     * @param timeZoneOffset Time zone offset
     * @return Time in HH:mm format
     */
    fun convertLongToTime(time: Long, timeZoneOffset: Long): String {
        val date = Date((time + timeZoneOffset) * 1000L)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        timeFormat.timeZone = TimeZone.getTimeZone("UTC")
        return timeFormat.format(date)
    }

    // Format properties
    fun formatWindSpeed(speed: Double): String = "$speed m/s"
    fun formatCelsius(degree: Double): String = "${round(degree).toInt()}°C"
    fun formatMaxTemp(max: String, temp: Double): String = "$max Temp: ${formatCelsius(temp)}"
    fun formatHumidity(humidity: Int): String = "$humidity%"
    fun currentDateFormat(): String {
        val date = Date()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(date)
    }

    fun formatDescription(description: String): String {
        // Capitalize the first letter of each word in the description
        val words = description.split(" ").toMutableList()
        for (i in words.indices) {
            words[i] = words[i].replaceFirstChar { it.uppercase() }
        }
        return words.joinToString(" ")
    }
}