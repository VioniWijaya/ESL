//package com.example.esl.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.esl.models.network.ApiService
//import com.example.esl.models.network.CancellationRequest
//import com.example.esl.models.network.CancellationResponse
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//// ViewModel
//class CancellationViewModel(
//    private val apiService: ApiService
//) : ViewModel() {
//    private val _cancellationState = MutableStateFlow<CancellationUiState>(CancellationUiState.Initial)
//    val cancellationState = _cancellationState.asStateFlow()
//
//    fun cancelRental(rentalId: String, reason: String) {
//        viewModelScope.launch {
//            _cancellationState.value = CancellationUiState.Loading
//            try {
//                val response = apiService.cancelRental(CancellationRequest(rentalId, reason))
//                if (response.isSuccessful) {
//                    _cancellationState.value = CancellationUiState.Success(response.body()!!)
//                } else {
//                    _cancellationState.value = CancellationUiState.Error("Gagal membatalkan pesanan")
//                }
//            } catch (e: Exception) {
//                _cancellationState.value = CancellationUiState.Error(e.message ?: "Terjadi kesalahan")
//            }
//        }
//    }
//}
//
//// UI State
//sealed class CancellationUiState {
//    object Initial : CancellationUiState()
//    object Loading : CancellationUiState()
//    data class Success(val response: CancellationResponse) : CancellationUiState()
//    data class Error(val message: String) : CancellationUiState()
//}
//
