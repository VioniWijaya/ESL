package com.example.esl.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.esl.models.network.OrderViewModel
import kotlinx.coroutines.launch

@Composable
fun OrderScreen(
    idPenyewaan: String,
    idProperti: String,
    viewModel: OrderViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadOrderDetails(idPenyewaan, idProperti)
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color(0xFFB2EBF2)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card Properti
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = uiState.namaProperti, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = uiState.jenisProperti, fontSize = 14.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input Tanggal Mulai
            InputField(
                title = "Mulai",
                value = uiState.tanggalMulai,
                onValueChange = { viewModel.updateTanggalMulai(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Harga Sewa
            InputField(title = "Harga", value = "Rp. ${uiState.hargaSewa},-")

            Spacer(modifier = Modifier.height(16.dp))

            // Masa Sewa
            InputField(title = "Sewa", value = "${uiState.masaSewa} Hari")

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // Update API call logic
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6EE9C)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp)
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
}

@Composable
fun InputField(title: String, value: String, onValueChange: (String) -> Unit = {}) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE0E0E0),
                unfocusedContainerColor = Color(0xFFE0E0E0)
            )
        )
    }
}
