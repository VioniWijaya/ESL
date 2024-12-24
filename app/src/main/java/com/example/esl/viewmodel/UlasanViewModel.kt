package com.example.esl.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esl.models.network.RetrofitInstance
import com.example.esl.models.network.UlasanRequest
import com.example.esl.models.network.UlasanResponse
import com.example.esl.repository.UlasanRepository
import kotlinx.coroutines.launch

class UlasanViewModel : ViewModel() {

    private val _ulasanState = mutableStateOf<List<UlasanResponse>>(emptyList())
    val ulasanState: State<List<UlasanResponse>> = _ulasanState

    private val ulasanApi = RetrofitInstance.api // Instance API Retrofit Anda

    fun createUlasan(ulasanRequest: UlasanRequest,  onSuccess: () -> Unit) {
        viewModelScope.launch {
            Log.d("Ulasan", "Mulai proses ulasan")
            try {
                val response = ulasanApi.createUlasan(ulasanRequest)
                Log.d("Ulasan", "Response: $response")
                if (response.isSuccessful) {
                    Log.d("Ulasan", "Ulasan berhasil dibuat: ${response.body()}")
                    onSuccess()
                } else {
                    Log.e("Ulasan", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Ulasan", "Exception: ${e.message}")
            }
        }
    }

    fun updateUlasan(id: Int, ulasanRequest: UlasanRequest) {
        viewModelScope.launch {
            try {
                val response = ulasanApi.updateUlasan(id, ulasanRequest)
                if (response.isSuccessful) {
                    Log.d("Ulasan", "Ulasan berhasil diupdate: ${response.body()}")
                } else {
                    Log.e("Ulasan", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Ulasan", "Exception: ${e.message}")
            }
        }
    }

    fun getUlasanByPenyewaan(idPenyewaan: Int) {
        viewModelScope.launch {
            try {
                val response = ulasanApi.getUlasanByPenyewaan(idPenyewaan)
                if (response.isSuccessful) {
                    _ulasanState.value = response.body() ?: emptyList()
                } else {
                    Log.e("Ulasan", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Ulasan", "Exception: ${e.message}")
            }
        }
    }
}
