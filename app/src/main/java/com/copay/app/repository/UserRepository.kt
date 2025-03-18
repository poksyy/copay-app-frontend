package com.copay.app.repository

import android.util.Log
import com.copay.app.config.ApiService
import com.copay.app.dto.request.LoginRequest
import com.copay.app.dto.request.RegisterRequest
import com.copay.app.dto.response.JwtResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserRepository(private val apiService: ApiService) {

    suspend fun login(phoneNumber: String, password: String): Response<JwtResponse> {
        return withContext(Dispatchers.IO) {
            val response = apiService.loginUser(LoginRequest(phoneNumber, password))
            Log.d("UserRepository", "Login response: ${response.code()} - ${response.message()}")

            response.body()?.let {
                Log.d(
                    "UserRepository",
                    "Token received: ${it.token}, Expires in: ${it.expiresIn} seconds"
                )
            } ?: Log.d("UserRepository", "Login failed: No token received")

            response
        }
    }

    suspend fun register(
        username: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ): Response<JwtResponse> {
        val response = apiService.registerUser(
            RegisterRequest(
                username, email, phoneNumber, password, confirmPassword
            )
        )
        Log.d("UserRepository", "Register response: ${response.code()} - ${response.message()}")

        // If registration is successful, return a normal response.
        if (response.isSuccessful) {
            return response
        }

        // If the response is an error, it parses the 'JwtResponse' using Gson (converting the JSON into an object in Kotlin).
        val errorBody = response.errorBody()?.string()
        val errorResponse = try {
            Gson().fromJson(errorBody, JwtResponse::class.java)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error parsing error response: ${e.message}")
            JwtResponse(message = "Unknown error", token = null, expiresIn = null, type = null)
        }

        Log.e("UserRepository", "Registration failed: ${errorResponse.message}")

        return Response.success(errorResponse)
    }
}
