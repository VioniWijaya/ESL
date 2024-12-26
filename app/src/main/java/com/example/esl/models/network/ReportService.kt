package com.example.esl.models.network

import java.text.SimpleDateFormat
import java.util.*

// Data class untuk laporan
data class Report(
    val idLaporan: Int,
    val id_penyewaan: Int,
    val media: String,
    val masalah: String,
    val tanggalLaporan: String
)

object ReportService {
    // Fungsi untuk menyimpan laporan
    fun saveReport(report: Report) {
        // Implementasikan penyimpanan ke database lokal atau API server
        println("Laporan disimpan: $report")
    }

    // Fungsi untuk mendapatkan tanggal saat ini
    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}
