package com.copay.app.utils

import android.content.Context
import kotlinx.coroutines.flow.first

object TokenUtils {

    suspend fun getFormattedToken(context: Context): String {

        // Get the token generated thanks to DataStoreManager.
        val token = DataStoreManager.getToken(context).first()

        // Send the token with "Bearer " since the backend needs that format.
        return "Bearer $token"
    }

    // Method to extract the token from the response.
    fun <T> extractToken(responseBody: T?): String? {
        return try {
            // Verify if body response is not null.
            responseBody?.let {
                val field = it.javaClass.getDeclaredField("token")
                field.isAccessible = true
                field.get(it) as? String
            }
            // Returns null if body is null.
        } catch (e: Exception) {
            null
        }
    }

}