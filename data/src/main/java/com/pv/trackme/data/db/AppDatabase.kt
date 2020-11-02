package com.pv.trackme.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Session::class, Location::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getSessionDAO(): SessionDAO

    abstract fun getLocationDAO() : LocationDAO

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "db-name"
            ).build()
        }

    }

}