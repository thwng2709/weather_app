package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.network.WeatherServiceApi
import com.example.weatherapp.utils.Constants
import com.example.weatherapp.utils.Constants.BASE_URL
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.entries.all { it.value }
        if (isGranted) {
            // Get location data
            getLocationData()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocationPermissions()
    }

    // Check and request location permissions
    private fun requestLocationPermissions() {
        val (fineLocationGranted: Boolean, coarseLocationGranted: Boolean) = checkLocationPermission()

        if (fineLocationGranted || coarseLocationGranted) {
            // Get location data
            getLocationData()
        } else {
            requestLocationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

    }

    private fun checkLocationPermission(): Pair<Boolean, Boolean> {
        val fineLocationGranted: Boolean = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted: Boolean = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return Pair(fineLocationGranted, coarseLocationGranted)
    }

    // Get location data every 5 seconds
    private fun getLocationData() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setMinUpdateIntervalMillis(2000)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location: Location? = result.lastLocation
                if (location != null) {
                    getWeatherData(location.latitude, location.longitude)
                }
            }
        }

        val (fineGranted, coarseGranted) = checkLocationPermission()
        if (!fineGranted && !coarseGranted) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
    }

    private fun getWeatherData(latitude: Double, longitude: Double) {
        if (Constants.isNetWorkAvailable(this)) {
            // Get data using Retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val serviceApi = retrofit.create(WeatherServiceApi::class.java)

            val call = serviceApi.getWeatherDetails(latitude, longitude)
            call.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse?>,
                    response: Response<WeatherResponse?>
                ) {
                    if (response.isSuccessful) {
                        val weather: WeatherResponse? = response.body()
                        for (i in weather?.weather!!.indices) {
                            binding.textViewSunset.text = weather.sys.sunset.toString()
                            binding.textViewSunrise.text = weather.sys.sunrise.toString()
                            binding.textViewStatus.text = weather.weather[i].description
                            binding.textViewAddress.text = weather.name
                            binding.textViewTempMax.text = weather.main.temp_max.toString()
                            binding.textViewTempMin.text = weather.main.temp_min.toString()
                            binding.textViewTemp.text = weather.main.temp.toString()
                            binding.textViewHumidity.text = weather.main.humidity.toString()
                            binding.textViewPressure.text = weather.main.pressure.toString()
                            binding.textViewWind.text = weather.wind.speed.toString()
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Fail to get response",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<WeatherResponse?>,
                    t: Throwable
                ) {
                    TODO("Not yet implemented")
                }

            })

        } else {
            Toast.makeText(this, "No network", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdate()
    }

    private fun stopLocationUpdate() {
        if (::locationRequest.isInitialized && ::locationCallback.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}