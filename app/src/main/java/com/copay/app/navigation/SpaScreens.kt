package com.copay.app.navigation

sealed class Screen(val route: String) {

    data object Home : Screen("home")
    data object Plannings : Screen("plannings")
    data object Friends : Screen("friends")
    data object Profile : Screen("profile")
}