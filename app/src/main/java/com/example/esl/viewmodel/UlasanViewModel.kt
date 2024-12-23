package com.example.esl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esl.models.network.UlasanRequest
import com.example.esl.models.network.UlasanResponse
import com.example.esl.repository.UlasanRepository
import kotlinx.coroutines.launch

class UlasanViewModel(private val repository: UlasanRepository) : ViewModel() {
    val ulasanResponse = MutableLiveData<UlasanResponse?>()
    val error = MutableLiveData<String?>()

    // Tambahkan userId dan penyewaanId jika diperlukan
    val userId = MutableLiveData<Int?>()
    val penyewaanId = MutableLiveData<Int?>()

    fun createUlasan(ulasanRequest: UlasanRequest) {
        viewModelScope.launch {
            val result = repository.createUlasan(ulasanRequest)
            result.fold(
                onSuccess = { ulasanResponse.postValue(it) },
                onFailure = { error.postValue(it.message) }
            )
        }
    }

    fun updateUlasan(id: Int, ulasanRequest: UlasanRequest) {
        viewModelScope.launch {
            val result = repository.updateUlasan(id, ulasanRequest)
            result.fold(
                onSuccess = { ulasanResponse.postValue(it) },
                onFailure = { error.postValue(it.message) }
            )
        }
    }
}
