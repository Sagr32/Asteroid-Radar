package com.sagr.asteroidradar.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sagr.asteroidradar.Constants

@Database(entities = [AsteroidEntity::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {

    abstract val asteroidDao: AsteroidDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: AsteroidDatabase

        fun getInstance(context: android.content.Context): AsteroidDatabase {
            if (!::INSTANCE.isInitialized) {
                synchronized(AsteroidDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java, Constants.DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }

}