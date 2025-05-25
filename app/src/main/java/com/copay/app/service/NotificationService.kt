package com.copay.app.service

import com.copay.app.config.ApiService
import com.copay.app.dto.MessageResponseDTO
import com.copay.app.dto.notification.response.NotificationListResponseDTO
import retrofit2.Response

/**
 * NotificationService handles operations related to notifications,
 * such as fetching, marking as read, and deleting notifications.
 */

class NotificationService(private val api: ApiService) {

    // Fetches all notifications for the authenticated user.
    suspend fun getAllNotifications(token: String): Response<NotificationListResponseDTO> {
        return api.getAllNotifications(token)
    }

    // Fetches only unread notifications for the authenticated user.
    suspend fun getUnreadNotifications(token: String): Response<NotificationListResponseDTO> {
        return api.getUnreadNotifications(token)
    }

    // Marks a specific notification as read.
    suspend fun markNotificationAsRead(notificationId: Long, token: String): Response<MessageResponseDTO> {
        return api.markNotificationAsRead(notificationId, token)
    }

    // Marks all notifications as read for the authenticated user.
    suspend fun markAllNotificationsAsRead(token: String): Response<MessageResponseDTO> {
        return api.markAllNotificationsAsRead(token)
    }

    // Deletes a specific notification by its ID.
    suspend fun deleteNotification(notificationId: Long, token: String): Response<MessageResponseDTO> {
        return api.deleteNotification(notificationId, token)
    }
}
