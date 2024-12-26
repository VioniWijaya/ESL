package com.example.esl.models.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

// --- Data Class for UI State ---
data class RescheduleUiState(
    val isLoading: Boolean = true,
    val namaProperti: String = "",
    val jenisProperti: String = "",
    val lokasiProperti: String = "",
    val tanggalMulai: String = "",
    val masaSewa: Int = 0,
    val error: String? = null
)

// --- API Response Model ---
data class RescheduleDetailsResponse(
    val idPenyewaan: String,
    val namaProperti: String,
    val jenisProperti: String,
    val lokasiProperti: String,
    val tanggalMulai: String,
    val masaSewa: Int
)

// --- API Request Model for Updating Reschedule ---
data class UpdateRescheduleRequest(
    val tanggalMulai: String,
    val masaSewa: Int
)

// --- Retrofit API Service ---
interface RescheduleService {
    @GET("rentals/{id}")
    suspend fun getRescheduleDetails(@Path("id") id: String): RescheduleDetailsResponse

    @PUT("rentals/{id}")
    suspend fun updateReschedule(
        @Path("id") id: String,
        @Body request: UpdateRescheduleRequest
    )

    companion object {
        fun create(): RescheduleService {
            return Retrofit.Builder()
                .baseUrl("http://192.168.233.66:3000/") // Replace with your base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RescheduleService::class.java)
        }
    }
}

// --- ViewModel ---
class RescheduleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RescheduleUiState())
    val uiState: StateFlow<RescheduleUiState> = _uiState

    private val apiService = RescheduleService.create()

    fun loadRescheduleDetails(idPenyewaan: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val details = apiService.getRescheduleDetails(idPenyewaan)
                _uiState.value = RescheduleUiState(
                    isLoading = false,
                    namaProperti = details.namaProperti,
                    jenisProperti = details.jenisProperti,
                    lokasiProperti = details.lokasiProperti,
                    tanggalMulai = details.tanggalMulai,
                    masaSewa = details.masaSewa
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error: ${e.message}"
                )
            }
        }
    }

    fun updateRescheduleDetails(idPenyewaan: String, tanggalMulai: String, masaSewa: Int) {
        viewModelScope.launch {
            try {
                apiService.updateReschedule(
                    idPenyewaan,
                    UpdateRescheduleRequest(tanggalMulai, masaSewa)
                )
                _uiState.value = _uiState.value.copy(
                    tanggalMulai = tanggalMulai,
                    masaSewa = masaSewa
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to update: ${e.message}")
            }
        }
    }
}