package com.example.esl.ui.screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.esl.R
import com.example.esl.models.local.entities.Property
import com.example.esl.ui.theme.BackgroundColor
import com.example.esl.ui.theme.BarColor
import com.example.esl.ui.theme.ButtonColors
import com.example.esl.ui.theme.ESLTheme
import com.example.esl.viewmodel.PropertyViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat

// Data class untuk gambar kendaraan
data class VehicleImage(
    val resourceId: Int,
    val description: String
)

data class Review(
    val userName: String,
    val rating: Float,
    val comment: String,
    val date: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProperty(
    modifier: Modifier = Modifier,
    viewModel: PropertyViewModel,
    propertyId: Int,
    onNavigateBack: () -> Unit,
    onRentClick: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }
    val property by viewModel.propertyDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(propertyId) {
        Log.d("DetailProperty", "Loading property with id: $propertyId")
        viewModel.getPropertyDetail(propertyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Properti") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else LocalContentColor.current
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BarColor, // Warna biru
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar {
                OutlinedButton(
                    onClick = onRentClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50), // Warna hijau untuk tombol
                        contentColor = Color.White
                    )
                ) {
                    Text("Sewa Sekarang")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = errorMessage ?: "Terjadi kesalahan",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(
                            onClick = { viewModel.getPropertyDetail(propertyId) },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Coba Lagi")
                        }
                    }
                }
                property != null -> {
                    DetailContent(property = property!!)
                }
                else -> {
                    Text(
                        "Data tidak ditemukan.",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun DetailContent(property: Property) {

    val reviews = remember {
        listOf(
            Review(
                "John Doe",
                4.5f,
                "Mobil sangat bersih dan nyaman. Pemilik ramah.",
                "20 Nov 2024"
            ),
            Review(
                "Jane Smith",
                5f,
                "Pelayanan sangat baik, mobil sesuai ekspektasi",
                "19 Nov 2024"
            ),
            Review("Mike Johnson", 4f, "Pengalaman menyewa yang menyenangkan", "18 Nov 2024")
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF5F5F5))
    ) {
        // Gambar Properti
        Image(
            painter = rememberAsyncImagePainter(property.foto_properti),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        // Informasi Properti
        Column(modifier = Modifier.padding(16.dp)) {
            Text(property.nama_properti, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Harga: Rp ${property.hargaSewa} / hari",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF388E3C)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            // Deskripsi Pemilik
            Text("Nama Pemilik", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(property.pemilik)

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            // Deskripsi Properti
            Text("Deskripsi", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(property.deskripsi)

            Spacer(modifier = Modifier.height(16.dp))
            Divider()

            // Reviews Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Ulasan (${reviews.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                TextButton(onClick = { /* Handle see all reviews */ }) {
                    Text("Lihat Semua")
                }
            }

            // Review Cards
            reviews.take(3).forEach { review ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                review.userName,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                review.date,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                fontSize = 12.sp
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            repeat(5) { index ->
                                Icon(
                                    Icons.Default.Star,
                                    "Star",
                                    modifier = Modifier.size(16.dp),
                                    tint = if (index < review.rating) Color(0xFFFFC107)
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                            }
                        }
                        Text(
                            review.comment,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}



