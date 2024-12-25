package com.example.esl.models.network

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

// Data model untuk status rental
data class RentalStatus(
    val status: String,
    val tanggalMulai: String,
    val tanggalAkhir: String,
    val nama_properti: String
)

interface StatusApi {
    @GET("api/status")
    suspend fun getStatus(@Header("Authorization") token: String): Response<List<RentalStatus>>

    companion object {
        private const val BASE_URL = "http://192.168.19.66:3000/"

        fun create(): StatusApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(StatusApi::class.java)
        }
    }
}

// ViewModel untuk mengelola data status rental
class StatusViewModel : ViewModel() {

    private val apiService = StatusApi.create()

    private val _statusData = MutableStateFlow<List<RentalStatus>>(emptyList())
    val statusData: StateFlow<List<RentalStatus>> get() = _statusData

    // Fungsi untuk memuat data dari API dengan token
    fun loadStatus(token: String) {
        viewModelScope.launch {
            try {
                val response: Response<List<RentalStatus>> = apiService.getStatus(token)
                if (response.isSuccessful) {
                    Log.d("StatusViewModel", "API Response: ${response.body()}")
                    _statusData.value = response.body() ?: emptyList()
                } else {
                    Log.e("StatusViewModel", "API call failed with code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("StatusViewModel", "Error loading status: ${e.message}")
            }
        }
    }
}
