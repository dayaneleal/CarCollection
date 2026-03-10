package com.example.carcollection.data.service

import com.example.carcollection.domain.CarDetails
import com.example.carcollection.domain.Car
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ApiService {
    @GET("car")
    suspend fun getCars(): List<CarDetails>

    @GET("car/{id}")
    suspend fun getCarById(@Path("id") id: String): Car

    @PATCH("car/{id}")
    suspend fun updateCar(@Path("id") id: String, @Body car: CarDetails): Car
}
