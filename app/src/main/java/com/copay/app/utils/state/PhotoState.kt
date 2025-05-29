package com.copay.app.utils.state

import com.copay.app.dto.unsplash.UnsplashResponse

/**
 * Different photo states for UI handling
 */
sealed class PhotoState {

    // No action has been taken yet
    data object Idle : PhotoState()

    // Loading state while searching photos
    data object Loading : PhotoState()

    // Photos loaded successfully
    data class Success(val data: UnsplashResponse) : PhotoState()

    // An error occurred while searching photos
    data class Error(val message: String) : PhotoState()
}