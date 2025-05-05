package com.copay.app.repository

import com.copay.app.config.ApiService
import com.copay.app.dto.password.ForgotPasswordDTO
import retrofit2.Response

/**
 * PasswordRepository handles operations related to password management, such as the "forgot password" feature.
 * It delegates API calls to ApiService and abstracts the request logic from the UI layer.
 */

class PasswordRepository(private val api: ApiService) {

    // Sends a forgot password request to the backend using the provided email.
    suspend fun forgotPassword(request: ForgotPasswordDTO): Response<Unit> {
        return api.forgotPassword(request)
    }
}
