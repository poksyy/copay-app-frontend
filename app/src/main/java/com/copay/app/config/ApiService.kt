package com.copay.app.config

import retrofit2.http.GET
import okhttp3.ResponseBody
import retrofit2.Response

interface ApiService {

    companion object {
        private const val BASE_PATH = "api/"
    }

    // Method to test the connection with the client.
    @GET("${BASE_PATH}response")
    suspend fun getConnectionResponse(): Response<ResponseBody>
}