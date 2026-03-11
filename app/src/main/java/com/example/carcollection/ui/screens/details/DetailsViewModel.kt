package com.example.carcollection.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carcollection.data.service.RetrofitClient
import com.example.carcollection.data.service.SafeResult
import com.example.carcollection.data.service.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun fetchCarDetails(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isMapReady = false) }
            
            val result = withContext(Dispatchers.IO) {
                safeApiCall { RetrofitClient.apiService.getCarById(id) }
            }

            when(result) {
                is SafeResult.Success -> {
                    _uiState.update { 
                        it.copy(carDetails = result.data.value, isLoading = false)
                    }
                    delay(800)
                    _uiState.update { it.copy(isMapReady = true) }
                }
                is SafeResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.message, isLoading = false) }
                }
            }
        }
    }
}
