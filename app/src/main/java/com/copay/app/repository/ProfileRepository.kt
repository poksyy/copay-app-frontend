package com.copay.app.repository

import android.content.Context
import android.util.Log
import com.copay.app.dto.request.profile.UpdateEmailDTO
import com.copay.app.dto.request.profile.UpdatePasswordDTO
import com.copay.app.dto.request.profile.UpdatePhoneNumberDTO
import com.copay.app.dto.request.profile.UpdateUsernameDTO
import com.copay.app.service.ProfileService
import com.copay.app.utils.DataStoreManager
import com.copay.app.utils.state.ProfileState
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.first
import retrofit2.Response

/*
ProfileRepository is responsible for managing profile update operations by interacting with the ProfileService.
It makes API calls for updating username, phone number, and email, and handles responses by processing success and error cases.
This class encapsulates the logic of managing profile update operations, ensuring that the UI receives the appropriate state.
*/

class ProfileRepository(private val profileService: ProfileService) {

    // Updates the username of the user.
    suspend fun updateUsername(
        context: Context,
        userId: Long,
        newUsername: String
    ): ProfileState {

        val request = UpdateUsernameDTO(userId = userId, username = newUsername)
        val result = handleApiResponse(context) {
            profileService.updateUsername(request)
        }

        // Handles the result of the API call and returns the appropriate ProfileState.
        return when (result) {
            is ProfileState.Success -> ProfileState.Success.UsernameUpdated(result.data)
            else -> result
        }
    }

    // Updates the phone number of the user.
    suspend fun updatePhoneNumber(
        context: Context,
        userId: Long,
        newPhoneNumber: String
    ): ProfileState {

        val request = UpdatePhoneNumberDTO(userId = userId, phoneNumber = newPhoneNumber)
        val result = handleApiResponse(context) {
            profileService.updatePhoneNumber(request)
        }

        // Handles the result of the API call and returns the appropriate ProfileState.
        return when (result) {
            is ProfileState.Success -> ProfileState.Success.PhoneUpdated(result.data)
            else -> result
        }
    }

    // Updates the email of the user.
    suspend fun updateEmail(
        context: Context,
        userId: Long,
        newEmail: String
    ): ProfileState {

        val request = UpdateEmailDTO(userId = userId, email = newEmail)
        val result = handleApiResponse(context) {
            profileService.updateEmail(request)
        }

        // Handles the result of the API call and returns the appropriate ProfileState.
        return when (result) {
            is ProfileState.Success -> ProfileState.Success.EmailUpdated(result.data)
            else -> result
        }
    }

    // Updates the password of the user
    suspend fun updatePassword(
        context: Context,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): ProfileState {

        val request = UpdatePasswordDTO(
            currentPassword = currentPassword,
            newPassword = newPassword,
            confirmNewPassword = confirmNewPassword
        )

        // Get the token generated in registerStepOne thanks to DataStoreManager.
        val token = DataStoreManager.getToken(context).first()

        // Send the token with "Bearer " since the backend needs that format.
        val formattedToken = "Bearer $token"

        val result = handleApiResponse(context) {
            profileService.updatePassword(request, formattedToken)
        }

        // Handles the result of the API call and returns the appropriate ProfileState.
        return when (result) {
            is ProfileState.Success -> ProfileState.Success.PasswordUpdated(result.data)
            else -> result
        }
    }

    // Handles the API response for the profile update operations.
    private suspend fun <T> handleApiResponse(
        context: Context,
        apiCall: suspend () -> Response<T>
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