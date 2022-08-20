package com.sagr.asteroidradar.api

import com.sagr.asteroidradar.PictureOfDay
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query



interface AsteroidApiService {

    @GET("neo/rest/v1/feed")
    suspend fun getProperties(@Query("api_key") key: String): Response<String>

    @GET("planetary/apod")
    suspend fun getPicOfDay(@Query("api_key") key: String): PictureOfDay
}


