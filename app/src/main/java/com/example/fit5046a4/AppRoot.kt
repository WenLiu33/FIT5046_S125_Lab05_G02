package com.example.fit5046a4

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fit5046a4.bottomNavigationBarAndTopBar.BottomNavigationBarAndTopBar
import com.example.fit5046a4.loginScreen.loginScreen
import com.example.fit5046a4.registerScreen.navigateToRegister
import com.example.fit5046a4.registerScreen.registerScreen
import com.example.fit5046a4.termsOfServiceScreen.navigateToTerms
import com.example.fit5046a4.termsOfServiceScreen.termsScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppRoot() {
    // Tracks the user's login status using Firebase Auth
    var isLoggedIn by remember { mutableStateOf(Firebase.auth.currentUser != null) }

    val navController = rememberNavController()

    // Set the start destination based on login status
    val startDestination = if (isLoggedIn) "MainApp" else "LoginDestination"

    NavHost(navController = navController, startDestination = startDestination) {
        // Login screen with navigation to MainApp or Register
        loginScreen(
            onNavigateToMain = {
                isLoggedIn = true
                navController.navigate("MainApp") {
                    popUpTo(0) { inclusive = true }
                }
            },
            onNavigateToRegister = { navController.navigateToRegister() }
        )

        // Register screen with navigation to MainApp, Login, or Terms
        registerScreen(
            onNavigateToMain = {
                isLoggedIn = true
                navController.navigate("MainApp") {
                    popUpTo(0) { inclusive = true }
                }
            },
            onNavigateToLogin = { navController.navigateUp() },
            onNavigateToTerms = { navController.navigateToTerms() }
        )

        // Terms of Service screen
        termsScreen(navController)

        // MainApp screen with logout functionality
        composable("MainApp") {
            BottomNavigationBarAndTopBar(
                onLogout = {
                    Firebase.auth.signOut()
                    isLoggedIn = false
                }
            )
        }
    }
}

