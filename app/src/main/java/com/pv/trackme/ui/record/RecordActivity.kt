package com.pv.trackme.ui.record

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.pv.trackme.R
import com.pv.trackme.model.RecordActionData
import com.pv.trackme.model.RecordData
import com.pv.trackme.service.LocationUpdateService
import com.pv.trackme.ui.base.BaseActivity
import com.pv.trackme.ui.record.fragment.PendingFragment
import com.pv.trackme.ui.record.fragment.RecordingFragment
import com.pv.trackme.util.ImageUtil
import com.pv.trackme.util.ViewUtil
import kotlinx.android.synthetic.main.activity_record.*
import kotlinx.android.synthetic.main.layout_session_info.*
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance
import timber.log.Timber


class RecordActivity : BaseActivity(), RecordAction {
    companion object {
        private const val CAMERA_ZOOM = 15f
        private const val WITH_POLYLINE = 7f

        fun getStartIntent(context: Context) = Intent(context, RecordActivity::class.java)
    }

    private val factory: RecordViewModelFactory by instance()

    private val viewModel: RecordViewModel by viewModels { factory }

    private lateinit var googleMap: GoogleMap

    private var currentLocationMarker: Marker? = null

    private var alertDialog: AlertDialog? = null

    private var previousLocation: RecordData.Location? = null

    override fun getLayoutId(): Int = R.layout.activity_record

    override fun initViews() {
        viewModel.recordData.observe(this) {
            when (it) {
                is RecordData.Distance -> tvDistance.text = getString(
                    if (it.data == null) R.string.txt_distance_holder else R.string.txt_distance, it
                )
                is RecordData.Speed -> tvSpeed.text = getString(
                    if (it.data == null) R.string.txt_speed_holder else R.string.txt_speed, it
                )
                is RecordData.Time -> tvTime.text = it.data ?: getString(R.string.txt_time_holder)
                is RecordData.Location -> {
                    lifecycleScope.launch {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it.toLatLng(), CAMERA_ZOOM))
                        if (previousLocation == null) {
                            addMarkerToMap(it, R.drawable.ic_departure, R.string.txt_marker_departure)
                            return@launch
                        }
                        googleMap.addPolyline(
                            PolylineOptions().add(previousLocation!!.toLatLng(), it.toLatLng()).width(WITH_POLYLINE).color(Color.RED)
                        )
                        if (currentLocationMarker != null) {
                            currentLocationMarker!!.position = it.toLatLng()
                        } else {
                            currentLocationMarker = addMarkerToMap(it, R.drawable.ic_current_location, R.string.txt_current_location)
                        }
                        previousLocation = it
                    }
                }
            }
        }
        viewModel.recordAction.observe(this) {
            when (it!!) {
                RecordActionData.LOADING_START -> alertDialog = ViewUtil.showLoading(this)
                RecordActionData.LOADING_STOP -> alertDialog?.dismiss()
                RecordActionData.RECORD_STOP -> finish()
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
            startService(LocationUpdateService.getStartIntent(this, LocationUpdateService.ACTION_START_SERVICE))
        }
    }

    private suspend fun addMarkerToMap(location: RecordData.Location, markerIcon: Int, markerTitle: Int): Marker {
        val bitmapFromVectorDrawable =
            ImageUtil.getBitmapFromVectorDrawable(this@RecordActivity, markerIcon)
        return googleMap.addMarker(
            MarkerOptions()
                .position(location.toLatLng())
                .title(getString(markerTitle))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapFromVectorDrawable))
        )
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
        startService(LocationUpdateService.getStartIntent(this, LocationUpdateService.ACTION_STOP_SERVICE))
        googleMap.snapshot {
            viewModel.stop(this, it)
        }
    }

    override fun onRecordResume() {
        viewModel.resume()
        supportFragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        viewModel.stop(this, null)
    }
}