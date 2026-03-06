package com.example.carcollection.data.service

import com.example.carcollection.domain.Car
import retrofit2.http.GET

interface ApiService {
    @GET("car")
    suspend fun getCars(): List<Car>
}

