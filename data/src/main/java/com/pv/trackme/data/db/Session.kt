package com.pv.trackme.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class Session (
    @ColumnInfo
    val startTime: Long
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}