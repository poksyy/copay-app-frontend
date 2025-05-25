package com.copay.app.repository

import android.content.Context
import android.util.Log
import com.copay.app.dto.MessageResponseDTO
import com.copay.app.dto.notification.response.NotificationListResponseDTO
import com.copay.app.service.NotificationService
import com.copay.app.utils.DataStoreManager
import com.copay.app.utils.state.NotificationState
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response

/**
 * NotificationRepository handles all operations related to notifications,
 * including fetching, marking as read, and deleting notifications.
 */

class NotificationRepository(private val service: NotificationService) {

    suspend fun getAllNotifications(context: Context): NotificationState {

        val token = DataStoreManager.getFormattedToken(context)

        return handleApiResponse(context) {
            service.getAllNotifications(token)
        }
    }

    suspend fun getUnreadNotifications(context: Context): NotificationState {

        val token = DataStoreManager.getFormattedToken(context)

        return handleApiResponse(context) {
            service.getUnreadNotifications(token)
        }
    }

    suspend fun markNotificationAsRead(context: Context, notificationId: Long): NotificationState {

        val token = DataStoreManager.getFormattedToken(context)

        return handleApiResponse(context) {
            service.markNotificationAsRead(notificationId, token)
        }
    }

    suspend fun markAllNotificationsAsRead(context: Context): NotificationState {

        val token = DataStoreManager.getFormattedToken(context)

        return handleApiResponse(context) {
            service.markAllNotificationsAsRead(token)
        }
    }

    suspend fun deleteNotification(context: Context, notificationId: Long): NotificationState {

        val token = DataStoreManager.getFormattedToken(context)

        return handleApiResponse(context) {
            service.deleteNotification(notificationId, token)
        }
    }

    private suspend fun <T> handleApiResponse(
        context: Context, apiCall: suspend () -> Response<T>
    ): NotificationState {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    when (body) {
                        is NotificationListResponseDTO ->
                            NotificationState.Success.NotificationList(body)

                        is MessageResponseDTO ->
                            NotificationState.Success.Message(body.message)

                        else -> NotificationState.Error("Unexpected response type")
                    }
                } ?: NotificationState.Error("Empty response body")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("NotificationRepository", "Error Body: $errorBody")
                val message = extractErrorMessage(errorBody)
                NotificationState.Error(message ?: "Unknown error")
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Connection error: ${e.message}")
            NotificationState.Error("Connection error: ${e.message}")
        }
    }

    private fun extractErrorMessage(errorJson: String?): String? {
        if (errorJson.isNullOrEmpty()) return null
        return try {
            val jsonObject = Gson().fromJson(errorJson, JsonObject::class.java)
            jsonObject.get("message")?.asString
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error parsing error message: ${e.message}")
            null
        }
    }
}
