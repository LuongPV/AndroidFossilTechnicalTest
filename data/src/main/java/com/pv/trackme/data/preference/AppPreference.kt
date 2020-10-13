package com.pv.trackme.data.preference

internal interface AppPreference {

    fun isUpdatingLocation(): Boolean

    fun setUpdatingLocation(isUpdating: Boolean)

}