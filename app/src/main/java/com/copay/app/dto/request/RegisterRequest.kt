package com.copay.app.dto.request

/**
 * Data class representing the request body for user registration.
 */

data class RegisterRequest(
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val confirmPassword: String
)
