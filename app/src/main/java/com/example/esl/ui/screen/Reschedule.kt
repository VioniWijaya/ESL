package com.example.esl.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow

@Composable
fun OrderScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4DD0E1)) // Background biru muda
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            HeaderSection()

            // Detail Kendaraan
            VehicleDetailSection()

            // Waktu Mulai
            InputField(title = "Mulai", value = "15.00 WIB")

            // Harga
            InputField(title = "Harga", value = "Rp. 250.000,-")

            // Sewa
            InputField(title = "Sewa", value = "3 Hari")

            // Button Perbarui
            Spacer(modifier = Modifier.height(50.dp))
            UpdateButton()
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF006D77)) // Ganti dengan warna biru tua
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Pemesanan",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}


@Composable
fun VehicleDetailSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray) // Placeholder untuk gambar
            ) {
                Text(
                    text = "X",
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Motor Vespa Biru", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("BA 1234 DC", fontSize = 14.sp)
                Text("Pasar Ombilin", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun InputField(title: String, value: String) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun UpdateButton() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { /* Action Perbarui */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6EE9C)),
            shape = CircleShape,
            modifier = Modifier
                .size(width = 120.dp, height = 40.dp)
                .shadow(8.dp, shape = CircleShape)
        ) {
            Text(
                text = "Perbarui",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    MaterialTheme {
        OrderScreen()
    }
}
