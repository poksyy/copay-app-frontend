package com.copay.app.dto.auth.request

/**
 * Data class representing the request body for user login with google account.
 */

data class UserGoogleLoginRequestDTO(
    val idToken: String
)
