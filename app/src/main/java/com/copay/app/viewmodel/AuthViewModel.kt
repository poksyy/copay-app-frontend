package com.copay.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.dto.response.JwtResponse
import com.copay.app.repository.UserRepository
import com.copay.app.utils.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private suspend fun handleResponse(context: Context, response: Response<JwtResponse>) {
        val jwtResponse = response.body()
        if (jwtResponse != null && response.isSuccessful && jwtResponse.token != null) {
            DataStoreManager.saveToken(context, jwtResponse.token)
            _authState.value = AuthState.Success
        } else {
            val errorMessage = jwtResponse?.message ?: "Request failed"
            _authState.value = AuthState.Error(errorMessage)
        }
    }

    fun login(context: Context, phoneNumber: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val response = userRepository.login(phoneNumber, password)
            handleResponse(context, response)
        }
    }

    fun registerStepOne(context: Context, username: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val response = userRepository.registerStepOne(username, email, password, confirmPassword)
            handleResponse(context, response)
        }
    }

    fun registerStepTwo(context: Context, phoneNumber: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val response = userRepository.registerStepTwo(context, phoneNumber)
            handleResponse(context, response)
        }
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            DataStoreManager.clearToken(context)
            _authState.value = AuthState.Idle
        }
    }
}
