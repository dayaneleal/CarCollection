package com.example.carcollection.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    val isUserLoggedIn: Boolean
        get() = FirebaseAuth.getInstance().currentUser != null

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}