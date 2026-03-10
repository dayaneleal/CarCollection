package com.example.carcollection.ui.screens.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carcollection.data.service.RetrofitClient
import com.example.carcollection.data.service.SafeResult
import com.example.carcollection.data.service.safeApiCall
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val storageRef = FirebaseStorage.getInstance().reference
    private val imagesRef = storageRef.child("images")

    private fun fetchCarImage(id: String) {
        imagesRef.child("$id.jpg").downloadUrl
            .addOnSuccessListener { uri ->
                _uiState.update { state ->
                    if (state.carDetails?.id == id) {
                        state.copy(carDetails = state.carDetails.copy(imageUrl = uri.toString()))
                    } else {
                        state
                    }
                }
            }
            .addOnFailureListener {
                Log.e("DetailsViewModel", "Imagem não encontrada no Firebase para o ID $id. Mantendo a original.")
            }
    }

    fun fetchCarDetails(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = safeApiCall { RetrofitClient.apiService.getCarById(id) }

            when(result) {
                is SafeResult.Success -> {
                    val car = result.data.value
                    _uiState.update { it.copy(carDetails = car, isLoading = false) }
                    
                    fetchCarImage(car.id)
                }
                is SafeResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.message, isLoading = false) }
                }
            }
        }
    }
}
