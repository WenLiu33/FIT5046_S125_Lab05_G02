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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppRoot() {
    val navController = rememberNavController()

    val isLoggedIn = Firebase.auth.currentUser!=null
    val firstPage = if(isLoggedIn) "MainApp" else "LoginDestination"

    NavHost(
        navController = navController,
        startDestination = firstPage
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
            BottomNavigationBarAndTopBar()
        }
    }
}

