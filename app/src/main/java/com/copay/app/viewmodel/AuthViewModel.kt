package com.copay.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.repository.UserRepository
import com.copay.app.utils.DataStoreManager
import com.copay.app.service.AuthService
import com.copay.app.utils.state.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Holds the current authentication state.
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // Handles user login by calling the repository and processing the response.
    fun login(context: Context, phoneNumber: String, password: String) {

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val response = userRepository.login(phoneNumber, password)
            _authState.value = AuthService.handleResponse(context, response)
        }
    }

    // Handles the first step of user registration.
    fun registerStepOne(context: Context, username: String, email: String, password: String, confirmPassword: String) {

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val response = userRepository.registerStepOne(username, email, password, confirmPassword)
            _authState.value = AuthService.handleResponse(context, response)
        }
    }

    // Handles the second step of user registration (phone number verification).
    fun registerStepTwo(context: Context, phoneNumber: String) {

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val response = userRepository.registerStepTwo(context, phoneNumber)
            _authState.value = AuthService.handleResponse(context, response)
        }
    }

    // Logs out the user by clearing the stored authentication token.
    fun logout(context: Context) {

        viewModelScope.launch {
            DataStoreManager.clearToken(context)
            _authState.value = AuthState.Idle
        }
    }
}
