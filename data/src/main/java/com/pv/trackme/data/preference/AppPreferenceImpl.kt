package com.pv.trackme.data.preference

import android.content.Context

internal class AppPreferenceImpl(private val context: Context) : AppPreference {
    companion object {
        const val PREF_FILE_NAME = "AppPref"
        const val KEY_REQUESTING_LOCATION_UPDATE = "KEY_REQUESTING_LOCATION_UPDATE"
    }

    override fun isUpdatingLocation(): Boolean {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_REQUESTING_LOCATION_UPDATE, false)
    }

    override fun setUpdatingLocation(isUpdating: Boolean) {
        context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_REQUESTING_LOCATION_UPDATE, isUpdating)
            .apply()
    }
}