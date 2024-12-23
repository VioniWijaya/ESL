package com.example.esl.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.esl.ui.Home
import com.example.esl.viewmodel.PenyewaanViewModel
import com.example.esl.viewmodel.PropertyViewModel
import com.example.esl.viewmodel.UlasanViewModel
import com.example.esl.ui.screen.ProfileScreen
import com.example.esl.ui.screen.Register
import com.example.esl.ui.screen.RiwayatScreen
import com.example.esl.ui.screen.SearchScreen

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
        fun createRoute(penyewaanId: Int) = "ulasan/$penyewaanId"
    }

    object History : Screen("history")
    object Profile : Screen("profile")
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
        composable(Screen.Searching.route) {
            PropertyListScreen(navController)
        }

        composable(Screen.History.route) {
            RiwayatScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
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

        // Halaman Daftar Pesanan Penyewaan
        // Halaman Daftar Penyewaan Penyewaan
        composable(Screen.DaftarPenyewaan.route) {
            val viewModel: PenyewaanViewModel = viewModel()

            // Ambil userId, misalnya dari sesi atau data lokal
            val userId = 1 // Ganti dengan userId yang sesuai

            // Panggil fungsi fetch data di ViewModel
            LaunchedEffect(Unit) {
                viewModel.fetchPenyewaanByUser(userId)
            }

            DaftarPenyewaanPage(
                modifier = Modifier,
                penyewaanList = viewModel.penyewaanList.value,
                onUlasanClick = { orderId ->
                    navController.navigate(Screen.Ulasan.createRoute(orderId))
                },
                onCancelClick = { orderId ->
                    // Handle cancel logic
                }
            )
        }
        // Halaman Ulasan
        composable(
            route = Screen.Ulasan.route,
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
            val ulasanViewModel: UlasanViewModel = viewModel()

            UlasanPage(
                onSubmit = { ulasanRequest ->
                    // Update ulasanRequest untuk memasukkan orderId
                    val updatedRequest = ulasanRequest.copy(
                        id_penyewaan = orderId
                    )
                    ulasanViewModel.createUlasan(updatedRequest)
                    navController.popBackStack()
                },
                initialRating = 0,
                initialUlasan = ""
            )
        }
    }
}
