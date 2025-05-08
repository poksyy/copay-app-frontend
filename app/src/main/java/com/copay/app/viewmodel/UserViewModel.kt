package com.copay.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.model.User
import com.copay.app.repository.ProfileRepository
import com.copay.app.utils.session.UserSession
import com.copay.app.utils.state.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userSession: UserSession
) : ViewModel() {

    private val _userState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val userState: MutableStateFlow<ProfileState> get() = _userState

    val user: StateFlow<User?> = userSession.user

    fun getUserByPhone(context: Context, phoneNumber: String) {
        viewModelScope.launch {

            _userState.value = ProfileState.Loading

            val backendResponse = profileRepository.userByPhone(context, phoneNumber)

            _userState.value = backendResponse
        }
    }

    fun clearUser() {
        userSession.clearUser()
    }
}

