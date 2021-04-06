package com.ahmed.weatherapptask.Api.WeatherApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/data/2.5/weather?appid=f92bc632c261b360d774fed913b69983")
    fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
    ): Call<WeatherResponse>
}