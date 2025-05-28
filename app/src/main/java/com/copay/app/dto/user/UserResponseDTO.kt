package com.copay.app.dto.user

/**
 * Data class representing user information in a response.
 */
data class UserResponseDTO(
    val id: Long,
    val username: String,
    val email: String,
    val phonePrefix: String,
    val phoneNumber: String,

    // Message in case of any error in the backend.
    val message: String? = null
)
