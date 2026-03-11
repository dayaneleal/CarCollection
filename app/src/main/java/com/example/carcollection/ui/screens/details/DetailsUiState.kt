package com.example.carcollection.ui.screens.details

import com.example.carcollection.domain.CarDetails

data class DetailsUiState(
    val carDetails: CarDetails? = null,
    val isLoading: Boolean = false,
    val isMapReady: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val errorMessage: String? = null
)
