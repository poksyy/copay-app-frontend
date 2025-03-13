package com.copay.app.config

import com.copay.app.dto.request.LoginRequest
import com.copay.app.dto.request.RegisterRequest
import com.copay.app.dto.response.JwtResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    companion object {
        private const val BASE_PATH = "api/"
    }

    // Method to test the connection with the client.
    @GET("${BASE_PATH}response")
    suspend fun getConnectionResponse(): Response<ResponseBody>

    // Login API Call
    @POST("${BASE_PATH}auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<JwtResponse>

    // Register API Call
    @POST("${BASE_PATH}auth/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<JwtResponse>
}
