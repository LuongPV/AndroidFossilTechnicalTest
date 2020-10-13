package com.pv.trackme.location

import android.location.Location

interface LocationHelper {

    fun getAverageSpeed(): Double

    fun getTotalDistance(): Double

    fun getPreviousLocation(): Location?

    fun setInitialLocationListener(listener: DataListener<Location>)

    fun setCurrentLocationListener(listener: DataListener<Location>)

    fun setDistanceListener(listener: DataListener<Double>)

    fun setSpeedListener(listener: DataListener<Double>)

    fun onLocationUpdated(location: Location, formattedTime: String)


}