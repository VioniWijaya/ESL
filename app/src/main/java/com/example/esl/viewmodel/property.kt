package com.example.esl.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esl.models.local.entities.Property
import com.example.esl.models.network.RetrofitInstance
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PropertyViewModel : ViewModel() {

    private val _propertyDetail = MutableStateFlow<Property?>(null)
    val propertyDetail: StateFlow<Property?> = _propertyDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun getPropertyDetail(propertyId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                Log.d("PropertyViewModel", "Fetching property with id: $propertyId")
                val response = RetrofitInstance.api.getDetailProperti(propertyId)

                if (response.isSuccessful) {
                    val propertyResponse = response.body()
                    Log.d("PropertyViewModel", "Response received: $propertyResponse")

                    if (propertyResponse?.success == true) {
                        _propertyDetail.value = propertyResponse.data
                        Log.d("PropertyViewModel", "Property set: ${_propertyDetail.value}")
                    } else {
                        _errorMessage.value = propertyResponse?.message ?: "Unknown error"
                        Log.e("PropertyViewModel", "Error: ${_errorMessage.value}")
                    }
                } else {
                    _errorMessage.value = "Server error: ${response.code()}"
                    Log.e("PropertyViewModel", "Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("PropertyViewModel", "Error fetching property", e)
                _errorMessage.value = when (e) {
                    is IOException -> "Network error: Check your internet connection"
                    is HttpException -> "Server error: ${e.code()}"
                    else -> "Unknown error: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }


    private val _propertyList = MutableStateFlow<List<Property>?>(null)
    val propertyList: StateFlow<List<Property>?> = _propertyList

    fun getAllProperties() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = RetrofitInstance.api.getAllProperties()
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.let { propertyResponse ->
                        if (propertyResponse.success) {
                            _propertyList.value = propertyResponse.data
                        } else {
                            _errorMessage.value = propertyResponse.message
                        }
                    }
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            } catch (e: IOException) {
                _errorMessage.value = "Network Error: ${e.message}"
            } catch (e: HttpException) {
                _errorMessage.value = "HTTP Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel() // Cancel semua coroutines
        _propertyDetail.value = null // Clear data
        _errorMessage.value = null
    }
}




