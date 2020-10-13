package com.pv.trackme.data.repository

import com.pv.trackme.data.db.LocationDAO
import com.pv.trackme.data.db.SessionDAO
import com.pv.trackme.data.preference.AppPreference

class SessionRepositoryImpl(
    private val sessionDAO: SessionDAO,
    private val locationDAO: LocationDAO,
    private val appPreference: AppPreference
) : SessionRepository {
    private val currentSessionUid: Long? = null

    override suspend fun saveLocation(lat: Double, lng: Double) {
        
    }

}