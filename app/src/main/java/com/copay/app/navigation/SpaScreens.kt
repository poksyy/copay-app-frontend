package com.copay.app.navigation

sealed class SpaScreens(val route: String) {

    // HomeScreen navigation routes.
    data object Home : SpaScreens("home")
    data object JoinGroup : SpaScreens("join_group")
    data object CreateGroup : SpaScreens("create_group")

    data object Plannings : SpaScreens("plannings")
    data object Friends : SpaScreens("friends")
    data object Profile : SpaScreens("profile")

}