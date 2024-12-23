package com.example.esl.ui.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.esl.models.local.entities.Property
import com.example.esl.ui.component.BottomNavBar
import com.example.esl.ui.component.Screen
import com.example.esl.ui.theme.BarColor
import com.example.esl.viewmodel.PropertyViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyListScreen(
    modifier: Modifier = Modifier,
    viewModel: PropertyViewModel,
  navController: NavController,
    onPropertyClick: (Int) -> Unit // Callback untuk navigasi ke halaman detail
) {
    val propertyList by viewModel.propertyList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllProperties() // Pastikan ada fungsi untuk mendapatkan semua properti
    }
    Scaffold(
        topBar = {
            TopAppBar(

                title = { Text("Search Property") },
                navigationIcon = {
                    IconButton(onClick = {  }) { // Update onClick handler
                        Icon(Icons.Default.ArrowBack, "Back")
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
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2ED4D8))
                .padding(paddingValues)
        )
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "Error terjadi.",
                    modifier = Modifier.fillMaxSize(),
                    style = MaterialTheme.typography.labelSmall
                )
            }

            propertyList != null -> {
                LazyColumn {
                    items(propertyList!!) { property ->
                        PropertyCard(
                            property = property,
                            onClick = { onPropertyClick(property.id_properti) })
                    }
                }
            }

            else -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Tidak ada properti tersedia saat ini.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


@Composable
fun PropertyCard(
    property: Property,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
//            property.foto_properti?.let { url ->
//                Image(
//                    painter = rememberImagePainter(url),
//                    contentDescription = "Foto Properti",
//                    modifier = Modifier
//                        .size(100.dp)
//                        .clip(RoundedCornerShape(8.dp))
//                )
//            }
//            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = property.nama_properti, style = MaterialTheme.typography.titleMedium)
                Text(text = "Harga: Rp${property.hargaSewa} / hari", style = MaterialTheme.typography.labelSmall)
                Text(text = "Lokasi: ${property.lokasi}", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

//@Composable
//fun RegisterScreen(navController: NavController) {
//    Register(
//        onRegisterSuccess = {
//            navController.navigate(Screen.Searching.route) {
//                popUpTo(Screen.Register.route) { inclusive = true } // Hapus halaman register dari stack
//            }
//        }
//    )

