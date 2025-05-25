package com.copay.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.dto.notification.response.NotificationResponseDTO
import com.copay.app.repository.NotificationRepository
import com.copay.app.utils.state.NotificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * NotificationViewModel manages notification-related UI state and business logic.
 * It communicates with the NotificationRepository to fetch, update, or delete
 * notifications, and exposes state updates to the UI using StateFlow.
 */

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _notificationState = MutableStateFlow<NotificationState>(NotificationState.Idle)
    val notificationState: MutableStateFlow<NotificationState> get() = _notificationState

    fun resetNotificationState() {
        _notificationState.value = NotificationState.Idle
    }

    // List of all notifications.
    private val _allNotifications = MutableStateFlow<List<NotificationResponseDTO>>(emptyList())
    val allNotifications: StateFlow<List<NotificationResponseDTO>> get() = _allNotifications

    // List of unread notifications.
    private val _unreadNotifications = MutableStateFlow<List<NotificationResponseDTO>>(emptyList())
    val unreadNotifications: StateFlow<List<NotificationResponseDTO>> get() = _unreadNotifications

    // Save the list of groups to avoid unnecessary reloads (Caché).
    private var cachedNotificationIds: Set<Long> = emptySet()

    // Notice if there are new fetched notifications different from cached.
    val hasNewNotifications = MutableStateFlow(false)

    fun getAllNotifications(context: Context) {
        viewModelScope.launch {

            _notificationState.value = NotificationState.Loading

            val backendResponse = repository.getAllNotifications(context)

            if (backendResponse is NotificationState.Success.NotificationList) {
                _allNotifications.value = backendResponse.data.notifications
                _notificationState.value = backendResponse
            }
        }
    }

    fun getUnreadNotifications(context: Context) {
        viewModelScope.launch {

            _notificationState.value = NotificationState.Loading

            val backendResponse = repository.getUnreadNotifications(context)

            if (backendResponse is NotificationState.Success.NotificationList) {
                _unreadNotifications.value = backendResponse.data.notifications
                _notificationState.value = backendResponse
            }
        }
    }

    fun markNotificationAsRead(context: Context, notificationId: Long) {
        viewModelScope.launch {

            _notificationState.value = NotificationState.Loading

            val backendResponse = repository.markNotificationAsRead(context, notificationId)

            _notificationState.value = backendResponse

            // Update the list of notifications.
            getAllNotifications(context)
            getUnreadNotifications(context)
        }
    }

    fun markAllNotificationsAsRead(context: Context) {
        viewModelScope.launch {

            _notificationState.value = NotificationState.Loading

            val backendResponse = repository.markAllNotificationsAsRead(context)

            _notificationState.value = backendResponse

            // Update the list of notifications.
            getAllNotifications(context)
            getUnreadNotifications(context)
        }
    }

    fun deleteNotification(context: Context, notificationId: Long) {
        viewModelScope.launch {

            _notificationState.value = NotificationState.Loading

            val backendResponse = repository.deleteNotification(context, notificationId)

            _notificationState.value = backendResponse

            // Update the list of notifications.
            getAllNotifications(context)
            getUnreadNotifications(context)
        }
    }

    // Method to refresh notifications in background.
    private suspend fun refreshNotificationsSilently(context: Context) {
        val backendResponse = repository.getAllNotifications(context)

        if (backendResponse is NotificationState.Success.NotificationList) {
            val newNotificationIds =
                backendResponse.data.notifications.mapNotNull { it.notificationId }.toSet()

            if (newNotificationIds != cachedNotificationIds) {
                hasNewNotifications.value = true
                cachedNotificationIds = newNotificationIds
            }
        }
    }


    // Method to auto-refresh continuously every 5 seconds to get new notifications.
    fun autoRefreshNotifications(context: Context) {
        viewModelScope.launch {
            while (true) {
                refreshNotificationsSilently(context)
                kotlinx.coroutines.delay(5_000)
            }
        }
    }
}
