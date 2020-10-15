package com.pv.trackme.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location (
    @ColumnInfo
    val sessionUid: Long,
    @ColumnInfo
    val latitude: Double,
    @ColumnInfo
    val longitude: Double,
    @ColumnInfo
    val time: Long
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}