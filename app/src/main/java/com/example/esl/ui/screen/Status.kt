package com.example.esl.ui.screen

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esl.models.network.RentalStatus
import com.example.esl.models.network.StatusViewModel
import com.example.esl.ui.component.BottomNavBar
import com.example.esl.ui.component.Screen
import com.example.esl.ui.component.TopButtonBar

@Composable
fun StatusScreen(navController: NavController, context: Context, statusList : List<RentalStatus>) {
    val viewModel: StatusViewModel = viewModel()
    val statusData by viewModel.statusData.collectAsState()

    LaunchedEffect(Unit) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null)
        if (token != null) {
            viewModel.loadStatus("Bearer $token")
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2ED4D8))
                .padding(innerPadding)
        ) {
            Column {
                Text(
                    text = "Status Penyewaan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                TopButtonBar(navController)

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(statusData) { rental ->
                        StatusCard(status = rental, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusCard(status: RentalStatus, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Status: ${status.status}",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Nama Properti: ${status.nama_properti}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Tanggal Sewa: ${status.tanggalMulai}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Tanggal Selesai: ${status.tanggalAkhir}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "idPenyewaan: ${status.idPenyewaan}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* TODO: Add Cancel Functionality */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = "Cancel", color = Color.White)
                }

                Button(
                    onClick = { navController.navigate(Screen.Order.route + "/${status.idPenyewaan}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
                ) {
                    Text(text = "Reschedule", color = Color.Black)
                }

                Button(
                    onClick = { /* TODO: Add Report Functionality */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text(text = "Lapor", color = Color.White)
                }
            }
        }
    }
}