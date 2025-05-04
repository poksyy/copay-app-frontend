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

    // Internal storage for current screen state (Home by default).
    private val _currentScreen = MutableStateFlow<SpaScreens>(SpaScreens.Home)
    val currentScreen: StateFlow<SpaScreens> = _currentScreen

    // Stack of visited screens for back navigation.
    private val navigationHistory = mutableListOf<SpaScreens>()

    // Navigates to new screen while managing back stack.
    fun navigateTo(screen: SpaScreens) {
        _currentScreen.value.let {
            if (it != screen) navigationHistory.add(it)
        }
        _currentScreen.value = screen
    }

    // Navigates back to previous screen if available.
    fun navigateBack(): Boolean {
        return navigationHistory.removeLastOrNull()?.let { previousScreen ->
            _currentScreen.value = previousScreen
            true
        } ?: false
    }

    // Clears all navigation history.
    fun clearHistory() {
        navigationHistory.clear()
    }
}
