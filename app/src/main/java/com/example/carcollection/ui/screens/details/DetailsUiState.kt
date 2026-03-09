package com.example.carcollection.ui.screens.details

import com.example.carcollection.domain.Car

data class DetailsUiState(
    val car: Car? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
