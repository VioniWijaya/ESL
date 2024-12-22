package com.example.esl.ui.component

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.esl.ui.Home
import com.example.esl.ui.LoginScreen
import com.example.esl.ui.screen.ProfileScreen
import com.example.esl.ui.screen.Register
import com.example.esl.ui.screen.RiwayatScreen
import com.example.esl.ui.screen.SearchScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Search : Screen("search")
    object History : Screen("history")
    object Profile : Screen("profile")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        // Halaman Login
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screen.Home.route) },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        // Halaman Register
        composable(Screen.Register.route) {
            Register(
                onRegisterSuccess = { navController.popBackStack(Screen.Login.route, false) },
                onLoginClick = { navController.navigate(Screen.Login.route) }

            )
        }

        // Halaman Home
        composable(Screen.Home.route) {
            Home(navController)
        }
        composable(Screen.Search.route) {
            SearchScreen(navController)
        }

        composable(Screen.History.route) {
            RiwayatScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

    }
}
