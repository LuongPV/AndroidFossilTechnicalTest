package com.pv.trackme.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session (
    @ColumnInfo
    val mapImageUrl: String,
    @ColumnInfo
    val distance: Double?,
    @ColumnInfo
    val speed: Double?,
    @ColumnInfo
    val time: Long
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}