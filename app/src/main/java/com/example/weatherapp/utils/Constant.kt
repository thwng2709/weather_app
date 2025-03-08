package com.example.weatherapp.utils

import android.content.Context
import android.net.NetworkCapabilities

object Constant {
    const val BASE_URL = "https://api.openweathermap.org/data/"
    const val APP_ID = "d0b2f652a2ed798701544354bde7e299"
    const val METRIC_UNIT = "metric"

    // Define a function called isNetworkAvailable that takes a Context as a parameter and returns a Boolean value
    fun isNetworkAvailable(context: Context): Boolean {
        // Get an instance of the ConnectivityManager using the Context parameter
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager

        // Get the currently active network and return a false value if it is null
        val network = connectivityManager.activeNetwork ?: return false

        // Get the capabilities of the active network and return false if it is null
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        // Return true if the network is connected
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}