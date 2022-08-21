package com.sagr.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {

    @Query("select * from asteroids")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)
}