package com.copay.app.repository

import android.util.Log
import com.copay.app.config.ApiService
import com.copay.app.dto.request.LoginRequest
import com.copay.app.dto.request.RegisterRequest
import com.copay.app.dto.response.JwtResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserRepository(private val apiService: ApiService) {

    suspend fun login(phoneNumber: String, password: String): Response<JwtResponse> {
        return withContext(Dispatchers.IO) {
            val response = apiService.loginUser(LoginRequest(phoneNumber, password))
            Log.d("UserRepository", "Login response: ${response.code()} - ${response.message()}")
            response.body()?.let {
                Log.d("UserRepository", "Token received: ${it.token}, Expires in: ${it.expiresIn} seconds")
            } ?: Log.d("UserRepository", "Login failed: No token received")

            response
        }
    }

    suspend fun register(username: String, email: String, phoneNumber: String, password: String, confirmPassword: String): Response<JwtResponse> {
        return withContext(Dispatchers.IO) {
            val response = apiService.registerUser(RegisterRequest(username, email, phoneNumber, password, confirmPassword))
            Log.d("UserRepository", "Register response: ${response.code()} - ${response.message()}")
            response.body()?.let {
                Log.d("UserRepository", "Token received: ${it.token}, Expires in: ${it.expiresIn} seconds")
            } ?: Log.d("UserRepository", "Register failed: No token received")
            response
        }
    }
}
