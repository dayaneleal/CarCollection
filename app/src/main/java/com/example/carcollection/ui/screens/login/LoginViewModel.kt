package com.example.carcollection.ui.screens.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

sealed class LoginUiEvent {
    data class ShowError(val message: String) : LoginUiEvent()
    object CodeSent : LoginUiEvent()
}

class LoginViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<LoginUiEvent>()
    val events = _events.receiveAsFlow()

    fun onPhoneNumberChange(newNumber: String) {
        _uiState.update { it.copy(phoneNumber = newNumber) }
    }

    fun onOtpCodeChange(newCode: String) {
            _uiState.update { it.copy(otpCode = newCode) }
    }

    fun sendVerificationCode(activity: Activity) {
        val phone = _uiState.value.phoneNumber
        if (phone.isEmpty()) return

        onStartLoading()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(45L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    onError(e.message ?: "Erro desconhecido")
                }

                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    onCodeSentSuccess(id)
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyCode() {
        val code = _uiState.value.otpCode
        val verificationId = _uiState.value.verificationId
        if (verificationId.isEmpty()) return

        onStartLoading()
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _uiState.update { it.copy(shouldNavigate = true, isLoading = false) }
            } else {
                onError(task.exception?.message ?: "Código inválido")
            }
        }
    }

    private fun onStartLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    private fun onCodeSentSuccess(id: String) {
        _uiState.update { it.copy(verificationId = id, isLoading = false) }
        viewModelScope.launch { _events.send(LoginUiEvent.CodeSent) }
    }

    private fun onError(message: String) {
        _uiState.update { it.copy(isLoading = false) }
        viewModelScope.launch { _events.send(LoginUiEvent.ShowError(message)) }
    }
}
