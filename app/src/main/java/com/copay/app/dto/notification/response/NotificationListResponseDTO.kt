package com.copay.app.dto.notification.response

/**
 * Data class representing a list of notifications and related counts.
 */

data class NotificationListResponseDTO(
    val notifications: List<NotificationResponseDTO>,
    val totalCount: Int,
    val unreadCount: Int
)
