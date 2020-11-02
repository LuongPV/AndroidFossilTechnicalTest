package com.pv.trackme.location

import android.content.Context
import android.location.Location
import android.provider.Settings


object LocationUtil {

    fun isLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        try {
            locationMode = Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

    fun calculateTotalDistance(locations: List<Location>): Double {
        var previousLocation: Location? = null
        var totalDistance = 0.0
        locations.forEach {
            if (previousLocation != null) {
                totalDistance += previousLocation!!.distanceTo(it) / 1000f
            }
            previousLocation = it
        }
        return totalDistance
    }

    fun calculateTotalTime(startTime: Long, stopTime: Long) = (stopTime - startTime) / 1000 / 60 / 60

    fun calculateSpeed(totalDistance: Double, totalTime: Long): Double {
        return totalDistance / totalTime
    }

}