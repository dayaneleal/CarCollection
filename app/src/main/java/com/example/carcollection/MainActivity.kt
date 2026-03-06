package com.example.carcollection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carcollection.ui.screens.login.LoginScreen
import com.example.carcollection.ui.screens.main.MainScreen
import com.example.carcollection.ui.theme.CarCollectionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarCollectionTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "login",
                ) {
                    composable("login") {
                        LoginScreen(
                            onNavigate = { navController.navigate("main") }
                        )
                    }
                    composable("main") {
                        MainScreen()
                    }
                }
            }
        }
    }
}
