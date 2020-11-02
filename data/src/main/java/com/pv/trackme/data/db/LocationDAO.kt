package com.pv.trackme.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDAO {

    @Query("SELECT * FROM location WHERE sessionUid = :sessionUid ORDER BY uid DESC")
    fun getLocationsLiveData(sessionUid: Int): LiveData<Location>

    @Query("SELECT * FROM location WHERE sessionUid = :sessionUid ORDER BY uid DESC")
    fun getLocations(sessionUid: Int): List<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: Location): Long

}