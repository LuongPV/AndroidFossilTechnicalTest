package com.pv.trackme.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.pv.trackme.R
import com.pv.trackme.constant.CommonConstant.CHANNEL_ID_LOCATION_UPDATE
import com.pv.trackme.data.repository.SessionRepository
import com.pv.trackme.location.LocationUpdateHelper
import com.pv.trackme.util.NotificationUtil.createNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import timber.log.Timber


class LocationUpdateService : Service(), KodeinAware, CoroutineScope {
    companion object {
        private const val EXTRA_ACTION = "action"
        const val ACTION_START_SERVICE = "ACTION_START_SERVICE"
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"

        fun getStartIntent(context: Context, action: String) =
            Intent(context, LocationUpdateService::class.java).apply {
                putExtra(EXTRA_ACTION, action)
            }
    }

    override val kodein by kodein()

    private val locationUpdateHelper: LocationUpdateHelper by instance()

    private val sessionRepository: SessionRepository by instance()

    private var coroutineJob = Job()

    override val coroutineContext = Dispatchers.IO + coroutineJob

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.getStringExtra(EXTRA_ACTION)) {
            ACTION_START_SERVICE -> {
                if (sessionRepository.isLocationUpdating()) {
                    return START_NOT_STICKY
                }
                Timber.d("onStartCommand ACTION_START_SERVICE")
                initLocationUpdateForegroundService()
                locationUpdateHelper.startLocationUpdate {
                    Timber.d("Location received = $it")
                    launch {
                        sessionRepository.saveLocation(it.latitude, it.longitude)
                    }
                }
                sessionRepository.setLocationUpdating(true)
            }
            ACTION_STOP_SERVICE -> {
                Timber.d("onStartCommand ACTION_STOP_SERVICE")
                locationUpdateHelper.stopLocationUpdate()
                sessionRepository.setLocationUpdating(false)
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

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

    override fun onDestroy() {
        super.onDestroy()
        coroutineJob.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}