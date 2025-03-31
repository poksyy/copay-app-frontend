package com.copay.app.dto.response

/**
 * Data class representing the response received from the login API.
 */

data class LoginResponseDTO(
    // JWT Token.
    val token: String? = null,
    // Token expiration in seconds.
    val expiresIn: Int? = null,
    // Token should be always Bearer.
    val type: String? = null,


    // Logged user details.
    val username: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val isLogin: String?= null,

    // Message in case of any error in the backend.
    val message: String? = null
)
