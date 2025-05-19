package com.example.fit5046a4

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.fit5046a4.authScreen.loginScreen
import com.example.fit5046a4.authScreen.navigateToRegister
import com.example.fit5046a4.authScreen.registerScreen

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "LoginDestination"
    ) {
        loginScreen(
            onNavigateToMain = {},
            onNavigateToRegister = {navController.navigateToRegister()}
        )
        registerScreen(
            onNavigateToMain = {},
            onNavigateToLogin = {navController.navigateUp()}
        )
    }
}

