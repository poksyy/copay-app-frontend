package com.copay.app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.copay.app.navigation.SpaScreens

/**
 * Handles screen navigation logic and tracks current visible screen.
 * Defaults to Home screen when the app starts.
 */
class NavigationViewModel : ViewModel() {

    // Internal storage for current screen state (Home by default)
    private val _currentScreen = MutableStateFlow<SpaScreens>(SpaScreens.Home)
    val currentScreen: StateFlow<SpaScreens> = _currentScreen

    // Update current screen state.
    fun navigateTo(screen: SpaScreens) {
        _currentScreen.value = screen
    }
}
