package com.sagr.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {

    @Query("select * from asteroids ORDER BY closeApproachDate ASC")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>


    @Query("SELECT * FROM asteroids WHERE closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate ASC")
    fun getWeeklyAsteroids(
        startDate: String,
        endDate: String
    ): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroids WHERE closeApproachDate = :todayDate")
    fun getTodayAsteroids(todayDate: String): LiveData<List<AsteroidEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)
}