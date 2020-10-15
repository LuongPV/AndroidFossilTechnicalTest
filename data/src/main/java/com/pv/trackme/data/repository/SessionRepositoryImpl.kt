package com.pv.trackme.data.repository

import android.os.SystemClock
import com.pv.trackme.data.db.Location
import com.pv.trackme.data.db.LocationDAO
import com.pv.trackme.data.db.Session
import com.pv.trackme.data.db.SessionDAO
import com.pv.trackme.data.preference.AppPreference

class SessionRepositoryImpl(
    private val sessionDAO: SessionDAO,
    private val locationDAO: LocationDAO,
    private val appPreference: AppPreference
) : SessionRepository {
    private var currentSessionUid: Long? = null

    override suspend fun saveLocation(lat: Double, lng: Double) {
        currentSessionUid?.let {
            locationDAO.insert(Location(it, lat, lng, SystemClock.currentThreadTimeMillis()))
        }
    }

    override suspend fun createSession() {
        currentSessionUid = sessionDAO.insert(Session(SystemClock.currentThreadTimeMillis()))
    }

    override suspend fun stopSession() {
        currentSessionUid?.let {
            sessionDAO.stopSession(it, SystemClock.currentThreadTimeMillis())
            currentSessionUid = null
        }
    }

    override fun isLocationUpdating(): Boolean {
        return appPreference.isUpdatingLocation()
    }

    override fun setLocationUpdating(isUpdating: Boolean) {
        appPreference.setUpdatingLocation(isUpdating)
    }

}