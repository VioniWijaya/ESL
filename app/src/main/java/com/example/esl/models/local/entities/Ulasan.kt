package com.example.esl.models.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ulasan")
data class Ulasan(
    @PrimaryKey(autoGenerate = true) val id_ulasan: Int = 0,
    val id_users: Int,
    val id_penyewaan: Int,
    val ulasan: String,
    val rating: Int,
    val media_ulasan: String?,
    val tanggal_input: String
)
