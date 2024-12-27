package com.example.esl.ui.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import com.example.esl.models.network.Report
import com.example.esl.models.network.ReportService
import kotlinx.coroutines.launch
import coil.compose.rememberImagePainter
import java.io.File

@Composable
fun ProblemScreen(
    id_penyewaan: Int,
    onReportSubmitted: () -> Unit,
    onNavigateBack: () -> Unit,
    context: Context
) {
    var description by remember { mutableStateOf("") }
    var mediaFileUri by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("jwt_token", null)

    // ActivityResultLauncher for picking an image from the gallery
    val getImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        mediaFileUri = uri
    }

    // ActivityResultLauncher for capturing an image with the camera
    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // Image was successfully captured
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF2ED4D8)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2ED4D8)),
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

            // Buttons for uploading/capturing images
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = { getImageLauncher.launch("image/*") },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Upload from Gallery")
                }

                Button(
                    onClick = {
                        val photoFile = File(context.cacheDir, "temp_photo.jpg")
                        val photoUri = Uri.fromFile(photoFile)
                        takePictureLauncher.launch(photoUri)
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Capture with Camera")
                }
            }

            mediaFileUri?.let {
                Image(
                    painter = rememberImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input Deskripsi
            InputField(
                title = "Deskripsi Masalah",
                value = description,
                onValueChange = { description = it },
                placeholder = "Jelaskan masalah yang terjadi"
            )

            Spacer(modifier = Modifier.weight(1f))

            // Tombol Laporkan
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (description.isNotBlank() && mediaFileUri != null) {
                            try {
                                val report = Report(
                                    idLaporan = 0, // Generated automatically by the server
                                    id_penyewaan = id_penyewaan,
                                    media = mediaFileUri.toString(), // Convert URI to file path
                                    masalah = description
                                )
                                ReportService.saveReportUsingRetrofit(report, token, context, mediaFileUri)
                                onReportSubmitted()
                            } catch (e: Exception) {
                                // Handle error
                                println("Error: ${e.message}")
                            }
                        } else {
                            println("Deskripsi atau media belum diisi.")
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