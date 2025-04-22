package com.copay.app.dto.request.profile

data class UpdatePasswordDTO(
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)
