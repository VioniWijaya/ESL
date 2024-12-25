package com.example.esl.ui.screen

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.esl.ui.theme.ESLTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.esl.models.network.UlasanRequest
import coil.compose.rememberAsyncImagePainter
import com.example.esl.ui.component.BottomNavBar
import com.example.esl.ui.theme.BackgroundColor
import com.example.esl.viewmodel.UlasanViewModel
import java.io.File
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import com.example.esl.ui.theme.BarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UlasanPage(
    viewModel: UlasanViewModel,
    penyewaanId: Int,
    onSubmit: (UlasanRequest) -> Unit,
    onNavigateBack: () -> Unit,
    initialRating: Int,
    initialUlasan: String
) {
    var rating by remember { mutableStateOf(initialRating) }
    var ulasan by remember { mutableStateOf(initialUlasan) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Launcher untuk membuka kamera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val uri = saveBitmapToUri(context, bitmap)
            if (uri != null) {
                imageUri = uri
            } else {
                Toast.makeText(context, "Gagal menyimpan foto.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Gagal mengambil foto.", Toast.LENGTH_SHORT).show()
        }
    }


    // Launcher untuk memilih foto dari galeri
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            imageUri = uri
        } else {
            Toast.makeText(context, "Gagal memilih foto.", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(

                title = { Text("Beri Ulasan") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { // Update onClick handler
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
       }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2ED4D8))
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("Beri Bintang", fontWeight = FontWeight.Bold)
                RatingBar(rating = rating, onRatingChanged = { rating = it })

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = ulasan,
                    onValueChange = { ulasan = it },
                    label = { Text("Tulis Ulasan!") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(onClick = { cameraLauncher.launch() }) {
                        Text("Ambil Foto")
                    }

                    OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                        Text("Pilih dari Galeri")
                    }
                }

                // Tampilkan gambar jika URI tersedia
                imageUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Preview Foto",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                } ?: run {
                    Text(
                        text = "Tidak ada foto yang diambil.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = Color.White
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        val ulasanRequest = UlasanRequest(
                            id_users = 1,
                            id_penyewaan = penyewaanId,
                            ulasan = ulasan,
                            rating = rating,
                            media_ulasan = imageUri?.toString() // Simpan URI sebagai string
                        )
                        onSubmit(ulasanRequest)
                    }) {
                        Text("Submit")
                    }
                    Button(onClick = { /* Navigasi kembali */ }) {
                        Text("Batal")
                    }
                }
            }
        }
    }
}

// Fungsi untuk menyimpan Bitmap sebagai file dan mengembalikan URI-nya
fun saveBitmapToUri(context: Context, bitmap: Bitmap): Uri? {
    return try {
        val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


@Composable
fun RatingBar(rating: Int, onRatingChanged: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.Center) {
        (1..5).forEach { star ->
            Icon(
                imageVector = if (star <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = null,
                modifier = Modifier
                    .size(66.dp)
                    .clickable { onRatingChanged(star) },
                tint = if (star <= rating) Color.Yellow else Color.White
            )
        }
    }
}


