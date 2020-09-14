package com.pv.trackme.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SessionDAO {

    @Query("SELECT * FROM session ORDER BY uid DESC LIMIT :rowIndex, :numberOfItems")
    suspend fun getSessions(rowIndex: Int, numberOfItems: Int): List<Session>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: Session): Long

}