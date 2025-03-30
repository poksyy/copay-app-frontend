package com.copay.app.navigation

sealed class Screen(val route: String) {

    // HomeScreen navigation routes.
    data object Home : Screen("home")
    data object JoinGroup : Screen("join_group")
    data object CreateGroup : Screen("create_group")

    data object Plannings : Screen("plannings")
    data object Friends : Screen("friends")
    data object Profile : Screen("profile")

}