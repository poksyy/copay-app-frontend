package com.copay.app.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.copay.app.config.ApiService
import com.copay.app.model.User

class UserViewModel : ViewModel() {

    // Use of mutableStateOf for controlling the user state.
    private val _user = mutableStateOf<User?>(null)
    val user = _user

    // Method in case the username changes.
    fun updateUser(user: User) {
        _user.value = user
    }
}
