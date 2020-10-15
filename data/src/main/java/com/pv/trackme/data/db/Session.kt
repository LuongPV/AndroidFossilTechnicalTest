package com.pv.trackme.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pv.trackme.common.util.DateTimeUtil

@Entity
data class Session(
    @ColumnInfo
    val startTime: Long,
    @ColumnInfo
    var stopTime: Long? = null
) {

    companion object {
        const val TIME_PATTERN_SESSION = "HH:mm:ss"
    }

    @ColumnInfo
    var startTimeString: String? = null

    @ColumnInfo
    var stopTimeString: String? = null

    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    init {
        startTimeString = DateTimeUtil.formatDateTime(TIME_PATTERN_SESSION, startTime)
        stopTime?.let {
            stopTimeString = DateTimeUtil.formatDateTime(TIME_PATTERN_SESSION, it)
        }
    }

    fun setStopTime(stopTime: Long) {
        this.stopTime = stopTime
        stopTimeString = DateTimeUtil.formatDateTime(TIME_PATTERN_SESSION, stopTime)
    }
}