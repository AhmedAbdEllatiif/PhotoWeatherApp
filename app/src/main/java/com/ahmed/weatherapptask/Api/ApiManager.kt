package com.ahmed.weatherapptask.Api

import com.ahmed.weatherapptask.Api.WeatherApi.WeatherService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiManager {

    //Base url
    private var retrofit: Retrofit? = null


    /**
     * Initialize retrofit as singleton
     * */
    private fun getInstance(): Retrofit? {
        if (retrofit != null) {
            return retrofit
        }
        retrofit = buildRetrofit()
        return retrofit
    }

    /**
     * Build a new Retrofit
     * */
    private fun buildRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient())
            .build().also { retrofit = it }
    }

    /**
     * Build okHttpClient
     * */
    private fun okHttpClient():OkHttpClient{
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Initialize weather services
     * */
    fun getWeatherServices(): WeatherService? {
        return getInstance()!!.create(WeatherService::class.java)
    }


    companion object {
        private const val baseUrl: String = "https://api.openweathermap.org/"
        public const val baseIconUrl: String = " http://openweathermap.org/img/wn/"
    }


}