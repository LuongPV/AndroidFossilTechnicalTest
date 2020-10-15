package com.pv.trackme.ui.record

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pv.trackme.common.util.DateTimeUtil
import com.pv.trackme.constant.CommonConstant
import com.pv.trackme.data.repository.SessionRepository
import com.pv.trackme.model.RecordActionData
import com.pv.trackme.model.RecordData
import com.pv.trackme.util.ImageUtil
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class RecordViewModel(
    private val sessionRepository: SessionRepository,
    private val handler: Handler
) : ViewModel() {
    val recordAction: MutableLiveData<RecordActionData> = MutableLiveData()
    val recordData: MutableLiveData<RecordData> = MutableLiveData()
    var currentTimestamp: Long = 0

    fun start() {
        viewModelScope.launch {
            val cal = DateTimeUtil.getNonTimeCalendar()
            recordData.value = RecordData.Time(
                DateTimeUtil.formatDateTime(
                    CommonConstant.TIME_PATTERN_SESSION,
                    cal.timeInMillis
                )
            )
            tickTime(cal)
            sessionRepository.createSession()
        }
    }

    private fun tickTime(cal: Calendar) {
        handler.post(object : Runnable {
            override fun run() {
                handler.postDelayed(this, 1000)
                cal.add(Calendar.SECOND, 1)
                recordData.value =
                    RecordData.Time(DateTimeUtil.formatDateTime(CommonConstant.TIME_PATTERN_SESSION, cal.timeInMillis))
                currentTimestamp = cal.timeInMillis
            }
        })
    }

    fun pause() {
        handler.removeCallbacksAndMessages(null)
    }

    fun resume() {
        tickTime(Calendar.getInstance(Locale.getDefault()).apply {
            timeInMillis = currentTimestamp
        })
    }

    fun stop(context: Context, mapSnapshotBitmap: Bitmap?) {
        if (mapSnapshotBitmap == null) {
            recordAction.value = RecordActionData.RECORD_STOP
            return
        }
        viewModelScope.launch {
            sessionRepository.stopSession()
            recordAction.value = RecordActionData.LOADING_START
            val imagePath = ImageUtil.saveBitmapToFile(
                context,
                mapSnapshotBitmap
            ) ?: return@launch
            Timber.d("Done saving map snapshot image to storage location = $imagePath")
            recordAction.value = RecordActionData.LOADING_STOP
            recordAction.value = RecordActionData.RECORD_STOP
        }
    }

}