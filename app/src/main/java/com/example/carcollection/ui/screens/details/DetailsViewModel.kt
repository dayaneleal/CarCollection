package com.example.carcollection.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carcollection.data.service.RetrofitClient
import com.example.carcollection.data.service.SafeResult
import com.example.carcollection.data.service.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<DetailsUiEvent>()
    val events = _events.receiveAsFlow()

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
                    delay(400)
                    _uiState.update { it.copy(isMapReady = true) }
                }
                is SafeResult.Error -> {
                    _uiState.update { it.copy(
                        carDetails = null,
                        errorMessage = result.message,
                        isLoading = false
                    ) }
                }
            }
        }
    }

    fun showDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun hideDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun deleteCar(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, showDeleteDialog = false) }
            val result = withContext(Dispatchers.IO) {
                safeApiCall { RetrofitClient.apiService.removeCar(id) }
            }

            when(result) {
                is SafeResult.Success -> {
                    onSuccess()
                }
                is SafeResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(DetailsUiEvent.ShowError(result.message))
                }
            }
        }
    }
}
