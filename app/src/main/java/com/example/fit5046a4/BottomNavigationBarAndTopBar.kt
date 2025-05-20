package com.example.fit5046a4
//noinspection UsingMaterialAndMaterial3Libraries
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored.Sharp
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.sharp.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fit5046a4.loginScreen.LoginScreen
import com.example.fit5046a4.loginScreen.loginScreen
import com.example.fit5046a4.registerScreen.RegisterScreen
import com.example.fit5046a4.registerScreen.navigateToRegister
import com.example.fit5046a4.reportScreen.Report

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun BottomNavigationBarAndTopBar() {
    val navRoutes = listOf(
        NavRoute("fridge", R.drawable.refrigerator_20 , "Fridge"),
        NavRoute("dashboard", R.drawable.report, "Dashboard"),
        NavRoute("cook", R.drawable.mix_20, "Cook")
    )

    val navController = rememberNavController()

    Scaffold(
        topBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (currentRoute) {
//                            "home" -> "Home"
                            "fridge" -> "My Fridge"
                            "cook" -> "Cook a Meal"
                            "dashboard" -> "Dashboard"
                            "add_ingredient" -> "Add Items"

                            else -> ""
                        },
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate("login"){
                            popUpTo(navController.graph.startDestinationId){
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Log out"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.padding(bottom = 2.dp).height(65.dp),
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                navRoutes.forEach { navRoute ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = navRoute.iconRes),
                                contentDescription = navRoute.label
                            )
                        },
                        label = { Text(navRoute.label) },
                        selected = currentDestination?.route == navRoute.route,
                        onClick = {
                            navController.navigate(navRoute.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("dashboard") { Report() }
            composable("cook") { Cook() }
            //Fridge screen receives navController to allow for internal in-screen navigation.
            //Specifically, the "Add Ingredient" button inside the Fridge screen uses it to navigate to the Add Ingredient screen.
            composable("fridge") { Fridge(navController) }
            composable("add_ingredient") {
                //This route handles navigation to the Add Ingredient screen.
                //It is not part of the bottom navigation bar used for internal flow only, triggered from the Fridge screen.
                AddIngredientScreen(navController)
            }
            composable("login") {
                loginScreen(
                    onNavigateToMain = { navController.navigate("MainApp") },
                    onNavigateToRegister = { navController.navigateToRegister() }
                )
            }
        }
    }
}
