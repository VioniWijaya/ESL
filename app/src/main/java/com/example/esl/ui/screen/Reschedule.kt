package com.example.esl.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.esl.models.network.RescheduleViewModel
import kotlinx.coroutines.launch

@Composable
fun RescheduleScreen(
    id_penyewaan: String,
    onNavigateBack: () -> Unit,
    onRescheduleSuccess: () -> Unit,
    viewModel: RescheduleViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(id_penyewaan) {
        viewModel.loadRescheduleDetails(id_penyewaan)
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF40E0D0)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Reschedule Penyewaan",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Detail Properti
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = uiState.namaProperti, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = uiState.jenisProperti, fontSize = 14.sp, color = Color.Gray)
                    Text(text = uiState.lokasi, fontSize = 14.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input Tanggal Mulai
            var tanggalMulai by remember { mutableStateOf(uiState.tanggalMulai) }
            InputField(
                title = "Tanggal Mulai",
                value = tanggalMulai,
                onValueChange = { tanggalMulai = it },
                placeholder = "Masukkan tanggal mulai (YYYY-MM-DD)"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Masa Sewa
            var masaSewa by remember { mutableStateOf(uiState.masaSewa.toString()) }
            InputField(
                title = "Masa Sewa (Hari)",
                value = masaSewa,
                onValueChange = { masaSewa = it },
                placeholder = "Masukkan jumlah hari",
                isNumeric = true
            )

            Spacer(modifier = Modifier.weight(1f))

            // Tombol Perbarui
            Button(
                onClick = {
                    Log.d("UpdateButton", "Tombol Perbarui diklik")
                    coroutineScope.launch {
                        Log.d("UpdateCoroutine", "Coroutine dimulai")
                        if (tanggalMulai.isNotBlank() && masaSewa.isNotBlank() && masaSewa.toIntOrNull() != null) {
                            Log.d("InputValidation", "Input valid")
                            try {
                                Log.d("API Call", "Mengirim request untuk perbarui reschedule")
                                viewModel.updateRescheduleDetails(
                                    id_penyewaan = id_penyewaan,
                                    tanggalMulai = tanggalMulai,
                                    masaSewa = masaSewa.toInt()
                                )
                                Log.d("UpdateSuccess", "Reschedule berhasil diperbarui")
                                onRescheduleSuccess()
                            } catch (e: Exception) {
                                val errorMessage = "Terjadi kesalahan: ${e.localizedMessage}"
                                Log.e("UpdateError", errorMessage, e)
                            }
                        } else {
                            val errorMessage = "Harap isi semua kolom dengan benar"
                            Log.e("InputValidationError", errorMessage)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0FFFF)),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Perbarui",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            // Tombol Kembali
            Button(
                onClick = onNavigateBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp)
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
fun InputField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isNumeric: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = value,
            onValueChange = { newValue ->
                if (isNumeric) {
                    if (newValue.all { it.isDigit() }) {
                        onValueChange(newValue)
                    }
                } else {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White.copy(alpha = 0.2f),
                focusedContainerColor = Color.White.copy(alpha = 0.2f)
            ),
            shape = RoundedCornerShape(8.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        )
    }
}
