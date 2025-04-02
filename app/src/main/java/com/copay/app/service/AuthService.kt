package com.copay.app.service

import android.content.Context
import android.util.Log
import com.copay.app.utils.DataStoreManager
import com.copay.app.utils.state.AuthState
import retrofit2.Response
import com.copay.app.model.User

/**
 * AuthService is responsible for managing authentication-related API calls.
 * It provides functions to perform user login, registration, and other
 * authentication actions. This class interacts with the backend to verify
 * credentials, manage JWT tokens, and handle session persistence.
 * It acts as the main service for user authentication within the app.
 */

object AuthService {
    // Generic handler that can work with any response type.
    suspend fun <T : Any> handleResponse(
        context: Context,
        response: Response<T>
    ): AuthState {
        val responseBody = response.body()

        return if (responseBody != null && response.isSuccessful) {
            try {
                // Extract token using reflection
                val tokenField = responseBody::class.java.getDeclaredField("token")
                tokenField.isAccessible = true
                val token = tokenField.get(responseBody) as? String

                return if (token != null) {
                    // Save token
                    DataStoreManager.saveToken(context, token)

                    // Extract username, phoneNumber and email from backend DTO response (if they exist).
                    val username = getFieldValueSafely(responseBody, "username") ?: "Not sent"
                    val phoneNumber = getFieldValueSafely(responseBody, "phoneNumber") ?: "Not sent"
                    val email = getFieldValueSafely(responseBody, "email") ?: "Not sent"
                    val isLogin = getFieldValueSafely(responseBody, "isLogin") ?: "Not sent"

                    if (phoneNumber == "Not sent") {
                        Log.d(
                            "AuthService",
                            "RegisterStepOne response detected, skipping phoneNumber."
                        )
                    } else if (isLogin == "true") {
                        Log.d("AuthService", "Login response detected, received user information.");

                    } else {
                        Log.d(
                            "AuthService",
                            "RegisterStepTwo response detected, received all user information."
                        );
                    }

                    // Create user.
                    val user = User(
                        username = username,
                        phoneNumber = phoneNumber,
                        email = email,
                        token = token
                    )

                    Log.d("AuthService", "User logged: $user")
                    AuthState.Success(user)
                } else {
                    AuthState.Error("Invalid token received")
                }
            } catch (e: NoSuchFieldException) {
                Log.e("AuthService", "Required field 'token' not found in response: ${e.message}")
                AuthState.Error("Authentication failed: Invalid response format")

            } catch (e: Exception) {
                Log.e("AuthService", "Error processing response: ${e.message}")
                AuthState.Error("Failed to process response: ${e.message}")
            }
        } else {
            // Extract error message if available.
            val errorMessage = if (responseBody != null) {
                try {
                    getFieldValueSafely(responseBody, "message") ?: "Request failed"
                } catch (e: Exception) {
                    "Request failed"
                }
            } else {
                response.message() ?: "Request failed"
            }

            AuthState.Error(errorMessage)
        }
    }

    // Helper function to safely get field values using reflection
    private fun <T : Any, R> getFieldValueSafely(obj: T, fieldName: String): R? {
        return try {
            val field = obj::class.java.getDeclaredField(fieldName)
            field.isAccessible = true
            field.get(obj) as? R
        } catch (e: NoSuchFieldException) {
            null
        } catch (e: Exception) {
            Log.e("AuthService", "Error getting field '$fieldName': ${e.message}")
            null
        }
    }
}
