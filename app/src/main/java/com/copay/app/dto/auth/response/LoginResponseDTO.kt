package com.copay.app.dto.auth.response

/**
 * Data class representing the response received from the login API.
 */

data class LoginResponseDTO(
    // JWT Token.
    val token: String? = null,
    // Token expiration in seconds.
    val expiresIn: Int? = null,

    // Logged user details.
    val userId: Long? = null,
    val phoneNumber: String? = null,
    val username: String? = null,
    val email: String? = null,
    val isLogin: String?= null,

    // Message in case of any error in the backend.
    val message: String? = null
)
