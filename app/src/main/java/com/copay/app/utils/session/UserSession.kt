package com.copay.app.utils.session

import com.copay.app.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    fun setUser(phonePrefix: String?, phoneNumber: String?, userId: Long?, username: String?, email: String?) {
        // Check if the user ID, username, and email are not null before setting the user data.
        if (userId != null && username != null && email != null) {
            // Create a new User instance with the provided credentials and update the session state.
            _user.value = User(
                userId = userId,
                username = username,
                email = email,
                phonePrefix = phonePrefix,
                phoneNumber = phoneNumber
            )
        }
    }

    fun clearUser() {
        _user.value = null
    }
}