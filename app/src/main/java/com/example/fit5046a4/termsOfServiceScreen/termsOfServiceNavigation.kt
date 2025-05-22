package com.example.fit5046a4.termsOfServiceScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.termsScreen(navController: NavController) {
    composable("TermsOfService") {
        TermsOfServiceScreen(
            onBack = { navController.popBackStack() }
        )
    }
}

fun NavController.navigateToTerms() {
    navigate("TermsOfService")
}
