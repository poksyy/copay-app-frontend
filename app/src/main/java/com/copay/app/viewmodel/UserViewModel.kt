package com.copay.app.viewmodel

import androidx.lifecycle.ViewModel
import com.copay.app.model.User
import com.copay.app.utils.session.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userSession: UserSession
) : ViewModel() {

    val user: StateFlow<User?> = userSession.user

    fun clearUser() {
        userSession.clearUser()
    }
}

