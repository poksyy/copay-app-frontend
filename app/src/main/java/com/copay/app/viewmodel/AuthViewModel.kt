package com.copay.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.repository.UserRepository
import com.copay.app.utils.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _authState = MutableStateFlow<Boolean?>(null)
    val authState: StateFlow<Boolean?> = _authState

    fun login(context: Context, phoneNumber: String, password: String) {
        viewModelScope.launch {
            val response = userRepository.login(phoneNumber, password)
            response.body()?.let { jwtResponse ->
                DataStoreManager.saveToken(context, jwtResponse.token)
                _authState.value = true
            } ?: run {
                _authState.value = false
            }
        }
    }

    fun register(context: Context, username: String, email: String, phoneNumber: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            val response = userRepository.register(username, email,  phoneNumber, password, confirmPassword)
            response.body()?.let { jwtResponse ->
                DataStoreManager.saveToken(context, jwtResponse.token)
                _authState.value = true
            } ?: run {
                _authState.value = false
            }
        }
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            DataStoreManager.clearToken(context)
            _authState.value = false
        }
    }
}
