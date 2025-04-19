package com.copay.app.utils.state

/**
 * Different authentication states for UI handling
 */
sealed class GroupState {

    // Initial state when no action is performed.
    data object Idle : GroupState()

    // Ongoing petition process.
    data object  Loading : GroupState()

    // Successful creation of the group.
    data class Success(val user: Any?) : GroupState()

    // Error message in the ui.
    data class Error(val message: String) : GroupState()
}
