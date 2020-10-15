package com.pv.trackme.data.repository

interface SessionRepository {

    suspend fun saveLocation(lat: Double, lng: Double)

    suspend fun createSession()

    suspend fun stopSession()

    fun isLocationUpdating(): Boolean

    fun setLocationUpdating(isUpdating: Boolean)

}