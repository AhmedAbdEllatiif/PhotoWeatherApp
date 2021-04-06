package com.ahmed.weatherapptask.Utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log

class LocationProvider(private val context: Context, private val accessUserLocation: AccessUserLocationHelper) :
    LocationListener {

    // GPS status
    private var isGPSEnabled = false

    // Network status
    private var isNetworkEnabled = false

    // location
    private var location: Location? = null

    // The minimum distance to change Updates in meters
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f // 10 meters

    // The minimum time between updates in milliseconds
    private val MIN_TIME_BW_UPDATES: Long = 5000 // 2 seconds


    private var locationManager: LocationManager =
        context.getSystemService(LOCATION_SERVICE) as LocationManager




    /**
     * call back of location changed
     * */
    override fun onLocationChanged(location: Location) {}


    /**
     * To init current user location
     * */
    @SuppressLint("MissingPermission")
    fun initCurrentLocation(): Location? {
        try {
            //check if both (GPS & Network) are not enabled
            if (!isLocationEnabled()) return null

            //Try get location from gps
            if (isGPSEnabled) {
                location = getGpsLocation()
            }

            //when failed to get location from gps try network location
            if (location == null && isNetworkEnabled){
                location = getNetworkLocation()
            }

        } catch (e: Exception) {
            Log.e(TAG, "getCurrentLocation: ${e.message}")
        }

        //Send call back of current location
        accessUserLocation.currentUserLocation(location)

        return location
    }


    /**
     * To get location from gps
     * */
    @SuppressLint("MissingPermission")
    private fun getGpsLocation():Location?{
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
            MIN_TIME_BW_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES,
            this)
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    }


    /**
     * To get location from Network
     * */
    @SuppressLint("MissingPermission")
    private fun getNetworkLocation():Location?{
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
            MIN_TIME_BW_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES,
            this)
        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }


    /**
     * @return false if both GPS_PROVIDER & NETWORK_PROVIDER not enabled
     * */
    private fun isLocationEnabled(): Boolean {
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGPSEnabled && !isNetworkEnabled) return false
        return true

    }











    companion object {
        private const val TAG = "LocationProvider"
    }

}