package com.example.carcollection.ui.screens.main

import com.example.carcollection.domain.CarDetails

data class CarsUiState(
    val carDetails: List<CarDetails> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)