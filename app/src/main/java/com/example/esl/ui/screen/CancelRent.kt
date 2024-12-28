import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.esl.ui.theme.BarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancelScreen(
    id_penyewaan: Int,
    viewModel: CancelViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    var selectedReason by remember { mutableStateOf<String?>(null) }
    var otherReason by remember { mutableStateOf("") }
    val cancelState by viewModel.cancelState.collectAsState()
    val context = LocalContext.current

    // Get token from SharedPreferences
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("jwt_token", null)

    LaunchedEffect(cancelState) {
        when (cancelState) {
            is CancelState.Success -> {
                Toast.makeText(
                    context,
                    (cancelState as CancelState.Success).message,
                    Toast.LENGTH_LONG
                ).show()
                onNavigateBack()
            }

            is CancelState.Error -> {
                Toast.makeText(context, (cancelState as CancelState.Error).error, Toast.LENGTH_LONG)
                    .show()
            }

            else -> {}
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alasan Pembatalan") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BarColor, // Warna biru
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2ED4D8))
                .padding(innerPadding)
        ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "Berikan alasan pembatalan penyewaan",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val reasons = listOf(
                "Harga sewa terlalu mahal",
                "Kondisi properti yang buruk",
                "Ingin mengubah pesanan",
                "Berubah pikiran/ada rencana lain",
                "Lainnya"
            )

            reasons.forEach { reason ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { selectedReason = reason }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedReason == reason,
                        onClick = { selectedReason = reason }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = reason)
                }
            }

            if (selectedReason == "Lainnya") {
                OutlinedTextField(
                    value = otherReason,
                    onValueChange = { otherReason = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    label = { Text("Masukkan alasan lainnya") },
                    maxLines = 3
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onNavigateBack,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text("Kembali")
                }

                Button(
                    onClick = {
                        val finalReason = if (selectedReason == "Lainnya") otherReason else selectedReason

                        if (!finalReason.isNullOrBlank() && token != null) {
                            viewModel.cancelPenyewaan(id_penyewaan, finalReason, token)
                        } else {
                            Toast.makeText(context, "Alasan atau token tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = (selectedReason != null &&
                            (selectedReason != "Lainnya" || otherReason.isNotBlank())) &&
                            cancelState !is CancelState.Loading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    if (cancelState is CancelState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text("Batalkan Pesanan", color = Color.White)
                    }
                }
            }
        }
    }
}
}