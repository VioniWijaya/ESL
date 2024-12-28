package com.example.esl.ui.component


import CancelScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
import com.example.esl.models.network.RentalFavoriteViewModel
import com.example.esl.models.network.RescheduleViewModel
import com.example.esl.models.network.StatusViewModel
import com.example.esl.models.network.UserService
import com.example.esl.ui.Home
import com.example.esl.ui.LoginScreen
import com.example.esl.ui.ProfileScreen
import com.example.esl.ui.RiwayatScreen
import com.example.esl.ui.screen.FavoritScreen
import com.example.esl.ui.screen.ProblemScreen
import com.example.esl.ui.screen.RescheduleScreen
//import com.example.esl.viewmodel.PenyewaanViewModel
import com.example.esl.viewmodel.PropertyViewModel
import com.example.esl.viewmodel.UlasanViewModel

import com.example.esl.ui.screen.StatusScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
    object Status : Screen("status")
    object Reschedule : Screen("reschedule/{id_penyewaan}") {
        fun createRoute(id_penyewaan: Int) = "reschedule/$id_penyewaan"
    }
    object Report : Screen("report/{id_penyewaan}") {
        fun createRoute(id_penyewaan: Int) = "report/$id_penyewaan"
    }
    object Favorite: Screen("favorite")

    object Cancel : Screen("cancel/{id_penyewaan}") {
        fun createRoute(id_penyewaan: Int) = "cancel/$id_penyewaan"
    }

}

@Composable
fun AppNavigation(navController: NavHostController) {

    val propertyViewModel = viewModel<PropertyViewModel>()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        // Halaman Login
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                context = context,
                onRegisterClick = { navController.navigate(Screen.Register.route) },

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
                statusList = rentalStatusList
            )
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
            RiwayatScreen(navController, context = context)

        }

        // Di dalam NavHost:
        composable(
            route = Screen.Cancel.route,
            arguments = listOf(navArgument("id_penyewaan") { type = NavType.IntType })
        ) { backStackEntry ->
            val id_penyewaan = backStackEntry.arguments?.getInt("id_penyewaan") ?: 0
            CancelScreen(
                id_penyewaan = id_penyewaan,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        //Halaman favorit
        composable(Screen.Favorite.route) {
            val favoriteViewModel: RentalFavoriteViewModel = viewModel()
            val favoriteList = favoriteViewModel.favoriteData.collectAsState(initial = emptyList()).value

            FavoritScreen(
                navController = navController,
                context = context
            )
        }

        // Halaman Report Problem
        composable(
            route = Screen.Report.route,
            arguments = listOf(navArgument("id_penyewaan") { type = NavType.IntType })
        ) { backStackEntry ->
            val id_penyewaan = backStackEntry.arguments?.getInt("id_penyewaan") ?: 0

            ProblemScreen(
                id_penyewaan = id_penyewaan,
                onNavigateBack = { navController.popBackStack() },
                context = context,
                onReportSubmitted = {
                    navController.navigate(Screen.Status.route) {
                        popUpTo(Screen.Report.route) { inclusive = true }
                    }
                }
            )
        }

        // Halaman Reschedule
        composable(
            route = Screen.Reschedule.route,
            arguments = listOf(navArgument("id_penyewaan") { type = NavType.IntType })
        ) { backStackEntry ->
            val id_penyewaan = backStackEntry.arguments?.getInt("id_penyewaan")
            val rescheduleViewModel: RescheduleViewModel = viewModel()

            LaunchedEffect(id_penyewaan) {
                id_penyewaan?.let {
                    rescheduleViewModel.loadRescheduleDetails(it)
                }
            }

            RescheduleScreen(
                id_penyewaan = id_penyewaan ?: 0,
                viewModel = rescheduleViewModel,
                onNavigateBack = { navController.popBackStack() },
                onRescheduleSuccess = {
                    navController.navigate(Screen.Status.route) {
                        popUpTo(Screen.Reschedule.route) { inclusive = true }
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


        // Halaman Ulasan
        composable(
            route = Screen.Ulasan.route,
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
            val ulasanViewModel: UlasanViewModel = viewModel()

//            UlasanPage(
//                onSubmit = { ulasanRequest ->
//                    // Update ulasanRequest untuk memasukkan orderId
//                    val updatedRequest = ulasanRequest.copy(
//                        id_penyewaan = orderId
//                    )
//                    ulasanViewModel.createUlasan(updatedRequest)
//                    navController.popBackStack()
//                },
//                initialRating = 0,
//                initialUlasan = ""
//            )
        }
    }
}