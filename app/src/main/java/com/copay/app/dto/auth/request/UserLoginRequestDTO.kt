package com.copay.app.dto.auth.request

/**
 * Data class representing the request body for user login.
 */
data class UserLoginRequestDTO(
    val phoneNumber: String,
    val password: String
)
