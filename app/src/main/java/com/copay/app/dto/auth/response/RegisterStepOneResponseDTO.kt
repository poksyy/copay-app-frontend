package com.copay.app.dto.auth.response

/**
 * Data class representing the response received from the login API and register API.
 */

data class RegisterStepOneResponseDTO(
    // JWT Token.
    val token: String? = null,
    // Token expiration in seconds.
    val expiresIn: Int? = null,

    // Registered (temporally) user details.
    val username: String? = null,
    val email: String? = null,

    // Message in case of any error in the backend.
    val message: String? = null
)