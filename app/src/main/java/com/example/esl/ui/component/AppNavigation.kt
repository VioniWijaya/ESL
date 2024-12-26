package com.example.esl.ui.component

import ProfileScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.esl.ui.LoginScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.esl.ui.screen.DaftarPenyewaanPage
import com.example.esl.ui.screen.DetailProperty
//import com.example.esl.ui.screen.Home
import com.example.esl.ui.screen.PemesananScreen
import com.example.esl.ui.screen.PropertyListScreen
import com.example.esl.ui.screen.Register
import com.example.esl.ui.screen.UlasanPage
//import com.example.esl.ui.screen.RegisterScreen
import androidx.navigation.NavHostController

import com.example.esl.models.network.RescheduleViewModel
import com.example.esl.models.network.StatusViewModel
import com.example.esl.ui.Home
import com.example.esl.ui.screen.RescheduleScreen
//import com.example.esl.viewmodel.PenyewaanViewModel
import com.example.esl.viewmodel.PropertyViewModel
import com.example.esl.viewmodel.UlasanViewModel


import com.example.esl.ui.screen.RiwayatScreen
import com.example.esl.ui.screen.StatusScreen
import com.example.ui.screen.ProfileScreen


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
    object Cancellation : Screen("cancellation/{rentalId}") {
        fun createRoute(rentalId: String) = "cancellation/$rentalId"
    }
    object History : Screen("history")
    object Profile : Screen("profile")
    object Status : Screen("status")
    object Order : Screen("order/{id_penyewaan}") {
        fun createRoute(id_penyewaan: String) = "order/$id_penyewaan"
    }

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
                onRegisterSuccess = {
                    println("Navigating back to Login Screen")
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.popBackStack(Screen.Login.route, inclusive = false)
                }
            )
        }



        // Halaman Home
        composable(Screen.Home.route) {
            Home(navController)
        }

        composable(Screen.Status.route) {
            val statusViewModel: StatusViewModel = viewModel()
            val rentalStatusList = statusViewModel.statusData.collectAsState(initial = emptyList()).value

            StatusScreen(
                navController = navController,
                context = context,
                statusList = rentalStatusList // This should now work
            )
        }


        composable(Screen.Searching.route) {
//            PropertyListScreen(
//                modifier = Modifier,
//                viewModel = propertyViewModel,
//                onPropertyClick = { propertyId ->
//                    navController.navigate(Screen.DetailProperty.createRoute(propertyId))
//                }
//            )
        }


        composable(Screen.History.route) {
            RiwayatScreen(navController)

        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController, context = context)
        }

        // Halaman Reschedule
        composable(
            route = Screen.Order.route,
            arguments = listOf(navArgument("id_penyewaan") { type = NavType.StringType })
        ) { backStackEntry ->
            val id_penyewaan = backStackEntry.arguments?.getString("id_penyewaan") ?: ""
            val rescheduleViewModel: RescheduleViewModel = viewModel()

            LaunchedEffect(Unit) {
                rescheduleViewModel.loadRescheduleDetails(id_penyewaan)
            }

            RescheduleScreen(
                id_penyewaan = id_penyewaan,
                viewModel = rescheduleViewModel,
                onNavigateBack = { navController.popBackStack() },
                onRescheduleSuccess = {
                    navController.navigate(Screen.Status.route) {
                        popUpTo(Screen.Order.route) { inclusive = true }
                    }
                }
            )
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


//        composable(Screen.DaftarPenyewaan.route) {
//            val viewModel: PenyewaanViewModel = viewModel()
//
//            // Ambil userId, misalnya dari sesi atau data lokal
//            val userId = 1 // Ganti dengan userId yang sesuai
//
//            // Panggil fungsi fetch data di ViewModel
//            LaunchedEffect(Unit) {
//                viewModel.fetchPenyewaanByUser(userId)
//            }
//
//            DaftarPenyewaanPage(
//                modifier = Modifier,
//                penyewaanList = viewModel.penyewaanList.value,
//                onUlasanClick = { orderId ->
//                    navController.navigate(Screen.Ulasan.createRoute(orderId))
//                },
//                onCancelClick = { orderId ->
//                    // Handle cancel logic
//                }
//            )
//        }
        // Halaman Ulasan
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
//        composable(
//            route = Screen.Cancellation.route,
//            arguments = listOf(navArgument("rentalId") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val rentalId = backStackEntry.arguments?.getString("rentalId") ?: return@composable
//
//            CancellationScreen(
//                apiService = apiService,  // Gunakan parameter yang diteruskan
//                rentalId = rentalId,
//                onNavigateBack = {
//                    navController.navigate(Screen.History.route) {
//                        popUpTo(Screen.History.route) { inclusive = true }
//                    }
//                }
//            )
//        }

    }
}