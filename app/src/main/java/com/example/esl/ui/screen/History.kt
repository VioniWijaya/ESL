package com.example.esl.ui

import android.content.Context
import android.content.SharedPreferences
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esl.models.network.RentalHistory
import com.example.esl.models.network.RentalViewModel
import com.example.esl.ui.component.BottomNavBar
import com.example.esl.ui.component.Screen
import com.example.esl.ui.component.TopButtonBar
import kotlinx.coroutines.launch

@Composable
fun RiwayatScreen(navController: NavController, context: Context) {
    val viewModel: RentalViewModel = viewModel()
    val rentalData by viewModel.rentalData.collectAsState()

    // Mengambil token dari SharedPreferences dan memuat data rental
    LaunchedEffect(Unit) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null) // Konsistensi dengan auth_token

        if (token != null) {
            viewModel.loadRentals("Bearer $token") // Memanggil fungsi loadRentals
        } else {
            // Tangani token yang tidak ada (misalnya navigasi ke layar login)
            navController.navigate(Screen.Login.route)
        }
    }

    // Layout utama
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

                TopButtonBar(navController)

                // Tampilkan daftar rental
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
                        navController.navigate("ulasan_page/${rental.id_penyewaan}")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007B7F))
                ) {
                    Text("Beri Ulasan")
                }
            }
        }
    }
}