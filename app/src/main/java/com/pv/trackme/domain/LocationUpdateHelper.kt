package com.pv.trackme.domain

import android.location.Location
import com.pv.trackme.util.DataListener

interface LocationUpdateHelper {

    fun isLocationUpdating(): Boolean

    fun setLocationUpdating(isLocationUpdating: Boolean)

    fun startLocationUpdate(locationListener: DataListener<Location>)

    fun stopLocationUpdate()

}