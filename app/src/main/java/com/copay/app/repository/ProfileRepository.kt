package com.copay.app.repository

import android.content.Context
import android.util.Log
import com.copay.app.dto.profile.request.UpdateEmailDTO
import com.copay.app.dto.profile.request.UpdatePasswordDTO
import com.copay.app.dto.profile.request.UpdatePhoneNumberDTO
import com.copay.app.dto.profile.request.UpdateUsernameDTO
import com.copay.app.mappers.toUser
import com.copay.app.model.User
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

    // Get user by phone number.
    suspend fun userByPhone(
        context: Context, phoneNumber: String
    ): ProfileState {

        val token = DataStoreManager.getFormattedToken(context)

        Log.d("ProfileRepository", token)

        return handleApiResponse(
            context,
            apiCall = { profileService.getUserByPhone(phoneNumber, token) },
            onSuccess = { body -> ProfileState.Success.GetUser(body) }
        )
    }

    // Updates the username of the user.
    suspend fun updateUsername(
        context: Context, newUsername: String
    ): ProfileState {

        // Extract userId through user session.
        val user = userSession.user.first()
        val userId = user?.userId ?: return ProfileState.Error("User not logged in")

        val token = DataStoreManager.getFormattedToken(context)

        val request = UpdateUsernameDTO(username = newUsername)

        // Handles the result of the API call and returns the appropriate ProfileState.
        return handleApiResponse(
            context,
            apiCall = { profileService.updateUsername(userId, request, token) },
            onSuccess = { ProfileState.Success.UsernameUpdated(it) }
        )
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

        // Handles the result of the API call and returns the appropriate ProfileState.
        return handleApiResponse(
            context,
            apiCall = { profileService.updatePhoneNumber(userId, request, token) },
            onSuccess = { ProfileState.Success.PhoneUpdated(it) }
        )
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

        // Handles the result of the API call and returns the appropriate ProfileState.
        return handleApiResponse(
            context,
            apiCall = { profileService.updateEmail(userId, request, token) },
            onSuccess = { ProfileState.Success.EmailUpdated(it) }
        )
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

        // Handles the result of the API call and returns the appropriate ProfileState.
        return handleApiResponse(
            context,
            apiCall = { profileService.updatePassword(request, token) },
            onSuccess = { ProfileState.Success.PasswordUpdated(it) }
        )
    }

    suspend fun getUserByPhoneDirect(context: Context, phoneNumber: String): User? {
        val token = DataStoreManager.getFormattedToken(context)
        return try {
            val response = profileService.getUserByPhone(phoneNumber, token)
            if (response.isSuccessful) {
                response.body()?.toUser()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // Handles the API response for the profile update operations.
    private suspend fun <T> handleApiResponse(
        context: Context,
        apiCall: suspend () -> Response<T>,
        onSuccess: (T) -> ProfileState
    ): ProfileState {
        return try {
            val response = apiCall()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    onSuccess(body)
                } else {
                    ProfileState.Error("Empty response body")
                }
            } else {
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