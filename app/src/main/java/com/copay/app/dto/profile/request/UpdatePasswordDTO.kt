package com.copay.app.dto.profile.request

/**
 * Data class representing a request to update a user's password.
 */
data class UpdatePasswordDTO(
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)
