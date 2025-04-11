package com.copay.app.service

import com.copay.app.config.ApiService
import com.copay.app.dto.request.profile.UpdateEmailDTO
import com.copay.app.dto.request.profile.UpdatePhoneNumberDTO
import com.copay.app.dto.request.profile.UpdateUsernameDTO
import com.copay.app.dto.response.profile.EmailResponseDTO
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
}
