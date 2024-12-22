package com.example.esl.ui.screen

import android.Manifest
import android.net.Uri
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import com.example.esl.models.network.UlasanRequest
//import coil.compose.rememberAsyncImagePainter
import com.example.esl.ui.theme.BackgroundColor
import java.io.File

//import java.util.jar.Manifest

@Composable
fun Rating(modifier: Modifier = Modifier,
           initialRating: Int = 0,
           onRatingChanged: (Int) -> Unit = {}) {
    Column {
        var rating by remember { mutableStateOf(initialRating) }

        Row(modifier = modifier) {
            for (i in 1..5) {
                Icon(
                    imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .clickable {
                            rating = i
                            onRatingChanged(rating)
                        },
                    tint = if (i <= rating) Color.Yellow else Color.Gray
                )
            }
        }
    }
}

@Composable
fun UlasanPage(
    onSubmit: (UlasanRequest) -> Unit,
    initialUlasan: String = "",
    initialRating: Int = 0
) {
    var ulasan by remember { mutableStateOf(initialUlasan) }
    var rating by remember { mutableStateOf(initialRating) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var videoUri by remember { mutableStateOf<Uri?>(null) }

    // Temporary file for camera output
    val context = LocalContext.current
    val photoFile = remember {
        File(context.externalCacheDir, "photo_${System.currentTimeMillis()}.jpg")
    }
    val videoFile = remember {
        File(context.externalCacheDir, "video_${System.currentTimeMillis()}.mp4")
    }
    val photoUriForCamera = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        photoFile
    )
    val videoUriForCamera = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        videoFile
    )

    // Launchers for photo and video
    val photoCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri = photoUriForCamera
        }
    }

    val videoCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakeVideo()
    ) { uri ->
        videoUri = uri
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Berikan Ulasan Anda", fontSize = 20.sp)
        Spacer(Modifier.height(16.dp))

        // Rating Bar (Star Icons)
        Text("Beri Rating Properti")
        Rating(initialRating = rating) { newRating -> rating = newRating }
        Spacer(Modifier.height(16.dp))

        // Ulasan TextField
        OutlinedTextField(
            value = ulasan,
            onValueChange = { ulasan = it },
            label = { Text("Masukkan Ulasan") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        // Camera and Gallery Options
        Text("Tambahkan Foto atau Video")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { photoCameraLauncher.launch(photoUriForCamera) }) {
                Text("Ambil Foto")
            }
            Button(onClick = { videoCameraLauncher.launch(videoUriForCamera) }) {
                Text("Ambil Video")
            }
        }

        // Preview Photo and Video
        photoUri?.let {
            Text("Foto diambil:")
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        videoUri?.let {
            Text("Video diambil:")
            AndroidView(
                factory = { context ->
                    VideoView(context).apply {
                        setVideoURI(it)
                        start()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                val request = UlasanRequest(
                    id_users = 1, // Replace with actual user ID
                    id_penyewaan = 1, // Replace with actual penyewaan ID
                    ulasan = ulasan,
                    rating = rating,
                    media_ulasan = photoUri?.toString() ?: videoUri?.toString()
                )
                onSubmit(request)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kirim Ulasan")
        }
    }
}


//@Composable
//fun ReviewPage(
//) {
//
//    var reviewText by remember { mutableStateOf("") }
//    var rating by remember { mutableStateOf(0) }
//    var isFocused by remember { mutableStateOf(false) }
//    var hasCameraPermission by remember { mutableStateOf(false) }
//    var hasStoragePermission by remember { mutableStateOf(false) }
//    var photoUri by remember { mutableStateOf<Uri?>(null) }
//
//    // Launchers for permissions
//    val cameraPermissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        hasCameraPermission = isGranted
//    }
//
//    val storagePermissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        hasStoragePermission = isGranted
//    }
//
//    // Launchers for photo
//    val photoGalleryLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        photoUri = uri
//    }
//
//    val photoCameraLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.TakePicturePreview()
//    ) { bitmap ->
//        // Handle bitmap (Optional: Save or display)
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .background(BackgroundColor)
//    ) {
//        // Title
//        Text(
//            text = "Berikan Penilaian Anda",
//            fontSize = 20.sp,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//        Column {
//            Text(text = "Nilai Properti")
//            // Rating Bar
//            Rating(onRatingChanged = { newRating ->
//                rating = newRating
//            })
//        }
//
//        Column {
//            Text(text = "Tambahkan Foto atau Video")
//            // Upload Photo Section
//            Text(text = "Unggah Foto:")
//
//            // Request permissions if not granted
//            if (!hasCameraPermission || !hasStoragePermission) {
//                Button(onClick = {
//                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
//                    storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//                }) {
//                    Text("Berikan Izin Kamera dan Penyimpanan")
//                }
//            } else {
//                // Buttons to pick photo
//                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                    Button(onClick = { photoGalleryLauncher.launch("image/*") }) {
//                        Text("Pilih dari Galeri")
//                    }
//                    Button(onClick = { photoCameraLauncher.launch(null) }) {
//                        Text("Ambil Foto")
//                    }
//                }
//            }
//
//            // Display Photo
////            photoUri?.let {
////                Image(
////                    painter = rememberAsyncImagePainter(it),
////                    contentDescription = "Foto Ulasan",
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .height(200.dp),
////                    contentScale = ContentScale.Crop
////                )
////            }
////        }
//
//            // Review Input
//            OutlinedTextField(
//                value = reviewText,
//                onValueChange = { reviewText = it },
//                textStyle = TextStyle(fontSize = 16.sp),
//                label = if (!isFocused) { // Hanya tampilkan label jika tidak fokus
//                    { Text("Masukkan Ulasan Anda di Sini") }
//                } else null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp)
//                    .height(150.dp)
//                    .onFocusChanged { focusState ->
//                        isFocused = focusState.isFocused
//                    }
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
////        // Display Review and Rating
////        Text(
////            text = "Rating: $rating",
////            fontSize = 16.sp,
////            modifier = Modifier.padding(top = 8.dp)
////        )
////        Text(
////            text = "Review: $reviewText",
////            fontSize = 16.sp,
////            modifier = Modifier.padding(top = 4.dp)
////        )
//        }
//    }
//}

//@Preview(showBackground = true)
//@Composable
//private fun RatingView() {
//    ESLTheme {
//        ReviewPage()
//    }
//}