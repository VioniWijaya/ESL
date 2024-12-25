package com.example.esl.models.network

import retrofit2.http.GET
import retrofit2.http.Path
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esl.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Penyewaan(
    val id: String,
    val tanggalMulai: String,
    val masaSewa: Int
)

data class Properti(
    val id: String,
    val namaProperti: String,
    val jenisProperti: String,
    val hargaSewa: Int
)

data class OrderUiState(
    val isLoading: Boolean = true,
    val namaProperti: String = "",
    val jenisProperti: String = "",
    val hargaSewa: Int = 0,
    val tanggalMulai: String = "",
    val masaSewa: Int = 0
)


interface Order {
    @GET("penyewaan/{id}")
    suspend fun getPenyewaan(@Path("id") id: String): Penyewaan

    @GET("properti/{id}")
    suspend fun getProperti(@Path("id") id: String): Properti
}

class OrderViewModel(private val repository: Repository) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState

    fun loadOrderDetails(idPenyewaan: String, idProperti: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val penyewaan = repository.getPenyewaan(idPenyewaan)
                val properti = repository.getProperti(idProperti)

                _uiState.value = OrderUiState(
                    isLoading = false,
                    namaProperti = properti.namaProperti,
                    jenisProperti = properti.jenisProperti,
                    hargaSewa = properti.hargaSewa * penyewaan.masaSewa,
                    tanggalMulai = penyewaan.tanggalMulai,
                    masaSewa = penyewaan.masaSewa
                )
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error
            }
        }
    }

    fun updateTanggalMulai(tanggal: String) {
        _uiState.value = _uiState.value.copy(tanggalMulai = tanggal)
    }
}