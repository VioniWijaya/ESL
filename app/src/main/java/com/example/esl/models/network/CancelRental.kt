import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

// Data Classes
data class CancellationRequest(
    val alasan_batal: String
)

data class CancellationResponse(
    val success: Boolean,
    val message: String
)

// API Interface
interface CancelApi {
    @POST("api/penyewaan/{id_penyewaan}/cancel")
    suspend fun cancelPenyewaan(
        @Path("id_penyewaan") id_penyewaan: Int,
        @Body request: CancellationRequest,
        @Header("Authorization") token: String
    ): Response<CancellationResponse>

    companion object {
        private const val BASE_URL = "http://192.168.54.66:3000/"

        fun create(): CancelApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CancelApi::class.java)
        }
    }
}

// ViewModel
class CancelViewModel : ViewModel() {
    private val apiService = CancelApi.create()

    private val _cancelState = MutableStateFlow<CancelState>(CancelState.Idle)
    val cancelState: StateFlow<CancelState> = _cancelState.asStateFlow()

    fun cancelPenyewaan(id_penyewaan: Int, alasan_batal: String, token: String) {
        viewModelScope.launch {
            _cancelState.value = CancelState.Loading
            try {
                val authToken = "Bearer $token"
                val response = apiService.cancelPenyewaan(
                    id_penyewaan = id_penyewaan,
                    request = CancellationRequest(alasan_batal = alasan_batal),
                    token = authToken
                )

                if (response.isSuccessful) {
                    Log.d("CancelViewModel", "Pembatalan berhasil")
                    _cancelState.value = CancelState.Success(
                        response.body()?.message ?: "Pembatalan berhasil"
                    )
                } else {
                    Log.e("CancelViewModel", "Pembatalan gagal: ${response.code()}")
                    _cancelState.value = CancelState.Error(
                        "Gagal membatalkan penyewaan: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("CancelViewModel", "Error: ${e.message}")
                _cancelState.value = CancelState.Error(
                    e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }
}

sealed class CancelState {
    object Idle : CancelState()
    object Loading : CancelState()
    data class Success(val message: String) : CancelState()
    data class Error(val error: String) : CancelState()
}