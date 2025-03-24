package com.copay.app.repository

import android.content.Context
import android.util.Log
import com.copay.app.config.ApiService
import com.copay.app.dto.request.UserLoginRequestDTO
import com.copay.app.dto.request.UserRegisterStepOneDTO
import com.copay.app.dto.request.UserRegisterStepTwoDTO
import com.copay.app.dto.response.JwtResponse
import com.copay.app.utils.DataStoreManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserRepository(private val apiService: ApiService) {

    suspend fun login(phoneNumber: String, password: String): Response<JwtResponse> {
        return withContext(Dispatchers.IO) {
            val response = apiService.loginUser(UserLoginRequestDTO(phoneNumber, password))
            Log.d("UserRepository", "Login: ${response.code()} - ${response.message()}")

            response.body()?.let { Log.d("UserRepository", "Token: ${it.token}") }
                ?: Log.d("UserRepository", "Login failed: No token received")

            response
        }
    }

    suspend fun registerStepOne(
        username: String, email: String, password: String, confirmPassword: String
    ): Response<JwtResponse> {
        Log.d("mic","UserRepository1")
        val response = apiService.registerStepOne(
            UserRegisterStepOneDTO(username, email, password, confirmPassword)
        )
        Log.d("UserRepository", "RegisterStepOne: ${response.code()} - ${response.message()}")

        // If registration is successful, return a normal response.
        return if (response.isSuccessful) {
            response
        } else {
            Response.success(handleErrorResponse(response))
        }
    }

    suspend fun registerStepTwo(context: Context, phoneNumber: String): Response<JwtResponse> {

        val token = DataStoreManager.getToken(context).firstOrNull() ?: ""

        Log.d("UserRepository", "Token: $token")

        val response = apiService.registerStepTwo(
            UserRegisterStepTwoDTO(phoneNumber), "Bearer $token"
        )
        Log.d("UserRepository", "RegisterStepTwo: ${response.code()} - ${response.message()}")

        // If registration is successful, return a normal response.
        return if (response.isSuccessful) {
            response
        } else {
            Response.success(handleErrorResponse(response))
        }
    }

    // If the response is an error, it parses the 'JwtResponse' using Gson (converting the JSON into an object in Kotlin).
    private fun <T> handleErrorResponse(response: Response<T>): JwtResponse {
        val errorBody = response.errorBody()?.string()
        val errorResponse = try {
            Gson().fromJson(errorBody, JwtResponse::class.java)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error parsing error response: ${e.message}")
            JwtResponse(message = "Unknown error", token = null, expiresIn = null, type = null)
        }
        Log.e("UserRepository", "Request failed: ${errorResponse.message}")
        return errorResponse
    }
}
