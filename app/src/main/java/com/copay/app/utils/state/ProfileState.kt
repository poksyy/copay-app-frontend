package com.copay.app.utils.state

/**
 * Different profile states for UI handling
 */
sealed class ProfileState {

    // No action has been taken yet
    data object Idle : ProfileState()

    // Loading state while updating profile
    data object Loading : ProfileState()

    // Profile updated successfully
    open class Success(open val data: Any?) : ProfileState() {
        class EmailUpdated(override val data: Any?) : Success(data)
        class UsernameUpdated(override val data: Any?) : Success(data)
        class PhoneUpdated(override val data: Any?) : Success(data)
    }

    // An error occurred while updating profile
    data class Error(val message: String) : ProfileState()
}