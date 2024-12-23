package com.example.esl.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.NavController

@Composable
fun TopButtonBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color(0xFF007B7F))
    ) {
        // Tombol Status
        TextButton(
            onClick = {
                navController.navigate(Screen.Status.route)
            },
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Status",
                color = Color(0xFFB2DFDB),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // Tombol Riwayat
        TextButton(
            onClick = {
                navController.navigate(Screen.History.route)
            },
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Riwayat",
                color = Color(0xFFB2DFDB),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}


