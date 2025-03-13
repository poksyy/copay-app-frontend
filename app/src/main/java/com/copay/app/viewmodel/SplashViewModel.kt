package com.copay.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SplashViewModel : ViewModel() {
    private val _isDataLoaded = MutableStateFlow(false)
    val isDataLoaded: StateFlow<Boolean> get() = _isDataLoaded

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            delay(5000)
            _isDataLoaded.value = true
        }
    }
}
