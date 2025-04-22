package com.copay.app.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.BottomNavigationBar
import com.copay.app.ui.screen.profile.EditProfileScreen
import com.copay.app.ui.screen.profile.ProfileScreen
import com.copay.app.ui.screen.profile.edit.EditEmailScreen
import com.copay.app.ui.screen.profile.edit.EditPhoneNumberScreen
import com.copay.app.ui.screen.profile.edit.EditUsernameScreen
import com.copay.app.ui.screen.profile.security.ChangePasswordScreen
import com.copay.app.utils.state.AuthState
import com.copay.app.viewmodel.AuthViewModel
import com.copay.app.viewmodel.NavigationViewModel

/**
 * Handles bottom navigation and screen switching.
 */
@Composable
fun HubScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onLogoutSuccess: () -> Unit
) {

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

                // Main pages.
                SpaScreens.Home -> HomeScreen()
                SpaScreens.Plannings -> {} // TODO
                SpaScreens.Friends -> {} // TODO
                SpaScreens.Profile -> ProfileScreen()

                // Home subpages.
                SpaScreens.JoinGroup -> JoinGroupScreen()
                SpaScreens.CreateGroup -> CreateGroupScreen()

                // Profile subpages.
                is SpaScreens.ProfileSubscreen -> when (currentScreen) {

                    // Profile Editing.
                    SpaScreens.ProfileSubscreen.EditProfile -> EditProfileScreen()
                    SpaScreens.ProfileSubscreen.EditUsername -> EditUsernameScreen()
                    SpaScreens.ProfileSubscreen.EditEmail -> EditEmailScreen()
                    SpaScreens.ProfileSubscreen.EditPhoneNumber -> EditPhoneNumberScreen()

                    // Profile Security.
                    SpaScreens.ProfileSubscreen.ChangePassword -> ChangePasswordScreen()

                    else -> {}
                }
            }
        }
    }
}