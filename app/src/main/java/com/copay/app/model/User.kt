package com.copay.app.model

/**
 * Represents a User entity used for authentication and data transfer.
 * This model is used for both login, registration, and user responses.
 */

data class User(

    val token: String? = null,
    val userId: Long? = null,
    val username: String? = null,
    val phonePrefix: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val password: String? = null
)
