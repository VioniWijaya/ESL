package com.example.esl.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.esl.R
import com.example.esl.ui.component.BottomNavBar

@Composable
fun Home(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        // Box untuk memberikan padding dari Scaffold
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFB2EBF2))
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(Color(0xFF40E0D0))
                    .padding(16.dp)
            ) {

                Text(
                    text = "Selamat Datang!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Mari Eksplor Keindahan\nDanau Singkarak Bersama dengan ESL~",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Bagian Daftar Gambar
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ImageCard(imageResId = R.drawable.danausingkarak, title = "Danau Singkarak")
                    ImageCard(imageResId = R.drawable.tourdesingkarak, title = "Tour de Singkarak")
                    ImageCard(imageResId = R.drawable.danau5, title = "Festival 5 Danau Kabupaten Solok")
                }
            }
        }
    }
}

@Composable
fun ImageCard(imageResId: Int, title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Cyan, shape = RoundedCornerShape(12.dp))
            .padding(8.dp), // Padding di dalam card
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier
                .padding(top = 8.dp)
        )
    }
}
