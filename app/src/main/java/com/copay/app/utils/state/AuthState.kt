package com.copay.app.utils.state

import com.copay.app.model.User

/**
 * Different authentication states for UI handling
 */
sealed class AuthState {

    // Initial state when no action is performed
    data object Idle : AuthState()

    // Ongoing authentication process
    data object  Loading : AuthState()

    // Successful authentication
    data class Success(val user: User) : AuthState()

    data class Error(val message: String) : AuthState()
}
