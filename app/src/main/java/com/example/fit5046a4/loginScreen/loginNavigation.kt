package com.example.fit5046a4.loginScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.loginScreen(
    onNavigateToMain: () -> Unit,
    onNavigateToRegister: () -> Unit
    ) {
    composable("LoginDestination") {
        LoginScreen(
            onNavigateToMain = onNavigateToMain,
            onNavigateToRegister = onNavigateToRegister
        )
    }
}