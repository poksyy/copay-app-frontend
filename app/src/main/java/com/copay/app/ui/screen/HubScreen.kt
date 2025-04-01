package com.copay.app.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.BottomNavigationBar

@Composable
fun HubScreen() {
    // Tracks the current screen in the bottom navigation.
    var currentScreen by remember { mutableStateOf<SpaScreens>(SpaScreens.Home) }
    // Flag to handle navigation state from bottom bar clicks.
    var navigatingFromBottomBar by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentScreen.route,
                onRouteSelected = { newRoute ->
                    // Set flag when navigation occurs from bottom bar.
                    navigatingFromBottomBar = true
                    currentScreen = when (newRoute) {
                        "home" -> SpaScreens.Home
                        "plannings" -> SpaScreens.Plannings
                        "friends" -> SpaScreens.Friends
                        "profile" -> SpaScreens.Profile
                        else -> SpaScreens.Home
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                // Pass navigation state to HomeScreen and handle reset.
                SpaScreens.Home -> HomeScreen(
                    onNavigateFromBottomBar = navigatingFromBottomBar,
                    onNavigationComplete = { navigatingFromBottomBar = false }
                )
                SpaScreens.Plannings -> {}
                SpaScreens.Friends -> {}
                SpaScreens.Profile -> {}

                // These screens are handled within HomeScreen's navigation
                SpaScreens.JoinGroup, SpaScreens.CreateGroup -> {
                    // Redirect to Home if these screens are somehow reached directly.
                    currentScreen = SpaScreens.Home
                    HomeScreen(
                        onNavigateFromBottomBar = navigatingFromBottomBar,
                        onNavigationComplete = { navigatingFromBottomBar = false }
                    )
                }
            }
        }
    }
}