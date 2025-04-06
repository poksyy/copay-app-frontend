package com.copay.app.config

import com.copay.app.service.AuthService
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
    private val retrofit by lazy {
        Retrofit.Builder()
            // Set the base URL for Retrofit.
            .baseUrl(BASE_URL)
            // Add Gson converter for JSON to object mapping.
            .addConverterFactory(GsonConverterFactory.create())
            // Use the OkHttpClient with Logcat.
            .client(client)
            .build()
    }

    // Create the ApiService instance trough retrofit.
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
