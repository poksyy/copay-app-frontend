package com.copay.app.dto.profile.response

data class PhoneNumberResponseDTO(
    val id: Long,
    val phone: String,
    val message: String? = null
)
