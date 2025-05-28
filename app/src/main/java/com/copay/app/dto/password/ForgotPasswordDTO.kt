package com.copay.app.dto.password

/**
 * Data class representing the request body for forgot password sending email.
 */
data class ForgotPasswordDTO(
    val email: String
)