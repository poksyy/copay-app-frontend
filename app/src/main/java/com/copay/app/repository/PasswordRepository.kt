package com.copay.app.repository

import com.copay.app.config.ApiService
import com.copay.app.dto.request.ForgotPasswordDTO
import retrofit2.Response

class PasswordRepository(private val api: ApiService) {

    suspend fun forgotPassword(request: ForgotPasswordDTO): Response<Unit> {
        return api.forgotPassword(request)
    }
}
