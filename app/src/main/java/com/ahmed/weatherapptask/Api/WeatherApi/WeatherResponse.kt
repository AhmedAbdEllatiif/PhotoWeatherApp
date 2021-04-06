package com.ahmed.weatherapptask.Api.WeatherApi

import com.google.gson.annotations.SerializedName

class WeatherResponse {


    @SerializedName("coord") var coord : Coord? = null

    @SerializedName("weather") var weather : List<Weather> = ArrayList<Weather>()

    @SerializedName("base") var base: String?  = null
    @SerializedName("main") var main : Main? = null
    @SerializedName("visibility") var visibility : Float = 0.toFloat()
    @SerializedName("wind") var wind : Wind? = null
    @SerializedName("clouds") var clouds : Clouds? = null
    @SerializedName("dt") var dt : Float = 0.toFloat()
    @SerializedName("sys") var sys : Sys? = null
    @SerializedName("timezone") var timezone : Float = 0.toFloat()
    @SerializedName("id") var id : Int = 0
    @SerializedName("name") var name : String? = null
    @SerializedName("cod") var cod : Float = 0.toFloat()



    data class Weather (
        @SerializedName("id") val id : Int,
        @SerializedName("main") val main : String,
        @SerializedName("description") val description : String,
        @SerializedName("icon") val icon : String
    )


    data class Main (
        @SerializedName("temp") val temp : Double,
        @SerializedName("feels_like") val feels_like : Double,
        @SerializedName("temp_min") val temp_min : Double,
        @SerializedName("temp_max") val temp_max : Double,
        @SerializedName("pressure") val pressure : Int,
        @SerializedName("humidity") val humidity : Int
    )


    data class Coord (
        @SerializedName("lon") val lon : Double,
        @SerializedName("lat") val lat : Double
    )


    data class Clouds (
        @SerializedName("all") val all : Int
    )

    data class Wind (
        @SerializedName("speed") val speed : Double,
        @SerializedName("deg") val deg : Int
    )

    data class Sys (
        @SerializedName("type") val type : Int,
        @SerializedName("id") val id : Int,
        @SerializedName("message") val message : Double,
        @SerializedName("country") val country : String,
        @SerializedName("sunrise") val sunrise : Int,
        @SerializedName("sunset") val sunset : Int
    )


}