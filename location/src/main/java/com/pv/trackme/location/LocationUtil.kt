package com.pv.trackme.location

import android.content.Context
import android.provider.Settings


object LocationUtil {

    fun isLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        try {
            locationMode = Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

}