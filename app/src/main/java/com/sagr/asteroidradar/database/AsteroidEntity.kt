package com.sagr.asteroidradar.database

import androidx.room.Entity
import com.sagr.asteroidradar.Constants

@Entity(tableName = Constants.TABLE_NAME)
data class AsteroidEntity(
    val id: Long, val codename: String, val closeApproachDate: String,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)