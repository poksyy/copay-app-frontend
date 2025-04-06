package com.copay.app.repository

import android.content.Context
import android.util.Log
import com.copay.app.config.ApiService
import com.copay.app.dto.request.UserLoginRequestDTO
import com.copay.app.dto.request.UserRegisterStepOneDTO
import com.copay.app.dto.request.UserRegisterStepTwoDTO
import com.copay.app.service.AuthService
import com.copay.app.utils.DataStoreManager
import com.copay.app.utils.state.AuthState
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.first
import retrofit2.Response

/*
UserRepository is responsible for managing user authentication operations by interacting with the AuthService.
It makes API calls for login, registration steps, and handles responses by processing success and error cases.
It also extracts the authentication token from the response and securely stores it using the DataStoreManager.
This class encapsulates the logic of managing user authentication state and storing the token, which is crucial for maintaining user sessions.
*/
class UserRepository(private val authService: AuthService) {

    // Login method.
    suspend fun login(
        context: Context,
        phoneNumber: String,
        password: String
    ): AuthState {

        val request = UserLoginRequestDTO(phoneNumber, password)
        return handleApiResponse(context) { authService.login(request) }
    }

    // Registration step one method
    suspend fun registerStepOne(
        context: Context,
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): AuthState {

        val request = UserRegisterStepOneDTO(username, email, password, confirmPassword)
        return handleApiResponse(context) { authService.registerStepOne(request) }
    }

    // Registration step two method.
    suspend fun registerStepTwo(
        context: Context,
        phoneNumber: String
    ): AuthState {

        // Get the token generated in registerStepOne thanks to DataStoreManager.
        val token = DataStoreManager.getToken(context).first()
        Log.d("UserRepository", "Token sent to registerStepTwo: $token")

        // Verifies if token is null.
        if (token.isNullOrEmpty()) {
            return AuthState.Error("Token not found.")
        }

        // Send the token with "Bearer " since the backend needs that format.
        val formattedToken = "Bearer $token"

        val request = UserRegisterStepTwoDTO(phoneNumber)

        return handleApiResponse(context) {
            authService.registerStepTwo(request, formattedToken)
        }
    }

    // Logic to manage the API Response.
    private suspend fun <T> handleApiResponse(
        context: Context,
        apiCall: suspend () -> Response<T>
    ): AuthState {

        return try {
            val response = apiCall()

            // Triggers if backend HTTP response is successful (200/201/300...)
            if (response.isSuccessful) {
                val body = response.body()

                // Triggers if the backend response is not null.
                if (body != null) {
                    // Extract the token from the backend response.
                    val token = extractToken(body)

                    // Triggers when backend response has a token and stores it trough DataStore.
                    if (token != null) {
                        // Log the token.
                        Log.d("UserRepository", "Token extracted: $token")

                        // Save the token if it exists.
                        DataStoreManager.saveToken(context, token)

                    } else {
                        Log.d("UserRepository", "No token found in response")
                    }

                    // Returns success if the response from the backend is valid.
                    AuthState.Success(body)

                } else {
                    AuthState.Error("Empty response")
                }
            // Triggers when the response is not successful.
            } else {
                // Extract the message field trough the extractErrorMessage() method.
                val errorBody = response.errorBody()?.string()
                val message = extractErrorMessage(errorBody)
                Log.d("UserRepository","ERROR STRUCTURE: $errorBody")
                // Updates the UI with an error message.
                AuthState.Error(message ?: "Unknown error")
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Connection error: ${e.message}")
            AuthState.Error("Connection error: ${e.message}")
        }
    }

    // Method to extract the token from the response.
    private fun <T> extractToken(responseBody: T?): String? {
        return try {
            // Verify if body response is not null.
            responseBody?.let {
                val field = it.javaClass.getDeclaredField("token")
                field.isAccessible = true
                field.get(it) as? String
            }
            // Returns null if body is null.
        } catch (e: Exception) {
            null
        }
    }

    private fun extractErrorMessage(errorJson: String?): String? {

        // Returns if message is null.
        if (errorJson.isNullOrEmpty()) return null

        val gson = Gson()

        return try {
            // Converts to JsonObject thanks to Gson.
            val jsonObject = gson.fromJson(errorJson, JsonObject::class.java)

            // Extracts the 'message' field from the Json.
            jsonObject.get("message")?.asString
        } catch (e: Exception) {
            // Returns null if the parse fails.
            null
        }
    }
}
