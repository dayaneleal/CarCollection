package com.example.carcollection.ui.screens.login

sealed class LoginUiEvent {
    data class ShowError(val message: String) : LoginUiEvent()
    object CodeSent : LoginUiEvent()
    object NavigateToMain : LoginUiEvent()
}
