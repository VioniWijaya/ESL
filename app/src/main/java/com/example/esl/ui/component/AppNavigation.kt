package com.example.esl.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.esl.ui.Home
import com.example.esl.ui.LoginScreen
import com.example.esl.ui.screen.DetailProperty
import com.example.esl.ui.screen.Home
import com.example.esl.ui.screen.PropertyListScreen
import com.example.esl.ui.screen.Register
//import com.example.esl.ui.screen.RegisterScreen
import com.example.esl.viewmodel.PropertyViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Searching : Screen("searching")
    object DetailProperty : Screen("detail/{propertyId}") {
        fun createRoute(propertyId: Int) = "detail/$propertyId"
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val navController = rememberNavController()
    val propertyViewModel = viewModel<PropertyViewModel>()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        // Halaman Login
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        // Halaman Register
        composable(Screen.Register.route) {
            Register(
                navController = navController,
                onRegisterSuccess = {
                    println("Navigating to Searching Screen")
                    // Setelah register berhasil, navigasi ke halaman property list
                    navController.navigate(Screen.Searching.route) {
                        // Hapus semua screen sebelumnya dari back stack
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Halaman Property List
        composable(Screen.Searching.route) {
            PropertyListScreen(
                modifier = Modifier,
                viewModel = propertyViewModel,
                onPropertyClick = { propertyId ->
                    navController.navigate(Screen.DetailProperty.createRoute(propertyId))
                }
            )
        }

        // Halaman Home
        composable(Screen.Home.route) {
            Home(navController)
        }

        // Halaman Detail Property
        composable(
            route = Screen.DetailProperty.route,
            arguments = listOf(navArgument("propertyId") { type = NavType.IntType })
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getInt("propertyId") ?: 0
            DetailProperty(
                modifier = Modifier,
                viewModel = propertyViewModel,
                propertyId = propertyId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
