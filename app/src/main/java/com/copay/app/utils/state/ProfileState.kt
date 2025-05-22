package com.copay.app.utils.state

import com.copay.app.dto.user.UserResponseDTO

/**
 * Different profile states for UI handling
 */
sealed class ProfileState {

    // No action has been taken yet
    data object Idle : ProfileState()

    // Loading state while updating profile
    data object Loading : ProfileState()

    // Profile updated successfully
    open class Success : ProfileState() {
        class GetUser(val data: UserResponseDTO) : Success()
        class EmailUpdated(val data: Any?) : Success()
        class UsernameUpdated(val data: Any?) : Success()
        class PhoneUpdated(val data: Any?) : Success()
        class PasswordUpdated(val data: Any?) : Success()
    }

    // An error occurred while updating profile
    data class Error(val message: String) : ProfileState()
}