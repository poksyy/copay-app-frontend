package com.copay.app.dto.response.profile

data class PhoneNumberResponseDTO(
    val id: Long,
    val phone: String,
    val message: String? = null
)
