package com.copay.app.ui.screen

import AuthViewModelFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.config.RetrofitInstance
import com.copay.app.navigation.SpaScreens
import com.copay.app.repository.UserRepository
import com.copay.app.ui.components.BottomNavigationBar
import com.copay.app.utils.state.AuthState
import com.copay.app.viewmodel.AuthViewModel
import com.copay.app.viewmodel.NavigationViewModel

/**
 * Handles bottom navigation and screen switching.
 */
@Composable
fun HubScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    onLogoutSuccess: () -> Unit
) {
    val userRepository = remember { UserRepository(RetrofitInstance.authService) }
    val viewModelFactory = remember { AuthViewModelFactory(userRepository) }
    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)

    // Collect screen state updates.
    val currentScreen by navigationViewModel.currentScreen.collectAsState()

    val authState by authViewModel.authState.collectAsState()

    // Detect if user logged out. If successful change screen
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLogoutSuccess()
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentScreen.route,
                onRouteSelected = { newRoute ->
                    val newScreen = when (newRoute) {
                        "home" -> SpaScreens.Home
                        "plannings" -> SpaScreens.Plannings
                        "friends" -> SpaScreens.Friends
                        "profile" -> SpaScreens.Profile
                        else -> SpaScreens.Home
                    }
                    navigationViewModel.navigateTo(newScreen)
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            // Render current screen based on navigation state.
            when (currentScreen) {

                SpaScreens.Home -> HomeScreen()
                SpaScreens.JoinGroup -> JoinGroupScreen()
                SpaScreens.CreateGroup -> CreateGroupScreen()
                SpaScreens.Plannings -> {} // TODO
                SpaScreens.Friends -> {} // TODO
                SpaScreens.Profile -> ProfileScreen()
            }
        }
    }
}