package com.copay.app.service

import android.util.Log
import com.copay.app.config.ApiService
import com.copay.app.dto.profile.request.UpdateEmailDTO
import com.copay.app.dto.profile.request.UpdatePasswordDTO
import com.copay.app.dto.profile.request.UpdatePhoneNumberDTO
import com.copay.app.dto.profile.request.UpdateUsernameDTO
import com.copay.app.dto.profile.response.EmailResponseDTO
import com.copay.app.dto.profile.response.PasswordResponseDTO
import com.copay.app.dto.profile.response.PhoneNumberResponseDTO
import com.copay.app.dto.profile.response.UsernameResponseDTO
import com.copay.app.dto.user.UserResponseDTO
import retrofit2.Response

/**
 * ProfileService handles all profile-related operations such as updating
 * user information like username, phone number, email, and password.
 * It serves as a clean interface between the ViewModel and ApiService.
 */

class ProfileService(private val api: ApiService) {

    // Sends a request to get a specific user's data.
    suspend fun getUserByPhone(phoneNumber: String, token: String): Response<UserResponseDTO> {
        return api.getUserByPhone(phoneNumber, token)
    }

    // Sends a request to update the user's username.
    suspend fun updateUsername(userId: Long, request: UpdateUsernameDTO, token: String): Response<UsernameResponseDTO> {
        return api.updateUsername(userId, request, token)
    }

    // Sends a request to update the user's phone number.
    suspend fun updatePhoneNumber(userId: Long, request: UpdatePhoneNumberDTO, token: String): Response<PhoneNumberResponseDTO> {
        return api.updatePhoneNumber(userId, request, token)
    }

    // Sends a request to update the user's email address.
    suspend fun updateEmail(userId: Long, request: UpdateEmailDTO, token: String): Response<EmailResponseDTO> {
        return api.updateEmail(userId, request, token)
    }

    // Sends a request to update the user's password using a JWT token for authentication.
    suspend fun updatePassword(
        request: UpdatePasswordDTO, token: String
    ): Response<PasswordResponseDTO> {
        return api.updatePassword(request, token)
    }
}
