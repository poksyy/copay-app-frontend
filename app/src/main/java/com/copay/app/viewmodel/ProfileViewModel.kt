package com.copay.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.repository.ProfileRepository
import com.copay.app.utils.state.ProfileState
import com.copay.app.utils.state.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository, private val userSession: UserSession
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> get() = _profileState

    // Resets the UI state to idle.
    fun resetProfileState() {
        _profileState.value = ProfileState.Idle
    }

    // Updates the user's username.
    fun updateUsername(context: Context, userId: Long, newUsername: String) {
        viewModelScope.launch {

            _profileState.value = ProfileState.Loading

            // Calls the repository to perform the update.
            val backendResponse =
                profileRepository.updateUsername(context, userId, newUsername)

            // Updates the UI through profileState.
            _profileState.value = backendResponse

            // If the update was successful, update the user session.
            if (backendResponse is ProfileState.Success.UsernameUpdated) {
                val updatedUser = userSession.user.value?.copy(username = newUsername)
                updatedUser?.let {
                    userSession.setUser(it.phoneNumber, it.userId, it.username, it.email)
                }
            }
        }
    }

    // Updates the user's phone number.
    fun updatePhoneNumber(context: Context, userId: Long, newPhoneNumber: String) {
        viewModelScope.launch {

            _profileState.value = ProfileState.Loading

            // Calls the repository to perform the update.
            val backendResponse =
                profileRepository.updatePhoneNumber(context, userId, newPhoneNumber)

            // Updates the UI through profileState.
            _profileState.value = backendResponse

            // If the update was successful, update the user session.
            if (backendResponse is ProfileState.Success.PhoneUpdated) {
                val updatedUser = userSession.user.value?.copy(phoneNumber = newPhoneNumber)
                updatedUser?.let {
                    userSession.setUser(it.phoneNumber, it.userId, it.username, it.email)
                }
            }
        }
    }

    // Updates the user's email address.
    fun updateEmail(context: Context, userId: Long, newEmail: String) {
        viewModelScope.launch {

            _profileState.value = ProfileState.Loading

            // Calls the repository to perform the update.
            val backendResponse =
                profileRepository.updateEmail(context, userId, newEmail)

            // Updates the UI through profileState.
            _profileState.value = backendResponse

            // If the update was successful, update the user session.
            if (backendResponse is ProfileState.Success.EmailUpdated) {
                val updatedUser = userSession.user.value?.copy(email = newEmail)
                updatedUser?.let {
                    userSession.setUser(it.phoneNumber, it.userId, it.username, it.email)
                }
            }
        }
    }
}
