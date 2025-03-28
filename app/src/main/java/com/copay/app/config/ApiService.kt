package com.copay.app.config

import com.copay.app.dto.request.ForgotPasswordDTO
import com.copay.app.dto.request.UserLoginRequestDTO
import com.copay.app.dto.request.UserRegisterStepTwoDTO
import com.copay.app.dto.request.UserRegisterStepOneDTO
import com.copay.app.dto.response.JwtResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
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
    suspend fun loginUser(@Body request: UserLoginRequestDTO): Response<JwtResponse>

    // Register Step One API Call
    @POST("${BASE_PATH}auth/register/step-one")
    suspend fun registerStepOne(@Body request: UserRegisterStepOneDTO): Response<JwtResponse>

    // Register Step Two API Call
    @POST("${BASE_PATH}auth/register/step-two")
    suspend fun registerStepTwo(
        @Body request: UserRegisterStepTwoDTO,
        @Header("Authorization") token: String
    ): Response<JwtResponse>

    // Forgot Password API Call
    @POST("${BASE_PATH}forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordDTO): Response<Unit>

}
