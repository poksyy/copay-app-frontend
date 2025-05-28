package com.copay.app.dto.auth.request

/**
 * Data class representing the request body for user registration in STEP TWO.
 */
data class UserRegisterStepTwoDTO(
    val phonePrefix: String,
    val phoneNumber: String
)
