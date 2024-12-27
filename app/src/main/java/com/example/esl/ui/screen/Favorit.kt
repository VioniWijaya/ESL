package com.example.esl.ui.screen

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.esl.models.network.RentalFavorite
import com.example.esl.models.network.RentalFavoriteViewModel
import kotlinx.coroutines.launch

@Composable
fun FavoritScreen(navController: NavController, context: Context) {
    val viewModel: RentalFavoriteViewModel = viewModel()
    val favoriteData by viewModel.favoriteData.collectAsState()

    // Mengambil token dari SharedPreferences dan memuat data favorit
    LaunchedEffect(Unit) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null)

        if (token != null) {
            viewModel.loadFavorites("Bearer $token") // Memanggil fungsi loadFavorites
        }
    }

    // Layout utama
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2ED4D8))
                .padding(innerPadding)
        ) {
            Column {
                // Header dengan ikon kembali dan teks "Favorit"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Favorit",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Tampilkan daftar favorit
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(favoriteData) { favorite ->
                        FavoriteCard(
                            favorite = favorite,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteCard(
    favorite: RentalFavorite,
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
                text = "Nama Properti: ${favorite.namaProperti}",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Harga Sewa: Rp${favorite.hargaSewa}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Lokasi: ${favorite.lokasi}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    navController.navigate("order/${favorite.id_properti}") // Navigasi ke halaman pesan lagi
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007B7F))
            ) {
                Text("Pesan Lagi!")
            }
        }
    }
}
