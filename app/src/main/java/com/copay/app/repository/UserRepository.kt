package com.copay.app.repository

import android.content.Context
import com.copay.app.config.ApiService
import com.copay.app.dto.request.UserLoginRequestDTO
import com.copay.app.dto.request.UserRegisterStepOneDTO
import com.copay.app.dto.request.UserRegisterStepTwoDTO
import com.copay.app.dto.response.JwtResponse
import com.copay.app.dto.response.LoginResponseDTO
import com.copay.app.service.ErrorResponseHandler
import com.copay.app.utils.DataStoreManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

// Repository that handles user authentication and registration with the API.
class UserRepository(private val apiService: ApiService) {

    // Function to handle user login. It sends the phone number and password to the API.
    suspend fun login(phoneNumber: String, password: String): Response<LoginResponseDTO> {

        return handleApiCall {
            apiService.loginUser(
                UserLoginRequestDTO(
                    phoneNumber,
                    password
                )
            )
        }
    }

    // Function to handle the first step of the registration process.
    suspend fun registerStepOne(username: String, email: String, password: String, confirmPassword: String): Response<JwtResponse> {

        // Call API for step one of registration with the provided details of our DTO.
            return handleApiCall {
                apiService.registerStepOne(
                    UserRegisterStepOneDTO(
                        username,
                        email,
                        password,
                        confirmPassword
                    )
                )
            }
    }

    // Function to handle the second step of the registration process, which includes sending the phone number.
    suspend fun registerStepTwo(context: Context, phoneNumber: String): Response<JwtResponse> {

        // Retrieve the token from DataStore (firstOrNull to avoid null value).
        val token = DataStoreManager.getToken(context).firstOrNull()
            ?: throw IllegalStateException("No authentication token found.")

        // Call API for step two of registration, passing the phone number and the token in the header.
        return handleApiCall {
            apiService.registerStepTwo(
                UserRegisterStepTwoDTO(
                    phoneNumber
                ), "Bearer $token"
            )
        }
    }

    private suspend fun <T> handleApiCall(call: suspend () -> Response<T>): Response<T> {

        return withContext(Dispatchers.IO) {
            val response = call.invoke()

            // Returns response if successful, otherwise handles error.
            if (response.isSuccessful) {
                response
            } else {
                val errorBody = Gson().toJson(ErrorResponseHandler.handleErrorResponse(response)).toResponseBody()
                Response.error(response.code(), errorBody)
            }
        }
    }
}
