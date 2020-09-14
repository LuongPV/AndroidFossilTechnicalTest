package com.pv.trackme.util

import android.location.Location
import com.google.android.gms.maps.model.LatLng

object CommonUtil {

    fun Location.toLatLng() = LatLng(this.latitude, this.longitude)

    fun Double?.isNotAvailable() = this == null || this == 0.0

}