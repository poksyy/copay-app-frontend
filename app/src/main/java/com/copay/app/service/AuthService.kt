package com.copay.app.service

import android.content.Context
import com.copay.app.dto.response.JwtResponse
import com.copay.app.utils.DataStoreManager
import com.copay.app.utils.state.AuthState
import retrofit2.Response

/**
 * AuthService is responsible for managing authentication-related API calls.
 * It provides functions to perform user login, registration, and other
 * authentication actions. This class interacts with the backend to verify
 * credentials, manage JWT tokens, and handle session persistence.
 * It acts as the main service for user authentication within the app.
 */


object AuthService {

    // Handles the authentication response from the API
    suspend fun handleResponse(context: Context, response: Response<JwtResponse>): AuthState {

        val jwtResponse = response.body()

        return if (jwtResponse != null && response.isSuccessful && jwtResponse.token != null) {

            // If the response is successful and contains a token, save it.
            DataStoreManager.saveToken(context, jwtResponse.token)
            AuthState.Success

        } else {

            // If the response fails, update state with an error message.
            val errorMessage = jwtResponse?.message ?: "Request failed"
            AuthState.Error(errorMessage)
        }
    }
}
