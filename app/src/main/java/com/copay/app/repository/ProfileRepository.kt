package com.copay.app.repository

import android.content.Context
import android.util.Log
import com.copay.app.dto.profile.request.UpdateEmailDTO
import com.copay.app.dto.profile.request.UpdatePasswordDTO
import com.copay.app.dto.profile.request.UpdatePhoneNumberDTO
import com.copay.app.dto.profile.request.UpdateUsernameDTO
import com.copay.app.service.ProfileService
import com.copay.app.utils.DataStoreManager
import com.copay.app.utils.session.UserSession
import com.copay.app.utils.state.ProfileState
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.first
import retrofit2.Response

/**
 * ProfileRepository is responsible for managing profile update operations by interacting with the ProfileService.
 * It makes API calls for updating username, phone number, and email, and handles responses by processing success and error cases.
 * This class encapsulates the logic of managing profile update operations, ensuring that the UI receives the appropriate state.
 */

class ProfileRepository(
    private val profileService: ProfileService,
    private val userSession: UserSession
) {

    // Updates the username of the user.
    suspend fun updateUsername(
        context: Context, newUsername: String
    ): ProfileState {

        // Extract userId through user session.
        val user = userSession.user.first()
        val userId = user?.userId ?: return ProfileState.Error("User not logged in")

        val token = DataStoreManager.getFormattedToken(context)

        val request = UpdateUsernameDTO(username = newUsername)
        val result = handleApiResponse(context) {
            profileService.updateUsername(userId, request, token)
        }

        // Handles the result of the API call and returns the appropriate ProfileState.
        return when (result) {
            is ProfileState.Success -> ProfileState.Success.UsernameUpdated(result.data)
            else -> result
        }
    }

    // Updates the phone number of the user.
    suspend fun updatePhoneNumber(
        context: Context, newPhoneNumber: String
    ): ProfileState {

        // Extract userId through user session.
        val user = userSession.user.first()
        val userId = user?.userId ?: return ProfileState.Error("User not logged in")

        val token = DataStoreManager.getFormattedToken(context)

        val request = UpdatePhoneNumberDTO(phoneNumber = newPhoneNumber)
        val result = handleApiResponse(context) {
            profileService.updatePhoneNumber(userId, request, token)
        }

        // Handles the result of the API call and returns the appropriate ProfileState.
        return when (result) {
            is ProfileState.Success -> ProfileState.Success.PhoneUpdated(result.data)
            else -> result
        }
    }

    // Updates the email of the user.
    suspend fun updateEmail(
        context: Context, newEmail: String
    ): ProfileState {

        // Extract userId through user session.
        val user = userSession.user.first()
        val userId = user?.userId ?: return ProfileState.Error("User not logged in")

        val token = DataStoreManager.getFormattedToken(context)

        val request = UpdateEmailDTO(email = newEmail)
        val result = handleApiResponse(context) {
            profileService.updateEmail(userId, request, token)
        }

        // Handles the result of the API call and returns the appropriate ProfileState.
        return when (result) {
            is ProfileState.Success -> ProfileState.Success.EmailUpdated(result.data)
            else -> result
        }
    }

    // Updates the password of the user
    suspend fun updatePassword(
        context: Context, currentPassword: String, newPassword: String, confirmNewPassword: String
    ): ProfileState {

        val request = UpdatePasswordDTO(
            currentPassword = currentPassword,
            newPassword = newPassword,
            confirmNewPassword = confirmNewPassword
        )

        val token = DataStoreManager.getFormattedToken(context)

        val result = handleApiResponse(context) {
            profileService.updatePassword(request, token)
        }

        // Handles the result of the API call and returns the appropriate ProfileState.
        return when (result) {
            is ProfileState.Success -> ProfileState.Success.PasswordUpdated(result.data)
            else -> result
        }
    }

    // Handles the API response for the profile update operations.
    private suspend fun <T> handleApiResponse(
        context: Context, apiCall: suspend () -> Response<T>
    ): ProfileState {

        return try {
            val response = apiCall()

            // If response is successful, process body.
            if (response.isSuccessful) {
                ProfileState.Success(response.body())
            } else {
                // If the response is an error, parse and return the error message.
                val errorBody = response.errorBody()?.string()
                val message = extractErrorMessage(errorBody)
                ProfileState.Error(message ?: "Unknown error")
            }
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Connection error: ${e.message}")
            ProfileState.Error("Connection error: ${e.message}")
        }
    }

    private fun extractErrorMessage(errorJson: String?): String? {

        // Returns if message is null.
        if (errorJson.isNullOrEmpty()) return null

        return try {
            val jsonObject = Gson().fromJson(errorJson, JsonObject::class.java)
            jsonObject.get("message")?.asString
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Error parsing error message: ${e.message}")
            null
        }
    }
}