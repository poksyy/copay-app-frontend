package com.copay.app.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.copay.app.navigation.Screen
import com.copay.app.ui.components.BottomNavigationBar

@Composable
fun HubScreen() {
    // Tracks the current screen in the bottom navigation.
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
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
                        "home" -> Screen.Home
                        "plannings" -> Screen.Plannings
                        "friends" -> Screen.Friends
                        "profile" -> Screen.Profile
                        else -> Screen.Home
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                // Pass navigation state to HomeScreen and handle reset.
                Screen.Home -> HomeScreen(
                    onNavigateFromBottomBar = navigatingFromBottomBar,
                    onNavigationComplete = { navigatingFromBottomBar = false }
                )
                Screen.Plannings -> {}
                Screen.Friends -> {}
                Screen.Profile -> {}

                // These screens are handled within HomeScreen's navigation
                Screen.JoinGroup, Screen.CreateGroup -> {
                    // Redirect to Home if these screens are somehow reached directly.
                    currentScreen = Screen.Home
                    HomeScreen(
                        onNavigateFromBottomBar = navigatingFromBottomBar,
                        onNavigationComplete = { navigatingFromBottomBar = false }
                    )
                }
            }
        }
    }
}