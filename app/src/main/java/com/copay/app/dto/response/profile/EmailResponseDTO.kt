package com.copay.app.dto.response.profile

data class EmailResponseDTO(
    val id: Long,
    val email: String,
    val message: String? = null
)
