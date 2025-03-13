package com.copay.app.dto.request

/**
 * Data class representing the request body for user login.
 */

data class LoginRequest(
    val phoneNumber: String,
    val password: String
)
