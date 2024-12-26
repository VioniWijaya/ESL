package com.example.esl.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.esl.models.network.Report
import com.example.esl.models.network.ReportService
import kotlinx.coroutines.launch

@Composable
fun ProblemScreen(
    id_penyewaan: Int,
    onReportSubmitted: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var mediaFileName by remember { mutableStateOf("Upload File") }
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF40E0D0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF40E0D0)), // Warna latar belakang
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Header
            Text(
                text = "Laporan Masalah",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display ID Penyewaan
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ID Penyewaan: $id_penyewaan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input File
            var inputFileName by remember { mutableStateOf(mediaFileName) }
            InputField(
                title = "Upload Bukti",
                value = inputFileName,
                onValueChange = { inputFileName = it },
                placeholder = "Klik untuk memilih file"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Deskripsi
            InputField(
                title = "Deskripsi Masalah",
                value = description,
                onValueChange = { description = it },
                placeholder = "Jelaskan masalah yang terjadi"
            )

            Spacer(modifier = Modifier.weight(1f))

            // Tombol Perbarui
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (description.isNotBlank() && mediaFileName.isNotBlank()) {
                            try {
                                val report = Report(
                                    idLaporan = 0, // Dibuat otomatis oleh database
                                    id_penyewaan = id_penyewaan,
                                    media = mediaFileName,
                                    masalah = description,
                                    tanggalLaporan = ReportService.getCurrentDate()
                                )
                                ReportService.saveReport(report)
                                onReportSubmitted()
                            } catch (e: Exception) {
                                // Menangani error saat menyimpan laporan
                            }
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
                    text = "Laporkan",
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
