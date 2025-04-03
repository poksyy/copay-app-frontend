package com.copay.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.dto.response.LoginResponseDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {


    private val _user = MutableStateFlow<LoginResponseDTO?>(null)
    val user: StateFlow<LoginResponseDTO?> = _user


    fun setUser(userResponse: LoginResponseDTO) {
        _user.value = userResponse
    }


    fun clearUser() {
        _user.value = null
    }
}
