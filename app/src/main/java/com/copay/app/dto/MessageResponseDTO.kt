package com.copay.app.dto

/**
 * Generic response DTO used to return simple messages from the backend,
 * such as success or error notifications.
 */

data class MessageResponseDTO (
    val message: String
)