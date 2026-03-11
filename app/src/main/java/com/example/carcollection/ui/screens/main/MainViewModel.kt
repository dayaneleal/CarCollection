package com.example.carcollection.ui.screens.main

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.carcollection.data.database.DatabaseBuilder
import com.example.carcollection.data.database.model.UserLocation
import com.example.carcollection.data.service.RetrofitClient
import com.example.carcollection.data.service.SafeResult
import com.example.carcollection.data.service.safeApiCall
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(CarsUiState())
    val uiState = _uiState.asStateFlow()

    private val storageRef = FirebaseStorage.getInstance().reference
    private val imagesRef = storageRef.child("images")
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private val userLocationDao = DatabaseBuilder.getInstance(application).userLocationDao()

    fun fetchCars() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = safeApiCall { RetrofitClient.apiService.getCars() }

            when (result) {
                is SafeResult.Success -> {
                    val initialCars = result.data
                    
                    val finalCars = coroutineScope {
                        initialCars.map { car ->
                            async {
                                val firebaseUri = getFirebaseImageUrl(car.id)
                                val finalUrl = firebaseUri ?: car.imageUrl
                                val updatedCar = car.copy(imageUrl = finalUrl)

                                if (finalUrl != car.imageUrl) {
                                    viewModelScope.launch {
                                        safeApiCall { RetrofitClient.apiService.updateCar(car.id, updatedCar) }
                                    }
                                }
                                updatedCar
                            }
                        }.awaitAll()
                    }

                    _uiState.update { it.copy(carDetails = finalCars, isLoading = false) }
                }
                is SafeResult.Error -> {
                    _uiState.update { it.copy(
                        errorMessage = result.message,
                        isLoading = false)
                    }
                }
            }
        }
    }

    private suspend fun getFirebaseImageUrl(id: String): String? {
        return try {
            imagesRef.child("$id.jpg").downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }
    @SuppressLint("MissingPermission")
    fun saveUserLocation() {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                location?.let {
                    viewModelScope.launch {
                        userLocationDao.insert(
                            UserLocation(
                                latitude = it.latitude,
                                longitude = it.longitude
                            )
                        )
                        Log.d("MainViewModel", "Localização salva no Room: ${it.latitude}, ${it.longitude}")
                    }
                }
            }
    }
}
