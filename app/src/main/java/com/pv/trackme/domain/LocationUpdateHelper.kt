package com.pv.trackme.domain

import android.location.Location
import com.pv.trackme.util.DataListener

interface LocationUpdateHelper {

    fun startLocationUpdate(locationListener: DataListener<Location>)

    fun stopLocationUpdate()

}