package com.copay.app.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * DataStoreManager is responsible for handling the secure storage of the JWT authentication token.
 * It uses DataStore Preferences API to persist the token locally, allowing the app to manage
 * user authentication across sessions. This class provides functions to:
 */

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

object DataStoreManager {
    private val TOKEN_KEY = stringPreferencesKey("jwt_token")

    // Save JWT token when the user logs in.
    suspend fun saveToken(context: Context, token: String) {
        Log.d("DataStoreManager", "Saving token: $token")
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    // Retrieve JWT token to maintain authentication state.
    fun getToken(context: Context): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    // Clear JWT token (Logout)
    suspend fun clearToken(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

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

    suspend fun testDataStore(context: Context) {
        val testToken = "sample_jwt_token"

        saveToken(context, testToken)
        Log.d("DataStoreTest", "Token saved: $testToken")

        val retrievedToken = getToken(context).first()
        Log.d("DataStoreTest", "Token retrieved: $retrievedToken")

        clearToken(context)
        Log.d("DataStoreTest", "Token cleared")

        val emptyToken = getToken(context).first()
        Log.d("DataStoreTest", "Token after clearing: $emptyToken")
    }

}