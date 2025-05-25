package com.copay.app.utils.state

import com.copay.app.dto.notification.response.NotificationListResponseDTO

/**
 * Different notification states for UI handling.
 */
sealed class NotificationState {

    // Initial idle state.
    data object Idle : NotificationState()

    // Loading state while performing notification-related operations.
    data object Loading : NotificationState()

    // Notification operations completed successfully.
    sealed class Success : NotificationState() {

        // When a list of notifications is successfully retrieved.
        data class NotificationList(val data: NotificationListResponseDTO) : Success()

        // When a generic message is returned (e.g., mark as read, delete).
        data class Message(val message: String) : Success()
    }

    // An error occurred during notification operations.
    data class Error(val message: String) : NotificationState()
}
