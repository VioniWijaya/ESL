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

data class RentalHistory(
    val status: String,
    val tanggalOrder: String,
    val nama_properti: String,
    val pemilik: String
)

interface RentalApi {
    @GET("api/rental")
    suspend fun getRentals(@Header("Authorization") token: String): Response<List<RentalHistory>>

    companion object {
        private const val BASE_URL = "http://192.168.19.66:3000/"

        fun create(): RentalApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(RentalApi::class.java)
        }
    }
}

class RentalViewModel : ViewModel() {

    private val apiService = RentalApi.create()

    private val _rentalData = MutableStateFlow<List<RentalHistory>>(emptyList())
    val rentalData: StateFlow<List<RentalHistory>> get() = _rentalData

    // Fungsi untuk memuat data dari API dengan token
    fun loadRentals(token: String) {
        viewModelScope.launch {
            try {
                val response: Response<List<RentalHistory>> = apiService.getRentals(token)
                if (response.isSuccessful) {
                    Log.d("RentalViewModel", "API Response: ${response.body()}")
                    _rentalData.value = response.body() ?: emptyList()
                } else {
                    Log.e("RentalViewModel", "API call failed with code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("RentalViewModel", "Error loading rentals: ${e.message}")
            }
        }
    }
}
