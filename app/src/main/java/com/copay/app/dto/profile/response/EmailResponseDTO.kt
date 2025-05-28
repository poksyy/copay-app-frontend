package com.copay.app.dto.profile.response

/**
 * Data class representing a response after an email update operation.
 */
data class EmailResponseDTO(
    val id: Long,
    val email: String,
    val message: String? = null
)
