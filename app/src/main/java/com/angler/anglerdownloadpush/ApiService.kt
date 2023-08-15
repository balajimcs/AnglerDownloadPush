package com.angler.anglerdownloadpush

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming

interface ApiService {
    @GET("pdf/flutter.pdf") // Replace with the actual PDF URL
    @Streaming
    suspend fun downloadPdf(): ResponseBody
}
