package com.pv.trackme.location

import android.location.Location
import java.util.*

internal class LocationHelperImpl : LocationHelper {
    private var previousLocation: Location? = null
    private var currentLocation: Location? = null
    private var initialLocationListener: DataListener<Location>? = null
    private var currentLocationListener: DataListener<Location>? = null
    private var distanceListener: DataListener<Double>? = null
    private var distance: Double = 0.0
    private var speedListener: DataListener<Double>? = null
    private val speeds = mutableListOf<Double>()

    override fun getTotalDistance(): Double = distance

    override fun getPreviousLocation(): Location? = previousLocation

    override fun setInitialLocationListener(listener: DataListener<Location>) {
        initialLocationListener = listener
    }

    override fun setCurrentLocationListener(listener: DataListener<Location>) {
        currentLocationListener = listener
    }

    override fun setDistanceListener(listener: DataListener<Double>) {
        distanceListener = listener
    }

    override fun setSpeedListener(listener: DataListener<Double>) {
        speedListener = listener
    }

    override fun onLocationUpdated(location: Location, formattedTime: String) {
        if (previousLocation == null) {
            previousLocation = location
            currentLocation = location
            initialLocationListener?.invoke(location)
            return
        }
        previousLocation = currentLocation
        currentLocation = location
        distanceListener?.let { calculateDistance(previousLocation!!, currentLocation!!) }
        speedListener?.let { calculateSpeed(formattedTime) }
        currentLocationListener?.invoke(currentLocation!!)
    }

    private fun calculateDistance(previousLocation: Location, currentLocation: Location) {
        val newDistance = previousLocation.distanceTo(currentLocation) / 1000f
        distance += newDistance
        distanceListener!!.invoke(distance)
    }

    private fun calculateSpeed(formattedTime: String) {
        val timeInMillis = DateTimeUtil.parseDateTime(CommonConstant.TIME_PATTERN_SESSION, formattedTime)
        val calendar = Calendar.getInstance(Locale.getDefault()).also {
            it.timeInMillis = timeInMillis
        }
        val hour = calendar.get(Calendar.SECOND) / 3600f
        val speed = distance / hour
        speeds.add(speed)
        speedListener!!.invoke(speed)
    }

    override fun getAverageSpeed(): Double {
        var totalSpeed = 0.0
        speeds.forEach {
            totalSpeed += it
        }
        return totalSpeed / speeds.size
    }

}