package com.example.esl.repository

import com.example.esl.models.network.RescheduleService

class Repository(private val rescheduleService: RescheduleService) {
    suspend fun getPenyewaan(id: String) = rescheduleService.getPenyewaan(id)
    suspend fun getProperti(id: String) = rescheduleService.getProperti(id)
}