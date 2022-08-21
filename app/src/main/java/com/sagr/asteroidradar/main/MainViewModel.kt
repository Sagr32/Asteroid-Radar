package com.sagr.asteroidradar.main


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sagr.asteroidradar.Asteroid
import com.sagr.asteroidradar.database.AsteroidDatabase
import com.sagr.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AsteroidDatabase.getInstance(application)
    private val asteroidRepository = AsteroidRepository(database)

    val asteroids = asteroidRepository.asteroids

    private var _navigateToDetails = MutableLiveData<Asteroid?>()

    val navigateToDetails: LiveData<Asteroid?>
        get() = _navigateToDetails


    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }


    fun startNavigation(asteroid: Asteroid) {
        _navigateToDetails.value = asteroid
    }

    fun onDoneNavigation() {
        _navigateToDetails.value = null
    }

}