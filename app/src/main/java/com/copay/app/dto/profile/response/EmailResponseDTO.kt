package com.copay.app.dto.profile.response

data class EmailResponseDTO(
    val id: Long,
    val email: String,
    val message: String? = null
)
