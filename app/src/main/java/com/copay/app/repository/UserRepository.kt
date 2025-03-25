package com.copay.app.repository

import android.content.Context
import com.copay.app.config.ApiService
import com.copay.app.dto.request.UserLoginRequestDTO
import com.copay.app.dto.request.UserRegisterStepOneDTO
import com.copay.app.dto.request.UserRegisterStepTwoDTO
import com.copay.app.dto.response.JwtResponse
import com.copay.app.service.ResponseHandler
import com.copay.app.utils.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import retrofit2.Response

// Repository that handles user authentication and registration with the API.
class UserRepository(private val apiService: ApiService) {

    // Function to handle user login. It sends the phone number and password to the API.
    suspend fun login(phoneNumber: String, password: String): Response<JwtResponse> {

        // Use IO dispatcher to make the network request in a background thread.
        return withContext(Dispatchers.IO) {
            val response = apiService.loginUser(UserLoginRequestDTO(phoneNumber, password))
            if (response.isSuccessful) {
                response
            } else {
                Response.success(ResponseHandler.handleErrorResponse(response))
            }
        }
    }

    // Function to handle the first step of the registration process.
    suspend fun registerStepOne(username: String, email: String, password: String, confirmPassword: String): Response<JwtResponse> {

        // Call API for step one of registration with the provided details.
        val response = apiService.registerStepOne(UserRegisterStepOneDTO(username, email, password, confirmPassword))

        return if (response.isSuccessful) {
            response
        } else {
            Response.success(ResponseHandler.handleErrorResponse(response))
        }
    }

    // Function to handle the second step of the registration process, which includes sending the phone number.
    suspend fun registerStepTwo(context: Context, phoneNumber: String): Response<JwtResponse> {

        // Retrieve the token from DataStore (firstOrNull to avoid null value).
        val token = DataStoreManager.getToken(context).firstOrNull() ?: ""

        // Call API for step two of registration, passing the phone number and the token in the header.
        val response = apiService.registerStepTwo(UserRegisterStepTwoDTO(phoneNumber), "Bearer $token")

        return if (response.isSuccessful) {
            response
        } else {
            Response.success(ResponseHandler.handleErrorResponse(response))
        }
    }
}
