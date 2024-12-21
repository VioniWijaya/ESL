package com.example.esl.models.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notifikasi")
data class Notifikasi(
    @PrimaryKey(autoGenerate = true) val id_notifikasi: Int = 0,
    val id_users: Int,
    val tanggal_kirim: String,
    val status_notifikasi: String // Enum: unread, read)
)
