package com.copay.app.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.GradientBackground
import com.copay.app.ui.components.bottomNavigationBar
import com.copay.app.ui.screen.group.create.CreateGroupScreen
import com.copay.app.ui.screen.group.edit.editGroupScreen
import com.copay.app.ui.screen.group.detail.groupBalancesScreen
import com.copay.app.ui.screen.group.edit.editGroupDescriptionScreen
import com.copay.app.ui.screen.group.edit.editGroupMembersScreen
import com.copay.app.ui.screen.group.edit.editGroupNameScreen
import com.copay.app.ui.screen.group.edit.editGroupPriceScreen
import com.copay.app.ui.screen.group.edit.searchPhotoScreen
import com.copay.app.ui.screen.profile.edit.editProfileScreen
import com.copay.app.ui.screen.profile.profileScreen
import com.copay.app.ui.screen.profile.edit.editEmailScreen
import com.copay.app.ui.screen.profile.edit.editPhoneNumberScreen
import com.copay.app.ui.screen.profile.edit.editUsernameScreen
import com.copay.app.ui.screen.profile.security.changePasswordScreen
import com.copay.app.utils.state.AuthState
import com.copay.app.viewmodel.AuthViewModel
import com.copay.app.viewmodel.NavigationViewModel

/**
 * Handles bottom navigation and screen switching.
 */

@Composable
fun hubScreen(
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
            bottomNavigationBar(
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

        GradientBackground {
            Column(modifier = Modifier.padding(paddingValues))  {

                when (currentScreen) {
                    // Main pages.
                    SpaScreens.Home -> homeScreen()
                    SpaScreens.Plannings -> {} // TODO
                    SpaScreens.Friends -> {} // TODO
                    SpaScreens.Profile -> profileScreen()

                    // Home subpages.
                    SpaScreens.CreateGroup -> CreateGroupScreen()
                    SpaScreens.BalancesGroup -> groupBalancesScreen()

                    // Group editing subpages.
                    is SpaScreens.GroupSubscreen -> when (currentScreen) {
                        SpaScreens.GroupSubscreen.EditGroup -> editGroupScreen()
                        SpaScreens.GroupSubscreen.EditName -> editGroupNameScreen()
                        SpaScreens.GroupSubscreen.EditDescription -> editGroupDescriptionScreen()
                        SpaScreens.GroupSubscreen.EditPrice -> editGroupPriceScreen()
                        SpaScreens.GroupSubscreen.EditMembers -> editGroupMembersScreen()
                        SpaScreens.GroupSubscreen.SearchPhoto -> searchPhotoScreen()
                        else -> {}
                    }

                    // Profile subpages.
                    is SpaScreens.ProfileSubscreen -> when (currentScreen) {

                        // Profile Editing.
                        SpaScreens.ProfileSubscreen.EditProfile -> editProfileScreen()
                        SpaScreens.ProfileSubscreen.EditUsername -> editUsernameScreen()
                        SpaScreens.ProfileSubscreen.EditEmail -> editEmailScreen()
                        SpaScreens.ProfileSubscreen.EditPhoneNumber -> editPhoneNumberScreen()

                        // Profile Security.
                        SpaScreens.ProfileSubscreen.ChangePassword -> changePasswordScreen()

                        else -> {}
                    }
                }
            }
        }
    }
}
