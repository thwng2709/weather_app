package com.example.weatherapp.network

import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServiceApi {

    @GET("2.5/weather")
    fun getWeatherDetails(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appId: String = Constants.APP_ID,
        @Query("units") metric: String = Constants.METRIC_UNIT
    ): Call<WeatherResponse>


}