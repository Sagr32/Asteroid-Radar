package com.sagr.asteroidradar.main


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.sagr.asteroidradar.Asteroid
import com.sagr.asteroidradar.Constants
import com.sagr.asteroidradar.api.AsteroidApi
import com.sagr.asteroidradar.api.asDatabaseModel
import com.sagr.asteroidradar.api.parseAsteroidsJsonResult
import com.sagr.asteroidradar.database.AsteroidDatabase
import com.sagr.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


enum class AsteroidFilter { ALL, TODAY, WEEKLY }


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AsteroidDatabase.getInstance(application)
    private val asteroidRepository = AsteroidRepository(database)
    private val _filter = MutableLiveData(AsteroidFilter.WEEKLY)


    var asteroids: LiveData<List<Asteroid>> = Transformations.switchMap(_filter) {
        when (it) {
            AsteroidFilter.TODAY -> asteroidRepository.todayAsteroids
            AsteroidFilter.WEEKLY -> asteroidRepository.weeklyAsteroids
            else -> asteroidRepository.allAsteroid
        }
    }


    private var _navigateToDetails = MutableLiveData<Asteroid?>()

    val navigateToDetails: LiveData<Asteroid?>
        get() = _navigateToDetails


    init {
        viewModelScope.launch {
            refreshAsteroid()
        }
    }

    private suspend fun refreshAsteroid() {
        try {
            asteroidRepository.refreshAsteroids()
        } catch (error: HttpException) {
            Timber.d(error.message.toString())
        } catch (io: IOException) {
            Timber.d(io.message.toString())
        }
    }


    fun updateFilter(filterFromMenu: AsteroidFilter) {
        _filter.value = filterFromMenu
    }

    fun startNavigation(asteroid: Asteroid) {
        _navigateToDetails.value = asteroid
    }

    fun onDoneNavigation() {
        _navigateToDetails.value = null
    }

}