package com.example.fit5046a4

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fit5046a4.loginScreen.loginScreen
import com.example.fit5046a4.registerScreen.navigateToRegister
import com.example.fit5046a4.registerScreen.registerScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppRoot() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "LoginDestination"
    ) {
        loginScreen(
            onNavigateToMain = { navController.navigate("MainApp") },
            onNavigateToRegister = {navController.navigateToRegister()}
        )
        registerScreen(
            onNavigateToMain = { navController.navigate("MainApp") },
            onNavigateToLogin = {navController.navigateUp()}
        )
        composable("MainApp") {
            BottomNavigationBarAndTopBar()  // Your existing main app
        }
    }
}

