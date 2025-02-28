package com.copay.app.config

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Base URL for API calls.
    private const val BASE_URL = "http://10.0.2.2:8080"

    // Create an OkHttpClient with logging interceptor for debugging.
    private val client by lazy {
        val logging = HttpLoggingInterceptor().apply {
            // Log full request/response body for debugging.
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            // Add logging interceptor to the HTTP client.
            .addInterceptor(logging)
            .build()
    }

    // Retrofit instance with base URL, Gson converter, and OkHttpClient.
    val api: ApiService by lazy {
        Retrofit.Builder()
            // Set the base URL for Retrofit.
            .baseUrl(BASE_URL)
            // Add Gson converter for JSON to object mapping.
            .addConverterFactory(GsonConverterFactory.create())
            // Use the OkHttpClient with Logcat.
            .client(client)
            .build()

            // Create ApiService interface for API endpoints.
            .create(ApiService::class.java)
    }
}
