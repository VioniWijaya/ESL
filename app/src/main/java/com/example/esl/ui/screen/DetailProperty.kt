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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DetailProperty(modifier: Modifier = Modifier, viewModel: PropertyViewModel, propertyId: Int,  onNavigateBack: () -> Unit,  onRentClick: () -> Unit) {
    var isFavorite by remember { mutableStateOf(false) }
    val property by viewModel.propertyDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    // Sample reviews

    LaunchedEffect(propertyId) {
        Log.d("DetailProperty", "Loading property with id: $propertyId")
        viewModel.getPropertyDetail(propertyId)
    }


    Scaffold(
        topBar = {
            TopAppBar(

                title = { Text("Detail Property") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { // Update onClick handler
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle share */ }) {
                        Icon(Icons.Default.Share, "Share")
                    }
                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        Icon(
                            if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            "Favorite",
                            tint = if (isFavorite) Color.Red else LocalContentColor.current
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BarColor, // Warna biru
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
//
                    OutlinedButton(
                        onClick = onRentClick,
                        modifier = Modifier
                            .weight(1f).padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonColors,
                            contentColor = Color.Black // Warna text
                        )
                    ) {
                        Text("Sewa Sekarang")
                    }
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
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
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
                            text = errorMessage ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(
                            onClick = { viewModel.getPropertyDetail(propertyId) },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Retry")
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
    // List gambar kendaraan
    val vehicleImages = remember {
        listOf(
            VehicleImage(R.drawable.toyota_avanza, "Tampak Depan"),
            VehicleImage(R.drawable.toyota_avanza1, "Tampak Belakang"),
        )
    }
            Column(
                modifier = Modifier
//                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .background(BackgroundColor)
            ) {
                // Image Carousel
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    val pagerState = rememberPagerState(pageCount = { vehicleImages.size })
                    val coroutineScope = rememberCoroutineScope()

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        Image(
                            painter = painterResource(id = vehicleImages[page].resourceId),
                            contentDescription = vehicleImages[page].description,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    // Image counter
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "${pagerState.currentPage + 1}/${vehicleImages.size}",
                            color = Color.White
                        )
                    }
                    // Dots Indicator
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(vehicleImages.size) { iteration ->
                            val color = if (pagerState.currentPage == iteration)
                                Color.White else Color.White.copy(alpha = 0.5f)
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(8.dp)
                                    .clickable {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(iteration)
                                        }
                                    }
                            )
                        }
                    }
                }

                // Thumbnail Preview
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(vehicleImages) { image ->
                        Image(
                            painter = painterResource(id = image.resourceId),
                            contentDescription = image.description,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    // Handle thumbnail click
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Product Info Section
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Vehicle Name and Price
                    Text(
                        property.nama_properti,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID"))
                            .format(property.hargaSewa) + " / hari",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    // Vehicle Specs
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Build, "Transmission")
                                Text("Matic", fontSize = 14.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Person, "Seats")
                                Text("7 Kursi", fontSize = 14.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Create, "Fuel")
                                Text("Bensin", fontSize = 14.sp)
                            }
                        }
                    }

                    Divider()

                    // Shop Info
                    Row(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                    AsyncImage(
//                        model = "https://example.com/shop-image.jpg",
//                        contentDescription = "Shop Image",
//                        modifier = Modifier
//                            .size(48.dp)
//                            .clip(RoundedCornerShape(24.dp))
//                    )
                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(1f)
                        ) {
                            Text(
                                property.pemilik,
                                fontWeight = FontWeight.Medium
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    "Rating",
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    "4.8 • Member sejak 2022",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }

                    Divider()

                    // Vehicle Description
                    Text(
                        "Tentang Kendaraan",
                        modifier = Modifier.padding(vertical = 16.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        property.deskripsi
                            .trimIndent()
                    )

                    Divider(modifier = Modifier.padding(vertical = 16.dp))

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

//@Preview(showBackground = true)
//@Composable
//fun DetailPropertyPreview() {
//    // Berikan instance ViewModel dummy jika dibutuhkan
//    val dummyViewModel = PropertyViewModel() // Pastikan untuk membuat ViewModel dengan dummy data
//    ESLTheme { // Gunakan tema yang sesuai dengan proyek Anda
//        DetailProperty(
//            viewModel = dummyViewModel,
//            propertyId = 1 // Gunakan ID properti dummy
//        )
//    }
//}
