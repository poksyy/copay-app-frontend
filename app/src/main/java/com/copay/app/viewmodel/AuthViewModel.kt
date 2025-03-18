package com.copay.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.dto.response.JwtResponse
import com.copay.app.repository.UserRepository
import com.copay.app.utils.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(context: Context, phoneNumber: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val response = userRepository.login(phoneNumber, password)
            val jwtResponse = response.body()

            if (jwtResponse != null && response.isSuccessful && jwtResponse.token != null) {
                DataStoreManager.saveToken(context, jwtResponse.token)
                _authState.value = AuthState.Success
            } else {
                val errorMessage = jwtResponse?.message ?: "Login failed"
                _authState.value = AuthState.Error(errorMessage)
            }
        }
    }

    fun register(
        context: Context,
        username: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val response = userRepository.register(username, email, phoneNumber, password, confirmPassword)
            val jwtResponse = response.body()

            if (jwtResponse != null && response.isSuccessful && jwtResponse.token != null) {
                DataStoreManager.saveToken(context, jwtResponse.token)
                _authState.value = AuthState.Success
            } else {
                val errorMessage = jwtResponse?.message ?: "Registration failed"
                _authState.value = AuthState.Error(errorMessage)
            }
        }
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            DataStoreManager.clearToken(context)
            _authState.value = AuthState.Idle
        }
    }
}
