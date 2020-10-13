package com.pv.trackme.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface LocationDAO {

    @Query("SELECT * FROM location WHERE sessionUid = :sessionUid ORDER BY uid DESC")
    suspend fun getPositions(sessionUid: Long): List<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: Location): Long

}