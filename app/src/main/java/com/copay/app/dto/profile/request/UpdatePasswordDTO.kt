package com.copay.app.dto.profile.request

data class UpdatePasswordDTO(
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)
