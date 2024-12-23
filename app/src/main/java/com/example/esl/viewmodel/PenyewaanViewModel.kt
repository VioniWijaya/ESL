package com.example.esl.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esl.models.network.PenyewaanResponse
import com.example.esl.models.network.RetrofitInstance
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.example.esl.ui.screen.DaftarPenyewaanPage
import kotlinx.coroutines.launch

class PenyewaanViewModel : ViewModel() {
    private val _penyewaanList = mutableStateOf<List<PenyewaanResponse>>(emptyList())
    val penyewaanList: State<List<PenyewaanResponse>> get() = _penyewaanList

    fun fetchPenyewaanByUser(userId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getPenyewaanByUser(userId)
                if (response.isSuccessful) {
                    _penyewaanList.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("PenyewaanViewModel", "Error fetching penyewaan: ${e.message}")
            }
        }
    }
}

//@Composable
//fun PenyewaanScreen(
//    viewModel: PenyewaanViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
//    onUlasanClick: (Int) -> Unit,
//    onCancelClick: (Int) -> Unit
//) {
//    val penyewaanList by viewModel.penyewaanList // Mengamati state dari ViewModel
//
//    DaftarPenyewaanPage(
//        penyewaanList = penyewaanList,
//        onUlasanClick = onUlasanClick,
//        onCancelClick = onCancelClick
//    )
//}

