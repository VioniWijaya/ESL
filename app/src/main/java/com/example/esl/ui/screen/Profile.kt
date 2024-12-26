package com.example.ui.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esl.models.network.UserViewModel
import com.example.esl.ui.component.BottomNavBar


@Composable
fun ProfileScreen(navController: NavController, context: Context) {
    val viewModel: UserViewModel = viewModel()
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading

    // Load user data from API
    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2ED4D8))
                .padding(innerPadding)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(32.dp))

                    // Username
                    Text(
                        text = user.username,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Edit Profile Button
                    TextButton(
                        onClick = {
                            navController.navigate("edit_profile")
                        },
                        modifier = Modifier.background(Color(0xFFE6EE9C))
                    ) {
                        Text(text = "Edit Profil", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Ubah Password
                    TextButton(onClick = { /* Navigate to Change Password Screen */ }) {
                        Text(
                            text = "Ubah Password",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }

                    Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.fillMaxWidth())

                    // Favorit
                    TextButton(onClick = { /* Navigate to Favorite Screen */ }) {
                        Text(
                            text = "Favorit",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }

                    Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.fillMaxWidth())

                    // Logout
                    TextButton(onClick = { /* Logout functionality */ }) {
                        Text(
                            text = "Logout",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
