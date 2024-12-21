package com.example.esl.models.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laporanmasalah")
data class LaporanMasalah(
    @PrimaryKey(autoGenerate = true) val id_laporan: Int = 0,
    val id_users: Int,
    val id_penyewaan: Int,
    val masalah: String,
    val media: String?,
    val tanggal_laporan: String
)

