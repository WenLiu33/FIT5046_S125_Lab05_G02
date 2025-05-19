package com.example.fit5046a4.registerScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.registerScreen(
    onNavigateToMain: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    composable("RegisterDestination") {
        RegisterScreen(
            onNavigateToMain = onNavigateToMain,
            onNavigateToLogin = onNavigateToLogin
        )
    }
}

fun NavController.navigateToRegister() {
    navigate("RegisterDestination")
}