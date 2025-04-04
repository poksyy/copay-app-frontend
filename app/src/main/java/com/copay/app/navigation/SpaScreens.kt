package com.copay.app.navigation

sealed class SpaScreens(val route: String) {

    // Main SpaScreens inside HubScreen.
    data object Home : SpaScreens("home")
    data object Plannings : SpaScreens("plannings")
    data object Friends : SpaScreens("friends")
    data object Profile : SpaScreens("profile")

    // HomeScreen subpages inside the SPA.
    data object JoinGroup : SpaScreens("join_group")
    data object CreateGroup : SpaScreens("create_group")

}