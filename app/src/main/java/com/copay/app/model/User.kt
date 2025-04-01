package com.copay.app.model

/**
 * Represents a User entity used for authentication and data transfer.
 * This model is used for both login, registration, and user responses.
 */

data class User(
    val username: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val token: String? = null
)
