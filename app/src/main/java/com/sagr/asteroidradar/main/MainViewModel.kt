package com.sagr.asteroidradar.main


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sagr.asteroidradar.database.AsteroidDatabase
import com.sagr.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AsteroidDatabase.getInstance(application)
    private val asteroidRepository = AsteroidRepository(database)

    val asteroids = asteroidRepository.asteroids

    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }
}