package com.copay.app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.copay.app.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    // Update the UserViewModel with the extracted fields from the Backend response.
    fun setUser(phoneNumber: String?, userId: Long?, username: String?, email: String?) {
        Log.d("UserViewModel", "Setting user: phoneNumber=$phoneNumber, userId=$userId, username=$username, email=$email")
        _user.value = User(
            userId = userId,
            username = username,
            email = email,
            phoneNumber = phoneNumber
        )
        Log.d("UserViewModel", "User set: ${_user.value}")
    }

    fun clearUser() {
        _user.value = null
    }
}
