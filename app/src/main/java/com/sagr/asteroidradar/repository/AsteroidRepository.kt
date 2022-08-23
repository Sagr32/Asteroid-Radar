package com.sagr.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.sagr.asteroidradar.Asteroid
import com.sagr.asteroidradar.Constants
import com.sagr.asteroidradar.PictureOfDay
import com.sagr.asteroidradar.api.AsteroidApi
import com.sagr.asteroidradar.api.asDatabaseModel
import com.sagr.asteroidradar.api.parseAsteroidsJsonResult
import com.sagr.asteroidradar.database.AsteroidDatabase
import com.sagr.asteroidradar.database.asDomainModel
import com.sagr.asteroidradar.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response

class AsteroidRepository(private val database: AsteroidDatabase) {

    lateinit var response: Response<PictureOfDay>

    val weeklyAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(
            database.asteroidDao.getWeeklyAsteroids(
                DateUtils.getTodayDate(),
                DateUtils.getEndDate()
            )
        ) {
            it.asDomainModel()
        }

    val todayAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(
            database.asteroidDao.getTodayAsteroids(DateUtils.getTodayDate())
        ) {
            it.asDomainModel()

        }


    val allAsteroid: LiveData<List<Asteroid>> =
        Transformations.map(
            database.asteroidDao.getAsteroids()
        ) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = AsteroidApi.retrofitService.getAsteroids(Constants.API_KEY)
            val result = parseAsteroidsJsonResult(JSONObject(asteroids))
            database.asteroidDao.insertAll(*result.asDatabaseModel())
        }
    }


    suspend fun getPicOfDay() {
        withContext(Dispatchers.IO) {
            response = AsteroidApi.retrofitService.getPicOfDay(Constants.API_KEY)
        }
    }
}