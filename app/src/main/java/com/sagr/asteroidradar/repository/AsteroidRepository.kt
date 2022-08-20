package com.sagr.asteroidradar.repository

import android.util.Log
import com.sagr.asteroidradar.Constants
import com.sagr.asteroidradar.PictureOfDay
import com.sagr.asteroidradar.api.AsteroidApi
import com.sagr.asteroidradar.api.asDatabaseModel
import com.sagr.asteroidradar.api.parseAsteroidsJsonResult
import com.sagr.asteroidradar.database.AsteroidDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException

class AsteroidRepository(private val database: AsteroidDatabase) {


    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroids = AsteroidApi.retrofitService.getAsteroids(Constants.API_KEY)
                val result = parseAsteroidsJsonResult(JSONObject(asteroids))
                Log.v("AsteroidRepository", result.toString())
                database.asteroidDao.insertAll(*result.asDatabaseModel())
                Log.v("AsteroidRepository", "Data inserted")

            } catch (error: HttpException) {
                Log.v("AsteroidRepository", error.message.toString())
            }


        }
    }

    suspend fun getPicOfDay() {
        withContext(Dispatchers.IO) {
            AsteroidApi.retrofitService.getPicOfDay(Constants.API_KEY)
        }
    }
}