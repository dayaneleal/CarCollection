package com.example.carcollection.ui.screens.main

import com.example.carcollection.domain.Car

data class CarsUiState(
    val cars: List<Car> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)