package com.ahmed.weatherapptask.Api

import android.util.Log
import com.ahmed.weatherapptask.Api.ApiHelpers.WeatherResponseHelper
import com.ahmed.weatherapptask.Api.WeatherApi.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepository {

    //Initialize Api Manager
    private val apiManager = ApiManager()



    /**
     * To fetch weather data from Server
     * */
    fun getWeatherData(lat: String, lon: String, weatherHelper:WeatherResponseHelper){

        //Initialize callWeatherData
        val callWeatherData: Call<WeatherResponse>? = apiManager.getWeatherServices()?.getCurrentWeatherData(lat, lon)

        callWeatherData?.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {

                Log.d(TAG, "onResponse: getWeatherData >> response.code() >> ${response.code()}")

                //Response is Successful
                if (response.isSuccessful) {
                    if(response.body() != null){
                        weatherHelper.onWeatherReady(response.body() as WeatherResponse)
                    }
                }

                //Response is not Successful
                else{
                    weatherHelper.onErrorBodyWeather(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                weatherHelper.onBadInternetConnection(t.message.toString())
            }
        })
    }






    companion object {
        private const val TAG = "ApiRepository"
    }

}