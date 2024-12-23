package com.example.esl.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
class ReportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                OrderReportScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderReportScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Laporan Pesanan") },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile Icon"
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF008080)
                )
            )
        },
        bottomBar = {
            ReportBottomNavigationBar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            MonthPicker()

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* TODO: Unduh laporan */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Unduh")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OrderCard(
                title = "Nama Rumah",
                detail = "Rumah Tepi Danau",
                date = "04/08/2024",
                owner = "Ruchi"
            )

            OrderCard(
                title = "Nama Kendaraan",
                detail = "Motor Beat BA 1954 AB",
                date = "18/08/2024",
                owner = "Viori"
            )

            OrderCard(
                title = "Nama Kendaraan",
                detail = "Sepeda Gunung",
                date = "06/09/2024",
                owner = "Maria"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthPicker() {
    var expanded by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf("Pilih bulan") }

    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { expanded = true },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Text(selectedMonth)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            val months = listOf(
                "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                "Juli", "Agustus", "September", "Oktober", "November", "Desember"
            )
            months.forEach { month ->
                DropdownMenuItem(
                    text = { Text(month) },
                    onClick = {
                        selectedMonth = month
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun OrderCard(title: String, detail: String, date: String, owner: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("$title: $detail", fontSize = 16.sp, color = Color.Black)
            Text("Tanggal: $date", fontSize = 14.sp, color = Color.Gray)
            Text("Pemilik: $owner", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ReportBottomNavigationBar() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home Icon"
                )
            },
            label = { Text("Home", fontSize = 10.sp) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            label = { Text("Search", fontSize = 10.sp) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "History Icon"
                )
            },
            label = { Text("History", fontSize = 10.sp) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOrderReportScreen() {
    MaterialTheme {
        OrderReportScreen()
    }
}
