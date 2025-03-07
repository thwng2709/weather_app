package com.example.weatherapp.utils

import android.content.Context
import android.net.NetworkCapabilities
import android.os.Build

object Constant {
    // Define a function called isNetworkAvailable that takes a Context as a parameter and returns a Boolean value
    fun isNetworkAvailable(context: Context): Boolean {
        // Get an instance of the ConnectivityManager using the Context parameter
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager

        // Check if the device's API is level 23 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        } else {
            // Get the active network info and return a true value if it is connected or connecting
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
        }
    }
}