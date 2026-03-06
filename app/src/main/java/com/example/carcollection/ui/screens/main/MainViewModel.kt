package com.example.carcollection.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carcollection.data.service.RetrofitClient
import com.example.carcollection.data.service.SafeResult
import com.example.carcollection.data.service.safeApiCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(CarsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchCars()
    }

    fun fetchCars() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = safeApiCall { RetrofitClient.apiService.getCars() }

            when(result) {
                is SafeResult.Success -> {
                    _uiState.update { it.copy(
                        cars = result.data,
                        isLoading = false
                    )}
                }
                is SafeResult.Error -> {
                    _uiState.update { it.copy(
                        errorMessage = "Ops! Algo deu errado: ${result.message}",
                        isLoading = false
                    )}
                }
            }
        }
    }
}
