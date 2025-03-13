package com.copay.app.dto.response

/**
 * Data class representing the response received from the login API and register API.
 */

data class JwtResponse(
    val token: String,      // JWT Token.
    val expiresIn: Int,     // Token expiration in seconds.
    val type: String        // Token type Bearer.
)
