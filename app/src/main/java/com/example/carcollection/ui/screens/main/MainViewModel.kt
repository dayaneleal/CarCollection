package com.example.carcollection.ui.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carcollection.data.service.RetrofitClient
import com.example.carcollection.data.service.SafeResult
import com.example.carcollection.data.service.safeApiCall
import com.example.carcollection.domain.CarDetails
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel: ViewModel()  {

    private val _uiState = MutableStateFlow(CarsUiState())
    val uiState = _uiState.asStateFlow()

    private val storageRef = FirebaseStorage.getInstance().reference
    private val imagesRef = storageRef.child("images")

    init {
        fetchCars()
    }

    fun fetchCars() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = safeApiCall { RetrofitClient.apiService.getCars() }

            when(result) {
                is SafeResult.Success -> {
                    val cars = result.data
                    _uiState.update { it.copy(carDetails = cars, isLoading = false) }
                    
                    updateCarsWithFirebaseImages(cars)
                }
                is SafeResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.message, isLoading = false) }
                }
            }
        }
    }

    private fun updateCarsWithFirebaseImages(cars: List<CarDetails>) {
        cars.forEach { car ->
            imagesRef.child("${car.id}.jpg").downloadUrl
                .addOnSuccessListener { uri ->
                    val firebaseImageUrl = uri.toString()
                    
                    if (car.imageUrl != firebaseImageUrl) {
                        patchCarImage(car.id, car.copy(imageUrl = firebaseImageUrl))
                    }
                }
                .addOnFailureListener {
                    Log.d("MainViewModel", "Imagem não encontrada no Firebase para o ID ${car.id}")
                }
        }
    }

    private fun patchCarImage(id: String, updatedCar: CarDetails) {
        viewModelScope.launch {
            val result = safeApiCall { RetrofitClient.apiService.updateCar(id, updatedCar) }
            
            if (result is SafeResult.Success) {
                _uiState.update { state ->
                    val updatedList = state.carDetails.map {
                        if (it.id == id) updatedCar else it
                    }
                    state.copy(carDetails = updatedList)
                }
                Log.d("MainViewModel", "API atualizada com sucesso para o carro $id")
            }
        }
    }
}
