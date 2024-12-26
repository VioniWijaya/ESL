//package com.example.esl.ui.screen
//
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.selection.selectable
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.RadioButton
//import androidx.compose.material3.Text
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.example.esl.models.network.ApiService
//import com.example.esl.ui.theme.BackgroundColor
//import com.example.esl.ui.theme.BarColor
//import com.example.esl.ui.theme.ButtonColors
//import com.example.esl.ui.theme.ESLTheme
//import com.example.esl.viewmodel.CancellationUiState
//import com.example.esl.viewmodel.CancellationViewModel
//
//@Composable
//fun CancellationScreen(
//    modifier: Modifier = Modifier,
//    apiService: ApiService,  // Pass ApiService directly
//    rentalId: String,
//    onNavigateBack: () -> Unit
//) {
//    val viewModel = remember { CancellationViewModel(apiService) }
//    val context = LocalContext.current
//    val cancellationState by viewModel.cancellationState.collectAsState(initial = CancellationUiState.Initial)
//
//    var selectedReason by remember { mutableStateOf<String?>(null) }
//    var otherReason by remember { mutableStateOf("") }
//
//    val reasons = listOf(
//        "Harga sewa terlalu mahal",
//        "Kondisi properti yang buruk",
//        "Ingin mengubah pesanan",
//        "Berubah pikiran/ada rencana lain",
//        "Lainnya"
//    )
//
//    if (cancellationState is CancellationUiState.Success) {
//        AlertDialog(
//            onDismissRequest = { onNavigateBack() },
//            title = { Text("Berhasil") },
//            text = { Text("Penyewaan berhasil dibatalkan") },
//            confirmButton = {
//                Button(
//                    onClick = { onNavigateBack() }
//                ) {
//                    Text("OK")
//                }
//            }
//        )
//    }
//    LaunchedEffect(cancellationState) {
//        when (cancellationState) {
//            is CancellationUiState.Success -> {
//                Toast.makeText(context, "Pesanan berhasil dibatalkan", Toast.LENGTH_SHORT).show()
//                onNavigateBack()
//            }
//            is CancellationUiState.Error -> {
//                Toast.makeText(
//                    context,
//                    (cancellationState as CancellationUiState.Error).message,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            else -> {}
//        }
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Alasan Pembatalan",
//            style = MaterialTheme.typography.titleLarge,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        reasons.forEach { reason ->
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable { selectedReason = reason }
//                    .padding(vertical = 8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                RadioButton(
//                    selected = selectedReason == reason,
//                    onClick = { selectedReason = reason }
//                )
//                Text(
//                    text = reason,
//                    modifier = Modifier.padding(start = 8.dp)
//                )
//            }
//        }
//
//        if (selectedReason == "Lainnya") {
//            OutlinedTextField(
//                value = otherReason,
//                onValueChange = { otherReason = it },
//                label = { Text("Masukkan alasan lainnya") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
//            )
//        }
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
//        ) {
//            OutlinedButton(
//                onClick = onNavigateBack,
//                enabled = cancellationState !is CancellationUiState.Loading
//            ) {
//                Text("Kembali")
//            }
//
//            Button(
//                onClick = {
//                    val finalReason = if (selectedReason == "Lainnya") otherReason
//                    else selectedReason
//                    if (finalReason.isNullOrBlank()) {
//                        Toast.makeText(context,
//                            "Mohon pilih alasan pembatalan",
//                            Toast.LENGTH_SHORT).show()
//                        return@Button
//                    }
//                    viewModel.cancelRental(rentalId, finalReason)
//                },
//                enabled = cancellationState !is CancellationUiState.Loading
//            ) {
//                if (cancellationState is CancellationUiState.Loading) {
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(24.dp)
////                        color = MaterialTheme.colors.onPrimary
//                    )
//                } else {
//                    Text("Batalkan Pesanan")
//                }
//            }
//        }
//    }
//}
////@Preview
////@Composable
////private fun CancelPrev() {
////    ESLTheme {
////        CancelRent()
////    }
////}