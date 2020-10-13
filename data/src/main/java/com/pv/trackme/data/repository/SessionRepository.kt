package com.pv.trackme.data.repository

interface SessionRepository {

    suspend fun saveLocation(lat: Double, lng: Double)

}