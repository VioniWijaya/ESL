package com.example.esl.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.esl.models.network.PenyewaanResponse
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items

@Composable
fun DaftarPenyewaanPage(
    modifier: Modifier = Modifier,
    penyewaanList: List<PenyewaanResponse>,
    onUlasanClick: (Int) -> Unit,
    onCancelClick: (Int) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(penyewaanList) { penyewaan ->
            Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Properti: ${penyewaan.properti.nama_properti}")
                    Text("Masa Sewa: ${penyewaan.masaSewa} hari")
                    Text("Status: ${penyewaan.status}")

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = { onUlasanClick(penyewaan.id_penyewaan) }) {
                            Text("Beri Ulasan")
                        }
                        Button(onClick = { onCancelClick(penyewaan.id_penyewaan) }) {
                            Text("Batalkan")
                        }
                    }
                }
            }
        }
    }
}
