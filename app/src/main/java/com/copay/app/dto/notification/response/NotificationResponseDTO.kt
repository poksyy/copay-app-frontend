package com.copay.app.dto.notification.response

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a single notification.
 */

data class NotificationResponseDTO(
    val notificationId: Long,
    val userId: Long,
    val message: String,
    val read: Boolean,
    val createdAt: String
)
