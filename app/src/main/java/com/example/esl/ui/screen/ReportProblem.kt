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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.esl.models.network.Report
import com.example.esl.models.network.ReportService

import java.util.*

@Composable
fun ProblemScreen(
    idPenyewaan: String,
    onReportSubmitted: () -> Unit // Callback setelah laporan berhasil disimpan
) {
    var description by remember { mutableStateOf("") }
    var mediaFileName by remember { mutableStateOf("Upload File") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF71D6CC) // Background color
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header()

            Spacer(modifier = Modifier.height(16.dp))

            // Display idPenyewaan
            Text(
                text = "ID Penyewaan: $idPenyewaan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Upload File Section
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text(text = "Upload Bukti", fontSize = 16.sp, color = Color.Black)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color(0xFFD9D9D9), RoundedCornerShape(8.dp))
                        .clickable {
                            // Simulasikan upload file
                            mediaFileName = "bukti_laporan.png"
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = mediaFileName, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description Section
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text(text = "Deskripsi", fontSize = 16.sp, color = Color.Black)

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Jelaskan masalah", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        // Buat laporan dan simpan menggunakan ReportService
                        val report = Report(
                            idLaporan = UUID.randomUUID().toString(),
                            idUsers = "current_user_id", // Ganti dengan ID pengguna yang sedang login
                            idPenyewaan = idPenyewaan,
                            media = mediaFileName,
                            masalah = description,
                            tanggalLaporan = ReportService.getCurrentDate()
                        )
                        ReportService.saveReport(report)
                        onReportSubmitted()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCFFFB1)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.width(120.dp)
                ) {
                    Text(text = "Laporkan", color = Color.Black)
                }

                Button(
                    onClick = { /* Handle Batal */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9D9D9)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.width(120.dp)
                ) {
                    Text(text = "Batal", color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun Header() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF006D77)) // Ganti dengan warna biru tua
            .padding(8.dp)
    ) {
        Text(
            text = "Beri Ulasan",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ProblemScreenPreview() {
//    ProblemScreen(idPenyewaan = "12345") {}
//}
