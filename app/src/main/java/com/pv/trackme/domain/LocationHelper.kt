package com.pv.trackme.domain

import android.location.Location
import com.pv.trackme.util.DataListener

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