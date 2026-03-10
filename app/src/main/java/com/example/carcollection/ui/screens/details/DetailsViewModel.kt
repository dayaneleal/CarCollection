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
import kotlinx.coroutines.tasks.await

class DetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val storageRef = FirebaseStorage.getInstance().reference
    private val imagesRef = storageRef.child("images")

    private suspend fun getFirebaseImageUrl(id: String): String? {
        return try {
            imagesRef.child("$id.jpg").downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("DetailsViewModel", "Imagem não encontrada no Firebase para o ID $id: ${e.message}")
            null
        }
    }

    fun fetchCarDetails(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val result = safeApiCall { RetrofitClient.apiService.getCarById(id) }

            when(result) {
                is SafeResult.Success -> {
                    val carDetails = result.data.value

                    val firebaseImageUrl = getFirebaseImageUrl(carDetails.id)
                    
                    val finalCarDetails = if (firebaseImageUrl != null) {
                        carDetails.copy(imageUrl = firebaseImageUrl)
                    } else {
                        carDetails
                    }

                    _uiState.update { 
                        it.copy(carDetails = finalCarDetails, isLoading = false) 
                    }
                }
                is SafeResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.message, isLoading = false) }
                }
            }
        }
    }
}
