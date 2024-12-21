package com.example.esl.models.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "properti")
data class Property(
    @PrimaryKey(autoGenerate = true) val id_properti: Int = 0,
    val nama_properti: String,
    val jenis_properti: String, // Enum: Kendaraan, Rumah
    val deskripsi: String,
    val pemilik: String,
    val hargaSewa: Int,
    val lokasi: String,
    val lang: Double,
    val long: Double,
    val status_properti: Boolean,
    val foto_properti: String?
)
