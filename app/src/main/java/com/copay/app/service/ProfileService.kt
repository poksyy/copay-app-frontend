package com.copay.app.service

import android.util.Log
import com.copay.app.config.ApiService
import com.copay.app.dto.request.profile.UpdateEmailDTO
import com.copay.app.dto.request.profile.UpdatePasswordDTO
import com.copay.app.dto.request.profile.UpdatePhoneNumberDTO
import com.copay.app.dto.request.profile.UpdateUsernameDTO
import com.copay.app.dto.response.profile.EmailResponseDTO
import com.copay.app.dto.response.profile.PasswordResponseDTO
import com.copay.app.dto.response.profile.PhoneNumberResponseDTO
import com.copay.app.dto.response.profile.UsernameResponseDTO
import retrofit2.Response

class ProfileService(private val api: ApiService) {

    suspend fun updateUsername(request: UpdateUsernameDTO): Response<UsernameResponseDTO> {
        return api.updateUsername(request.userId, request)
    }

    suspend fun updatePhoneNumber(request: UpdatePhoneNumberDTO): Response<PhoneNumberResponseDTO> {
        return api.updatePhoneNumber(request.userId, request)
    }

    suspend fun updateEmail(request: UpdateEmailDTO): Response<EmailResponseDTO> {
        return api.updateEmail(request.userId, request)
    }

    suspend fun updatePassword(request: UpdatePasswordDTO, token: String): Response<PasswordResponseDTO> {
        Log.d("DataStoreManager","update password SENDING TOKEN TO BACKEND $token")
        return api.updatePassword(request, token)
    }
}
