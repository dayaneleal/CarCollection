package com.example.carcollection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carcollection.ui.auth.AuthViewModel
import com.example.carcollection.ui.screens.details.DetailsScreen
import com.example.carcollection.ui.screens.login.LoginScreen
import com.example.carcollection.ui.screens.main.MainScreen
import com.example.carcollection.ui.theme.CarCollectorTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel: AuthViewModel by viewModels()
        val startDestination = if (authViewModel.isUserLoggedIn) "main" else "login"

        enableEdgeToEdge()
        setContent {
            CarCollectorTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                ) {
                    composable("login") {
                        LoginScreen(
                            onNavigate = { 
                                navController.navigate("main") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("main") {
                        MainScreen(onNavigate = { id ->
                            navController.navigate("details/$id" )
                        },

                            onLogout = {
                                authViewModel.logout()
                                navController.navigate("login") {
                                    popUpTo("main") { inclusive = true }
                                }
                            })
                    }
                    composable("details/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id") ?: ""
                        DetailsScreen(
                            onBackClick = { navController.popBackStack() },
                            id = id
                        )
                    }
                }
            }
        }
    }
}
