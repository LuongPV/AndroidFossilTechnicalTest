package com.pv.trackme.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.pv.trackme.BuildConfig
import com.pv.trackme.R
import com.pv.trackme.constant.CommonConstant.CHANNEL_ID_LOCATION_UPDATE
import com.pv.trackme.data.preference.AppPreference
import com.pv.trackme.domain.LocationUpdateHelper
import com.pv.trackme.util.NotificationUtil.createNotification
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import timber.log.Timber


class LocationUpdateService : Service(), KodeinAware {
    companion object {
        private const val EXTRA_ACTION = "action"
        const val ACTION_START_SERVICE = "ACTION_START_SERVICE"
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
        const val ACTION_BROADCAST = BuildConfig.APPLICATION_ID + ".broadcast"
        const val EXTRA_LOCATION = "EXTRA_LOCATION"

        fun getStartIntent(context: Context, action: String) =
            Intent(context, LocationUpdateService::class.java).apply {
                putExtra(EXTRA_ACTION, action)
            }
    }

    override val kodein by kodein()

    private val locationUpdateHelper: LocationUpdateHelper by instance()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        var count = 0
        when (intent.getStringExtra(EXTRA_ACTION)) {
            ACTION_START_SERVICE -> {
                if (locationUpdateHelper.isLocationUpdating()) {
                    return START_NOT_STICKY
                }
                Timber.d("onStartCommand ACTION_START_SERVICE")
                val dummyLocationData = dummyLocationData()
                initLocationUpdateForegroundService()
                locationUpdateHelper.startLocationUpdate {
                    if (count >= dummyLocationData.size - 1) {
                        Timber.d("Out of the list already")
                        return@startLocationUpdate
                    }
                    Timber.d("Location received = $it")
                    LocalBroadcastManager.getInstance(applicationContext)
                        .sendBroadcast(Intent(ACTION_BROADCAST).apply {
                            putExtra(EXTRA_LOCATION, it.apply {
                                latitude = dummyLocationData[count].first
                                longitude = dummyLocationData[count].second
                            })
                        })
                    count++
                }
                locationUpdateHelper.setLocationUpdating(true)
            }
            ACTION_STOP_SERVICE -> {
                Timber.d("onStartCommand ACTION_STOP_SERVICE")
                locationUpdateHelper.stopLocationUpdate()
                locationUpdateHelper.setLocationUpdating(false)
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    private fun dummyLocationData(): List<Pair<Double, Double>> = listOf(
        Pair(10.801319, 106.683513),
        Pair(10.800364, 106.684560),
        Pair(10.799938, 106.686669),
        Pair(10.801290, 106.687372),
        Pair(10.800614, 106.688583),
        Pair(10.799953, 106.688703),
        Pair(10.799600, 106.689361),
        Pair(10.799556, 106.689914),
        Pair(10.799747, 106.690677),
        Pair(10.799305, 106.691513),
        Pair(10.798725, 106.692157),
        Pair(10.797661, 106.691503),
        Pair(10.797229, 106.693220)
    )

    private fun initLocationUpdateForegroundService() {
        val notificationIntent = packageManager
            .getLaunchIntentForPackage(packageName)!!
            .setPackage(null)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = createNotification(
            this,
            CHANNEL_ID_LOCATION_UPDATE,
            getString(R.string.tit_update_notification),
            getString(R.string.txt_update_notification),
            R.mipmap.ic_launcher,
            pendingIntent
        )
        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}