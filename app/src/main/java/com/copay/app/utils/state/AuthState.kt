package com.copay.app.utils.state

/**
 * Different authentication states for UI handling
 */
sealed class AuthState {

    // Initial state when no action is performed
    object Idle : AuthState()

    // Ongoing authentication process
    object Loading : AuthState()

    // Successful authentication
    object Success : AuthState()

    data class Error(val message: String) : AuthState()
}
