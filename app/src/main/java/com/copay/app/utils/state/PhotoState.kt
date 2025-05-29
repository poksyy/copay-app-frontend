package com.copay.app.utils.state

import com.copay.app.dto.unsplash.response.UnsplashResponse

/**
 * Different photo states for UI handling
 */
sealed class PhotoState {

    // No action has been taken yet
    data object Idle : PhotoState()

    // Loading state while searching photos
    data object Loading : PhotoState()

    // Success states
    sealed class Success : PhotoState() {
        // Photos loaded successfully
        data class PhotoList(val data: UnsplashResponse) : Success()

        // Photo set successfully
        data class PhotoSet(val message: String) : Success()

        // Photo removed successfully
        data class PhotoRemoved(val message: String) : Success()
    }

    // An error occurred while searching photos
    data class Error(val message: String) : PhotoState()
}
