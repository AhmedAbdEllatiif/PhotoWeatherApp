package com.ahmed.weatherapptask.Utils

import android.location.Location

interface AccessUserLocationHelper {
    fun currentUserLocation(location: Location?)
}