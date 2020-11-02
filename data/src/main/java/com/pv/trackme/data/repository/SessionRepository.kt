package com.pv.trackme.data.repository

import com.pv.trackme.data.model.CalculatedSession

interface SessionRepository {

    suspend fun saveLocation(lat: Double, lng: Double)

    suspend fun createSession()

    suspend fun getCalculatedSessions(rowIndex: Int, numberOfItems: Int): List<CalculatedSession>

    suspend fun stopSession(mapUrl: String)

    fun isLocationUpdating(): Boolean

    fun setLocationUpdating(isUpdating: Boolean)

}