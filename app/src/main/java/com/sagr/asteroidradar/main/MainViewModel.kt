package com.sagr.asteroidradar.main


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.sagr.asteroidradar.Asteroid
import com.sagr.asteroidradar.Constants
import com.sagr.asteroidradar.PictureOfDay
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

enum class AsteroidApiStatus { LOADING, DONE, FAILED }

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AsteroidDatabase.getInstance(application)
    private val asteroidRepository = AsteroidRepository(database)
    private val _filter = MutableLiveData(AsteroidFilter.WEEKLY)

    private val _picOfDay = MutableLiveData<PictureOfDay>()

    val picOfDay: LiveData<PictureOfDay>
        get() = _picOfDay

    private val _loadingProgress = MutableLiveData(AsteroidApiStatus.LOADING)

    val loadingProgress: LiveData<AsteroidApiStatus>
        get() = _loadingProgress


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
            getPicOfTheDay()
        }

    }

    private suspend fun getPicOfTheDay() {
        try {
            asteroidRepository.getPicOfDay()
            asteroidRepository.response.let {
                if (asteroidRepository.response.isSuccessful) {
                    _picOfDay.value = asteroidRepository.response.body()
                }
            }

        } catch (error: HttpException) {
            Timber.d(error.message.toString())

        } catch (error: IOException) {
            Timber.d(error.message.toString())

        }
    }

    private suspend fun refreshAsteroid() {
        _loadingProgress.value = AsteroidApiStatus.LOADING
        try {
            asteroidRepository.refreshAsteroids()
            _loadingProgress.value = AsteroidApiStatus.DONE
        } catch (error: HttpException) {
            _loadingProgress.value = AsteroidApiStatus.FAILED
            Timber.d(error.message.toString())
        } catch (io: IOException) {
            _loadingProgress.value = AsteroidApiStatus.FAILED

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