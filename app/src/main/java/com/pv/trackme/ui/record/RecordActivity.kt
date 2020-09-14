package com.pv.trackme.ui.record

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.graphics.Color
import android.location.Location
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.observe
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.pv.trackme.R
import com.pv.trackme.service.LocationUpdateService
import com.pv.trackme.service.LocationUpdateService.Companion.ACTION_BROADCAST
import com.pv.trackme.service.LocationUpdateService.Companion.EXTRA_LOCATION
import com.pv.trackme.ui.base.BaseActivity
import com.pv.trackme.util.CommonUtil.toLatLng
import com.pv.trackme.util.ImageUtil
import com.pv.trackme.util.ViewUtil
import kotlinx.android.synthetic.main.activity_record.*
import kotlinx.android.synthetic.main.layout_session_info.*
import org.kodein.di.generic.instance
import timber.log.Timber


class RecordActivity : BaseActivity(), RecordAction {
    companion object {
        fun getStartIntent(context: Context) = Intent(context, RecordActivity::class.java)
    }

    private val factory: RecordViewModelFactory by instance()

    private val viewModel: RecordViewModel by viewModels { factory }

    private lateinit var googleMap: GoogleMap

    private var currentLocationMarker: Marker? = null

    private var alertDialog: AlertDialog? = null

    override fun getLayoutId(): Int = R.layout.activity_record

    override fun initViews() {
        viewModel.locationUpdateStart.observe(this) {
            startService(LocationUpdateService.getStartIntent(this, LocationUpdateService.ACTION_START_SERVICE))
        }
        viewModel.locationUpdateStop.observe(this) {
            startService(LocationUpdateService.getStartIntent(this, LocationUpdateService.ACTION_STOP_SERVICE))
        }
        viewModel.initLocation.observe(this) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it.toLatLng(), 15f))
            val bitmapFromVectorDrawable =
                ImageUtil.getBitmapFromVectorDrawable(this, R.drawable.ic_departure)
            googleMap.addMarker(
                MarkerOptions()
                    .position(it.toLatLng())
                    .title(getString(R.string.txt_marker_departure))
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmapFromVectorDrawable))
            )
        }
        viewModel.updatedLocation.observe(this) {
            val previousLocation = viewModel.getPreviousLocation()!!
            googleMap.addPolyline(
                PolylineOptions()
                    .add(previousLocation.toLatLng(), it.toLatLng())
                    .width(7f)
                    .color(Color.RED)
            )
            if (currentLocationMarker != null) {
                currentLocationMarker!!.position = it.toLatLng()
            } else {
                val bitmapFromVectorDrawable =
                    ImageUtil.getBitmapFromVectorDrawable(this, R.drawable.ic_current_location)
                currentLocationMarker = googleMap.addMarker(
                    MarkerOptions()
                        .position(it.toLatLng())
                        .title(getString(R.string.txt_current_location))
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapFromVectorDrawable))
                )
            }
        }
        viewModel.distance.observe(this) {
            tvDistance.text = getString(
                if (it == null) R.string.txt_distance_holder else R.string.txt_distance,
                it
            )
        }
        viewModel.speed.observe(this) {
            tvSpeed.text = getString(
                if (it == null) R.string.txt_speed_holder else R.string.txt_speed,
                it
            )
        }
        viewModel.time.observe(this) {
            tvTime.text = it ?: getString(R.string.txt_time_holder)
        }
        viewModel.dataSave.observe(this) {
            it?.let {
                setResult(Activity.RESULT_OK)
            }
            finish()
        }
        viewModel.loadingView.observe(this) {
            it?.let {
                alertDialog = ViewUtil.showLoading(this)
            } ?: run {
                alertDialog?.dismiss()
            }
        }
        if (supportFragmentManager.findFragmentById(R.id.flButtons) == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.flButtons, RecordingFragment())
                .commit()
        }
        initMap()
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            try {
                val isParseStyleSuccess =
                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))
                Timber.d("Style parsing isParseStyleSuccess = $isParseStyleSuccess")
            } catch (e: Resources.NotFoundException) {
                Timber.w("Can't find style. Error: $e")
            }
            flButtons.visibility = View.VISIBLE
            viewModel.start()
        }
    }

    override fun onRecordPause() {
        viewModel.pause()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.flButtons, PendingFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onRecordStop() {
        googleMap.snapshot {
            viewModel.stop(this, it)
        }
    }

    override fun onRecordResume() {
        viewModel.resume()
        supportFragmentManager.popBackStack()
    }

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            viewModel.onLocationUpdated(intent.getParcelableExtra(EXTRA_LOCATION) as Location)
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            locationReceiver,
            IntentFilter(ACTION_BROADCAST)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationReceiver)
    }

    override fun onBackPressed() {
        viewModel.stop(this, null)
    }
}