package com.pv.trackme.data.repository

import android.os.SystemClock
import com.pv.trackme.data.db.*
import com.pv.trackme.data.model.CalculatedSession
import com.pv.trackme.data.preference.AppPreference
import com.pv.trackme.location.LocationUtil

class SessionRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val appPreference: AppPreference
) : SessionRepository {
    private val sessionDAO: SessionDAO = appDatabase.getSessionDAO()
    private val locationDAO: LocationDAO = appDatabase.getLocationDAO()
    private var currentSessionUid: Long? = null

    override suspend fun saveLocation(lat: Double, lng: Double) {
        currentSessionUid?.let {
            locationDAO.insert(Location(it, lat, lng, SystemClock.currentThreadTimeMillis()))
        }
    }

    override suspend fun createSession() {
        currentSessionUid = sessionDAO.insert(Session(SystemClock.currentThreadTimeMillis()))
    }

    override suspend fun getCalculatedSessions(rowIndex: Int, numberOfItems: Int): List<CalculatedSession> {
        return sessionDAO.getSessions(rowIndex, numberOfItems).map { session ->
            val locations = locationDAO.getLocations(session.uid)
            val totalDistance = LocationUtil.calculateTotalDistance(locations.map {
                android.location.Location("").apply {
                    latitude = it.latitude
                    longitude = it.longitude
                }
            })
            var totalTime: Long? = null
            session.stopTime?.let { totalTime = LocationUtil.calculateTotalTime(session.startTime, it) }
            var averageSpeed: Double? = null
            totalTime?.let { averageSpeed = LocationUtil.calculateSpeed(totalDistance, it) }

            CalculatedSession(session.mapUrl, totalDistance, averageSpeed, totalTime)
        }
    }

    override suspend fun stopSession(mapUrl: String) {
        currentSessionUid?.let {
            sessionDAO.stopSession(it, SystemClock.currentThreadTimeMillis(), mapUrl)
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