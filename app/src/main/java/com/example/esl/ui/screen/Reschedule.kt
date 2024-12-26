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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.esl.models.network.RescheduleViewModel
import kotlinx.coroutines.launch

@Composable
fun RescheduleScreen(
    idPenyewaan: String,
    onNavigateBack: () -> Unit,
    onRescheduleSuccess: () -> Unit,
    viewModel: RescheduleViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(idPenyewaan) {
        viewModel.loadRescheduleDetails(idPenyewaan)
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
                    Text(text = uiState.lokasiProperti, fontSize = 14.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input Tanggal Mulai
            var tanggalMulai by remember { mutableStateOf(uiState.tanggalMulai) }
            InputField(
                title = "Tanggal Mulai",
                value = tanggalMulai,
                onValueChange = { tanggalMulai = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Masa Sewa
            var masaSewa by remember { mutableStateOf(uiState.masaSewa.toString()) }
            InputField(
                title = "Masa Sewa (Hari)",
                value = masaSewa,
                onValueChange = { masaSewa = it }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            viewModel.updateRescheduleDetails(
                                idPenyewaan = idPenyewaan,
                                tanggalMulai = tanggalMulai,
                                masaSewa = masaSewa.toInt()
                            )
                            onRescheduleSuccess() // Navigasi ke halaman Status setelah sukses
                        } catch (e: Exception) {
                            // Tangani error jika perlu
                        }
                    }
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

            Button(
                onClick = onNavigateBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = "Kembali",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun InputField(title: String, value: String, onValueChange: (String) -> Unit) {
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
