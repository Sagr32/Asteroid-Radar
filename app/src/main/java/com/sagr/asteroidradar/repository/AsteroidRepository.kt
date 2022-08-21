package com.sagr.asteroidradar.repository

import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroids = AsteroidApi.retrofitService.getAsteroids(Constants.API_KEY)
                val result = parseAsteroidsJsonResult(JSONObject(asteroids))
                Timber.d(result.toString())
                database.asteroidDao.insertAll(*result.asDatabaseModel())
                Timber.d("Data inserted")


            } catch (error: HttpException) {
                Timber.d(error.message.toString())
            } catch (io: IOException) {
                Timber.d(io.message.toString())

            }


        }
    }

    suspend fun getPicOfDay() {
        withContext(Dispatchers.IO) {
            AsteroidApi.retrofitService.getPicOfDay(Constants.API_KEY)
        }
    }
}