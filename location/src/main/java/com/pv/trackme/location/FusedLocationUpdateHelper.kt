package com.pv.trackme.location

import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import timber.log.Timber

internal class FusedLocationUpdateHelper(context: Context) : LocationUpdateHelper {

    companion object {
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2
    }

    private var locationRequest: LocationRequest? = null

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var locationCallback: LocationCallback? = null

    var locationListener: DataListener<Location>? = null

    private var isLocationUpdating: Boolean = false

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationListener?.invoke(locationResult.lastLocation)
            }
        }
        createLocationRequest()
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    override fun isLocationUpdating() = isLocationUpdating

    override fun setLocationUpdating(isLocationUpdating: Boolean) {
        this.isLocationUpdating = isLocationUpdating
    }

    override fun startLocationUpdate(locationListener: DataListener<Location>) {
        this.locationListener = locationListener
        getLastLocation()
        requestLocationUpdate()
    }

    override fun stopLocationUpdate() {
        removeLocationUpdate()
    }

    private fun getLastLocation() {
        try {
            fusedLocationClient!!.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        locationListener?.invoke(task.result!!)
                    } else {
                        Timber.d("Failed to get location.")
                    }
                }
        } catch (unlikely: SecurityException) {
            Timber.w("Lost location permission.$unlikely")
        }
    }

    private fun requestLocationUpdate() {
        Timber.d("Requesting location updates")
        try {
            fusedLocationClient!!.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
            Timber.d("Lost location permission. Could not request updates. $unlikely")
        }
    }

    private fun removeLocationUpdate() {
        Timber.d("Removing location updates")
        try {
            fusedLocationClient!!.removeLocationUpdates(locationCallback)
        } catch (unlikely: SecurityException) {
            Timber.w("Lost location permission. Could not remove updates. $unlikely")
        }
    }

}