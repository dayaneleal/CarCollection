package com.example.carcollection.data.service

import com.example.carcollection.domain.Car
import com.example.carcollection.domain.CarDetails
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("car")
    suspend fun getCars(): List<Car>

    @GET("car/{id}")
    suspend fun getCarById(@Path("id") id: String): CarDetails
}
