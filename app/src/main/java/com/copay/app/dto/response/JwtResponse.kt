package com.copay.app.dto.response

/**
 * Data class representing the response received from the login API and register API.
 */

data class JwtResponse(
    val token: String? = null,      // JWT Token.
    val expiresIn: Int? = null,     // Token expiration in seconds.
    val type: String? = null,        // Token type Bearer.
    val message: String? = null
)
