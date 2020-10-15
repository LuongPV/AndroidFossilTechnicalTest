package com.pv.trackme.location

import android.location.Location
import com.pv.trackme.common.callback.DataListener

interface LocationUpdateHelper {

    fun startLocationUpdate(locationListener: DataListener<Location>)

    fun stopLocationUpdate()

}