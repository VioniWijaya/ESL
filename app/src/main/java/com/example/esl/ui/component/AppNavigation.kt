package com.example.esl.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.esl.ui.LoginScreen
import com.example.esl.ui.screen.DaftarPenyewaanPage
import com.example.esl.ui.screen.DetailProperty
//import com.example.esl.ui.screen.Home
import com.example.esl.ui.screen.PemesananScreen
import com.example.esl.ui.screen.PropertyListScreen
import com.example.esl.ui.screen.Register
import com.example.esl.ui.screen.UlasanPage
//import com.example.esl.ui.screen.RegisterScreen
import androidx.navigation.NavHostController
import com.example.esl.ui.Home
import com.example.esl.viewmodel.PropertyViewModel
import com.example.esl.viewmodel.UlasanViewModel
import com.example.esl.ui.screen.ProfileScreen
import com.example.esl.ui.screen.Register
import com.example.esl.ui.screen.RiwayatScreen
import com.example.esl.ui.screen.StatusScreen


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")

    object Searching : Screen("searching")
    object DetailProperty : Screen("detail/{propertyId}") {
        fun createRoute(propertyId: Int) = "detail/$propertyId"
    }
    object OrderPage : Screen("order/{propertyId}") {
        fun createRoute(propertyId: Int) = "order/$propertyId"
    }
    object DaftarPenyewaan : Screen("daftar_penyewaan")
    object Ulasan : Screen("ulasan/{orderId}") {
        fun createRoute(orderId: Int): String = "ulasan/$orderId"
    }

    object History : Screen("history")
    object Profile : Screen("profile")
    object Status : Screen("status")
}

@Composable
fun AppNavigation(navController: NavHostController) {
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
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onRegisterSuccess = {
                    println("Navigating to Searching Screen")
                    // Setelah register berhasil, navigasi ke halaman property list
                    navController.navigate(Screen.Login.route) {
                        // Hapus semua screen sebelumnya dari back stack
//                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }



        // Halaman Home
        composable(Screen.Home.route) {
            Home(navController)
        }

        composable(Screen.Status.route) {
            StatusScreen(navController)
        }

        composable(Screen.Searching.route) {
            PropertyListScreen(
                modifier = Modifier,
                viewModel = propertyViewModel,
                navController = navController,
                onPropertyClick = { propertyId ->
                    navController.navigate(Screen.DetailProperty.createRoute(propertyId))
                }
            )
        }


        composable(Screen.History.route) {
            RiwayatScreen(navController)

        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
        composable(Screen.Searching.route) {
            PropertyListScreen(
                modifier = Modifier, // Sesuai dengan parameter pertama
                viewModel = propertyViewModel, // ViewModel yang sudah didefinisikan sebelumnya
                navController = navController, // NavController untuk navigasi
                onPropertyClick = { propertyId ->
                    navController.navigate(Screen.DetailProperty.createRoute(propertyId))
                }
            )

        }

        composable(Screen.History.route) {
            RiwayatScreen(navController)
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
                },
                onRentClick = {
                    navController.navigate(Screen.OrderPage.createRoute(propertyId))
                }
            )
        }

        // Halaman Pesanan
        composable(
            route = Screen.OrderPage.route,
            arguments = listOf(navArgument("propertyId") { type = NavType.IntType })
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getInt("propertyId") ?: 0
            PemesananScreen(
                modifier = Modifier,
                propertyId = propertyId,
                onOrderSuccess = {
                    navController.navigate(Screen.DaftarPenyewaan.route) {
                        popUpTo(Screen.DetailProperty.route) { inclusive = true }
                    }
                }
            )
        }



        // Halaman Ulasan

// Update the navigation route in AppNavigation
        composable(
            route = Screen.Ulasan.route,
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
            val ulasanViewModel: UlasanViewModel = viewModel()

            UlasanPage(
                viewModel = ulasanViewModel,
                penyewaanId = orderId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSubmit = { ulasanRequest ->
                    ulasanViewModel.createUlasan(ulasanRequest) {
                        navController.popBackStack() // Kembali ke halaman sebelumnya
                    }
                },
                initialRating = 0,
                initialUlasan = ""
            )
        }
    }
}
