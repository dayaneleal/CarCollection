package com.example.carcollection.ui.screens.details

sealed class DetailsUiEvent {
    data class ShowError(val message: String) : DetailsUiEvent()
}
