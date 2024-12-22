package com.example.esl.ui.screen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import com.example.esl.ui.theme.BarColor
import com.example.esl.ui.theme.ButtonColors
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.tooling.preview.Preview
import com.example.esl.ui.theme.ESLTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PemesananScreen() {
    var durasi by remember { mutableStateOf("3 hari") }
    var mulai by remember { mutableStateOf("15.00 WIB") }
    var harga by remember { mutableStateOf("Rp. 250.000,-") }
    var selectedOption by remember { mutableStateOf("Shopeepay") }
    var isFileUploaded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pemesanan") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BarColor, // Warna biru
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
                .background(Color(0xFFB2DFDB))
        ) {
            DetailProperty()
            Spacer(modifier = Modifier.height(16.dp))
            FormField(label = "Durasi", value = durasi, onValueChange = { durasi = it })
            FormField(label = "Mulai", value = mulai, onValueChange = { mulai = it })
            FormField(label = "Harga", value = harga, onValueChange = { harga = it })
            Spacer(modifier = Modifier.height(16.dp))
            MetodePembayaranDropdown(
                selectedOption = selectedOption,
                onOptionSelected = { selectedOption = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            UploadFileButton(
                isFileUploaded = isFileUploaded,
                onFileUploaded = { isFileUploaded = true }
            )
            Spacer(modifier = Modifier.height(24.dp))
            PesanButton(
                onClick = {
                    // Handle pesan
                }
            )
        }
    }
}

@Composable
fun DetailProperty() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.LightGray)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text("Motor Vespa Biru", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("Bk 1234 BC", fontSize = 14.sp)
            Text("Pasar Onilin", fontSize = 14.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, fontSize = 14.sp, color = Color.Gray)
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
//            colors = TextFieldDefaults.textFieldColors(
//                containerColor = Color.White,
//                focusedIndicatorColor = Color.Gray,
//                unfocusedIndicatorColor = Color.LightGray
//            )
        )
    }
}

@Composable
fun MetodePembayaranDropdown(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text("Metode Pembayaran", fontSize = 14.sp, color = Color.Gray)
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedOption, modifier = Modifier.weight(1f))
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("Shopeepay", "Gopay", "Transfer Bank").forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UploadFileButton(isFileUploaded: Boolean, onFileUploaded: () -> Unit) {
    Column {
        Text("Bukti Pembayaran", fontSize = 14.sp, color = Color.Gray)
        OutlinedButton(
            onClick = { onFileUploaded() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Icon(Icons.Filled.FileUpload, contentDescription = "Upload")
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isFileUploaded) "File Uploaded" else "Upload File")
        }
    }
}

@Composable
fun PesanButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ButtonColors)
    ) {
        Text("Pesan", color = Color.White, fontSize = 16.sp)
    }
}


@Preview
@Composable
private fun PemesananPrev() {
    ESLTheme {
        PemesananScreen()
    }
}