package com.copay.app.dto.auth.request

/**
 * Data class representing the request body for user registration in STEP ONE.
 */
data class UserRegisterStepOneDTO(
    val username: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)
