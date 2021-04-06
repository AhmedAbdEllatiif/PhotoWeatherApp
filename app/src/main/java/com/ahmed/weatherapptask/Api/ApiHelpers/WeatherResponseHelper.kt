package com.ahmed.weatherapptask.Api.ApiHelpers

import com.ahmed.weatherapptask.Api.WeatherApi.WeatherResponse

interface WeatherResponseHelper {

    /**
     * when response.code is 200
     * response.body() not null
     * */
    fun onWeatherReady(weatherResponse: WeatherResponse)

    /**
     * when response.code is not 200
     * */
    fun onErrorBodyWeather(errorBody: String)


    /**
     * when failed to connect with server
     * */
    fun onBadInternetConnection(str:String)

}