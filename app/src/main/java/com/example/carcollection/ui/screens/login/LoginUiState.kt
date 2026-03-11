package com.example.carcollection.ui.screens.login

data class LoginUiState(
    val phoneNumber: String = "",
    val otpCode: String = "",
    val verificationId: String = "",
    val isLoading: Boolean = false
)