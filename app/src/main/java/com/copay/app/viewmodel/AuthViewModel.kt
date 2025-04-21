package com.copay.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.repository.UserRepository
import com.copay.app.service.UserService
import com.copay.app.utils.DataStoreManager
import com.copay.app.utils.state.AuthState
import com.copay.app.utils.state.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val userSession: UserSession
) : ViewModel() {

    init {
        Log.d("AuthViewModel", "ViewModel created")
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> get() = _authState

    fun login(context: Context, phoneNumber: String, password: String) {
        viewModelScope.launch {

            _authState.value = AuthState.Loading
            // Stores the response from backend.
            val backendResponse = userRepository.login(context, phoneNumber, password)
            // Updates the UI trough authState.
            _authState.value = backendResponse

            // Only execute if the response is success.
            if (backendResponse is AuthState.Success) {
                // Extracts the user details through the userService.
                val extractedUser = userService.extractUser(backendResponse)
                extractedUser?.let {
                    // Set the user information trough userSession.
                    userSession.setUser(it.phonePrefix, it.phoneNumber, it.userId, it.username, it.email)
                }
            }
        }
    }

    // Handles the second step of user registration (phone number verification).
    fun registerStepOne(context: Context, username: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {

            _authState.value = AuthState.Loading
            // Updates the UI trough authState.
            _authState.value = userRepository.registerStepOne(context, username, email, password, confirmPassword)
        }
    }
    // Handles the first step of user registration.
    fun registerStepTwo(context: Context, phonePrefix: String, phoneNumber: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // Stores the response from backend.
            val backendResponse = userRepository.registerStepTwo(context, phonePrefix, phoneNumber)
            // Updates the UI through authState.
            _authState.value = backendResponse

            if (backendResponse is AuthState.Success) {
                // Extracts the user details through the userService.
                val extractedUser = userService.extractUser(backendResponse)
                extractedUser?.let {
                    // Set the user information through userSession.
                    userSession.setUser(it.phonePrefix, it.phoneNumber, it.userId, it.username, it.email)
                }
            }
        }
    }

    // Logs out the user by clearing the stored authentication token.
    fun logout(context: Context) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            DataStoreManager.getToken(context).first()?.let { token ->
                userRepository.logout(context, token)
            }
            DataStoreManager.clearToken(context)
            _authState.value = AuthState.Success(null)
        }
    }
}