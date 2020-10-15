package com.pv.trackme.model

import com.google.android.gms.maps.model.LatLng

sealed class RecordData {

    data class Distance(val data: Double?) : RecordData()

    data class Speed(val data: Double?) : RecordData()

    data class Time(val data: String?) : RecordData()

    data class Location(val latitude: Double, val longitude: Double) : RecordData() {
        fun toLatLng() = LatLng(latitude, longitude)
    }

}