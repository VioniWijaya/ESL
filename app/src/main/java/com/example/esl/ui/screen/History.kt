package com.example.esl.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esl.models.network.RentalHistory
import com.example.esl.models.network.RentalViewModel
import com.example.esl.ui.component.BottomNavBar
import com.example.esl.ui.component.Screen


// Screen Riwayat Penyewaan
@Composable
fun RiwayatScreen(navController: NavController, viewModel: RentalViewModel = viewModel()) {
    val rentalData by viewModel.rentalData.collectAsState()

    // Load data rentals ketika screen muncul
    LaunchedEffect(Unit) {
        Log.d("RiwayatScreen", "LaunchedEffect triggered")
        viewModel.loadRentals()
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
                    text = "Riwayat Penyewaan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(rentalData) { rental ->
                        RentalCard(
                            rental = rental,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RentalCard(
    rental: RentalHistory,
    navController: NavController
) {
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
                text = "Status: ${rental.status}",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Nama Properti: ${rental.nama_properti}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Tanggal Order: ${rental.tanggalOrder}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Pemilik: ${rental.pemilik}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // Add Review Button
            if (rental.status.equals("Selesai", ignoreCase = true)) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        navController.navigate(Screen.Ulasan.createRoute(rental.id_penyewaan))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ED4D8))
                ) {
                    Text("Beri Ulasan")
                }
            }
        }
    }
}