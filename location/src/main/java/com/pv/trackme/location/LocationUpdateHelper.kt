package com.pv.trackme.location

import android.location.Location

interface LocationUpdateHelper {

    fun isLocationUpdating(): Boolean

    fun setLocationUpdating(isLocationUpdating: Boolean)

    fun startLocationUpdate(locationListener: DataListener<Location>)

    fun stopLocationUpdate()

}