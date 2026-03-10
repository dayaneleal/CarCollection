package com.example.carcollection.ui.screens.main

import com.example.carcollection.domain.CarDetails

data class CarsUiState(
    val carDetails: List<CarDetails> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)