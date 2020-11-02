package com.pv.trackme.data.db

import androidx.room.*

@Dao
interface SessionDAO {

    @Query("SELECT * FROM session ORDER BY uid DESC LIMIT :rowIndex, :numberOfItems")
    suspend fun getSessions(rowIndex: Int, numberOfItems: Int): List<Session>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: Session): Long

    @Query("Update Session SET stopTime = :stopTime AND mapUrl = :mapUrl WHERE uid = :sessionUid")
    suspend fun stopSession(sessionUid: Long, stopTime: Long, mapUrl: String? = null)

}