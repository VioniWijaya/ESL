package com.example.esl.models.network

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File
import java.io.IOException

data class Report(
    val idLaporan: Int,
    val id_penyewaan: Int,
    val media: String,
    val masalah: String,
)

object ReportService {

    private const val SERVER_URL = "http://192.168.233.66:3000/"

    // Retrofit API Interface
    interface ReportApi {
        @Multipart
        @POST("api/problem/add/{id_penyewaan}")
        fun submitReport(
            @Path("id_penyewaan") id_penyewaan: Int,
            @Header("Authorization") token: String,
            @Part("masalah") masalah: RequestBody,
            @Part file: MultipartBody.Part?
        ): Call<Any>
    }

    // Retrofit Instance
    object RetrofitInstance {
        private const val BASE_URL = SERVER_URL

        val api: ReportApi by lazy {
            val client = OkHttpClient.Builder().build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(ReportApi::class.java)
        }
    }

    // Fungsi untuk mendapatkan path dari URI
    private fun getPathFromUri(context: Context, uri: Uri): String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return filePath ?: ""
    }

    // Fungsi untuk menyimpan laporan menggunakan Retrofit
    fun saveReportUsingRetrofit(report: Report, token: String?, context: Context, mediaUri: Uri?) {
        val filePart = mediaUri?.let {
            val file = File(getPathFromUri(context, it))

            // Tentukan tipe MIME yang lebih spesifik, misalnya image/jpeg atau image/png
            val mimeType = when (file.extension.lowercase()) {
                "jpg", "jpeg" -> "image/jpeg"
                "png" -> "image/png"
                else -> "image/*" // default fallback jika tipe file tidak diketahui
            }

            val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
            MultipartBody.Part.createFormData("file", file.name, requestBody)
        }

        val masalahBody = RequestBody.create("text/plain".toMediaTypeOrNull(), report.masalah)

        val call = RetrofitInstance.api.submitReport(
            report.id_penyewaan,
            "Bearer $token",
            masalahBody,
            filePart
        )

        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    println("Laporan berhasil dikirim")
                } else {
                    println("Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                println("Error: ${t.message}")
            }
        })
    }
}