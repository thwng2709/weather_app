package com.example.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

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
}