package com.copay.app.service

import android.util.Log
import com.copay.app.config.ApiService
import com.copay.app.dto.auth.request.UserGoogleLoginRequestDTO
import com.copay.app.dto.auth.request.UserLoginRequestDTO
import com.copay.app.dto.auth.request.UserRegisterStepOneDTO
import com.copay.app.dto.auth.request.UserRegisterStepTwoDTO
import com.copay.app.dto.auth.response.LoginResponseDTO
import com.copay.app.dto.auth.response.RegisterStepOneResponseDTO
import com.copay.app.dto.auth.response.RegisterStepTwoResponseDTO
import retrofit2.Response

/**
 * AuthService encapsulates all authentication-related operations.
 * It acts as an abstraction layer between the ViewModel and ApiService,
 * ensuring that all auth requests are organized and reusable.
 */

class AuthService(private val api: ApiService) {

    // Sends a login request with user credentials and returns a token if successful.
    suspend fun login(request: UserLoginRequestDTO): Response<LoginResponseDTO> {
        return api.loginUser(request)
    }

    // Sends a google login request with user credentials and returns a token if successful.
    suspend fun loginGoogle(
        request: UserGoogleLoginRequestDTO
    ): Response<LoginResponseDTO> {
        return api.loginUserWithGoogle(request)
    }

    // Sends the first step of registration (e.g., email and username validation).
    suspend fun registerStepOne(request: UserRegisterStepOneDTO): Response<RegisterStepOneResponseDTO> {
        return api.registerStepOne(request)
    }

    // Completes registration by sending password and additional data, along with a token.
    suspend fun registerStepTwo(
        request: UserRegisterStepTwoDTO, token: String
    ): Response<RegisterStepTwoResponseDTO> {
        return api.registerStepTwo(request, token)
    }

    // Sends a logout request to invalidate the current session token.
    suspend fun logout(token: String): Response<Unit> {
        return api.logout(token)
    }
}
