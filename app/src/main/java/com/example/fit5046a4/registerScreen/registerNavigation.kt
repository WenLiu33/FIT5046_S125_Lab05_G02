package com.example.fit5046a4.registerScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.registerScreen(
    onNavigateToMain: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToTerms: () -> Unit
) {
    composable("RegisterDestination") {
        RegisterScreen(
            onNavigateToMain = onNavigateToMain,
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToTerms = onNavigateToTerms
        )
    }
}


fun NavController.navigateToRegister() {
    navigate("RegisterDestination")
}