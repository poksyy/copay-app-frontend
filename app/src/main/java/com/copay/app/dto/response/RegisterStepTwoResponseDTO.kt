package com.copay.app.dto.response

/**
 * Data class representing the response received from the login API.
 */

data class RegisterStepTwoResponseDTO(
    // JWT Token.
    val token: String? = null,
    // Token expiration in seconds.
    val expiresIn: Int? = null,

    // Registered user details.
    val userId: Long? = null,
    val phoneNumber: String? =null,
    val username: String? = null,
    val email: String? = null,

    // Message in case of any error in the backend.
    val message: String? = null
)
