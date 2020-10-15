package com.pv.trackme.data.preference

interface AppPreference {

    fun isUpdatingLocation(): Boolean

    fun setUpdatingLocation(isUpdating: Boolean)

}